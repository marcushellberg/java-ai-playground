package org.vaadin.marcus.springai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * * @author Christian Tzolov
 */
@Service
public class SpringAiAssistant {

    private static final Logger logger = LoggerFactory.getLogger(SpringAiAssistant.class);

    private static final int CHAT_HISTORY_WINDOW_SIZE = 40;

    @Value("classpath:/spring-ai/system-qa.st")
    private Resource systemPrompt;

    private final StreamingChatClient chatClient;

    private final VectorStore vectorStore;

    private final ChatHistory chatHistory;

    public SpringAiAssistant(StreamingChatClient chatClient, VectorStore vectorStore, ChatHistory chatHistory) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.chatHistory = chatHistory;
    }

    public Flux<String> chat(String chatId, String userMessageContent) {

        // Retrieve related documents to query
        List<Document> similarDocuments = this.vectorStore.similaritySearch(userMessageContent);

        Message systemMessage = getSystemMessage(similarDocuments,
                this.chatHistory.getLastN(chatId, CHAT_HISTORY_WINDOW_SIZE));

        logger.info("System Message: {}", systemMessage.getContent());

        UserMessage userMessage = new UserMessage(userMessageContent);

        this.chatHistory.addMessage(chatId, userMessage);

        // Ask the AI model
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return this.chatClient.stream(prompt).map((ChatResponse chatResponse) -> {

            if (!isValidResponse(chatResponse)) {
                logger.warn("ChatResponse or the result output is null!");
                return "";
            }

            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

            this.chatHistory.addMessage(chatId, assistantMessage);

            return (assistantMessage.getContent() != null) ? assistantMessage.getContent() : "";
        });
    }

    private boolean isValidResponse(ChatResponse chatResponse) {
        return chatResponse != null && chatResponse.getResult() != null
                && chatResponse.getResult().getOutput() != null;
    }

    private Message getSystemMessage(List<Document> similarDocuments, List<Message> conversationHistory) {

        String history = conversationHistory.stream()
                .map(m -> m.getMessageType().name().toLowerCase() + ": " + m.getContent())
                .collect(Collectors.joining(System.lineSeparator()));

        String documents = similarDocuments.stream().map(entry -> entry.getContent())
                .collect(Collectors.joining(System.lineSeparator()));

        // Needs to be created on each call as it is not thread safe.
        Message systemMessage = new SystemPromptTemplate(this.systemPrompt)
                .createMessage(Map.of(
                        "documents", documents,
                        "current_date", java.time.LocalDate.now(),
                        "history", history));

        return systemMessage;

    }
}