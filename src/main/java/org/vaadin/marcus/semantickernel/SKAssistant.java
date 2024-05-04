package org.vaadin.marcus.semantickernel;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.marcus.service.BookingDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SKAssistant {

    Logger log = LoggerFactory.getLogger(SKAssistant.class);

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final InvocationContext invocationContext;
    private final ContextVariableTypeConverter<BookingDetails> bookingDetailsTypeConverter;
    private Map<String, SKChatManager> chatsInMemory;

    public SKAssistant(ChatCompletionService chatCompletionService, Kernel kernel,
                       InvocationContext invocationContext, ContextVariableTypeConverter<BookingDetails> bookingDetailsTypeConverter) {
        this.chatCompletionService = chatCompletionService;
        this.kernel = kernel;
        this.invocationContext = invocationContext;
        this.bookingDetailsTypeConverter = bookingDetailsTypeConverter;
    }

    @PostConstruct
    public void updateGlobalTypeConverter() {
        this.chatsInMemory = new HashMap<>();
        ContextVariableTypes.addGlobalConverter(bookingDetailsTypeConverter);
    }

    public Flux<String> chat(String chatId, String userMessage) {
        log.debug("chatid {} and usermsg {}", chatId, userMessage);

        if(!this.chatsInMemory.containsKey(chatId)) {
            SKChatManager chatManager = new SKChatManager(chatId);
            this.chatsInMemory.put(chatId, chatManager);
        }

        SKChatManager chatManager = this.chatsInMemory.get(chatId);
        chatManager.getChatHistory().addUserMessage(userMessage);

        Mono<List<ChatMessageContent<?>>> chatMessageContentsAsync = chatCompletionService
                .getChatMessageContentsAsync(chatManager.getChatHistory(), kernel, invocationContext);
        return chatMessageContentsAsync.flatMapIterable(list -> {
            List<String> messages = new ArrayList<>();
            list.forEach(e -> messages.add(e.getContent()));
            return messages;
        });
    }
}
