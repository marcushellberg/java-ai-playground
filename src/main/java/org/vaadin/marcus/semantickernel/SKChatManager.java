package org.vaadin.marcus.semantickernel;

import com.azure.ai.openai.models.ChatRequestAssistantMessage;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.util.StringUtils;

import java.util.List;

public class SKChatManager {

    private String chatId;
    private ChatHistory chatHistory;

    public SKChatManager(String chatId) {
        this.chatId = chatId;
        this.chatHistory = new ChatHistory("""
           You are a customer chat support agent of an airline named "Funnair",
           Respond in a friendly, helpful, and joyful manner.
           Before providing information about a booking or cancelling a booking,
           you MUST always get the following information from the user:
           booking number, customer first name and last name.
           Before changing a booking you MUST ensure it is permitted by the terms.
           If there is a charge for the change, you MUST ask the user to consent before proceeding.
           Today is {{current_date}}.
           """);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ChatHistory getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(ChatHistory chatHistory) {
        this.chatHistory = chatHistory;
    }


    private static final String IM_START_USER = "<|im_start|>user";
    private static final String IM_START_ASSISTANT = "<|im_start|>assistant";
    private static final String IM_START_SYSTEM = "<|im_start|>system";

    public String formatAsChatML() {
        StringBuilder sb = new StringBuilder();
        this.chatHistory.getMessages().forEach(message -> {
            if (message.getAuthorRole() == AuthorRole.USER) {
                sb.append(IM_START_USER).append("\n");
            } else if (message.getAuthorRole() == AuthorRole.SYSTEM) {
                sb.append(IM_START_SYSTEM).append("\n");
            } else if (message.getAuthorRole() == AuthorRole.ASSISTANT) {
                sb.append(IM_START_ASSISTANT).append("\n");
            }

            if (StringUtils.hasLength(message.getContent())) {
                sb.append(message.getContent()).append("\n").append("|im_end|").append("\n");
            }
        });
        return sb.toString();
    }
}
