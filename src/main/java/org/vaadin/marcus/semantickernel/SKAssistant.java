package org.vaadin.marcus.semantickernel;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reactor.core.publisher.Flux;

@Service
public class SKAssistant {

    Logger log = LoggerFactory.getLogger(SKAssistant.class);

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final InvocationContext invocationContext;
    private Map<String, SKChatManager> chatsInMemory;

    public SKAssistant(ChatCompletionService chatCompletionService, Kernel kernel, InvocationContext invocationContext) {
        this.chatCompletionService = chatCompletionService;
        this.kernel = kernel;
        this.invocationContext = invocationContext;
        this.chatsInMemory = new HashMap<>();
    }

    public Flux<String> chat(String chatId, String userMessage) {
        log.debug("chatid {} and usermsg {}", chatId, userMessage);

        if(!this.chatsInMemory.containsKey(chatId)) {
            SKChatManager chatManager = new SKChatManager(chatId);
            this.chatsInMemory.put(chatId, chatManager);
        }

        SKChatManager chatManager = this.chatsInMemory.get(chatId);
        chatManager.getChatHistory().addUserMessage(userMessage);

        ChatMessageContent<?> result = chatCompletionService
                .getChatMessageContentsAsync(chatManager.getChatHistory(), kernel, invocationContext).block().get(0);
        chatManager.getChatHistory().addAssistantMessage(result.getContent());
        log.debug("replying assistant with response: {}", result.getContent());
        return Flux.just(result.getContent());
    }
}
