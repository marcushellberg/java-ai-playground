package org.vaadin.marcus.semantickernel;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.vaadin.marcus.service.BookingDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SKAssistant {

    Logger log = LoggerFactory.getLogger(SKAssistant.class);

    private final Kernel kernel;
    private final ContextVariableTypeConverter<BookingDetails> bookingDetailsTypeConverter;

    private InvocationContext invocationContext;
    private Map<String, SKChatManager> chatsInMemory;

    public SKAssistant(Kernel kernel, ContextVariableTypeConverter<BookingDetails> bookingDetailsTypeConverter) {
        this.kernel = kernel;
        this.bookingDetailsTypeConverter = bookingDetailsTypeConverter;
    }

    @PostConstruct
    public void updateGlobalTypeConverter() {
        this.chatsInMemory = new HashMap<>();
        this.invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();
        ContextVariableTypes.addGlobalConverter(bookingDetailsTypeConverter);
    }

    public Flux<String> chat(String chatId, String userMessage) throws ServiceNotFoundException {
        log.debug("chatid {} and usermsg {}", chatId, userMessage);

        if(!this.chatsInMemory.containsKey(chatId)) {
            SKChatManager chatManager = new SKChatManager(chatId);
            this.chatsInMemory.put(chatId, chatManager);
        }

        SKChatManager chatManager = this.chatsInMemory.get(chatId);
        chatManager.getChatHistory().addUserMessage(userMessage);

        ChatCompletionService chatCompletionService = this.kernel.getService(ChatCompletionService.class);

        Mono<List<ChatMessageContent<?>>> chatMessageContentsAsync = chatCompletionService
                .getChatMessageContentsAsync(chatManager.getChatHistory(), this.kernel, this.invocationContext);
        return chatMessageContentsAsync.flatMapIterable(list ->
                list.stream().map(content -> {
                    String response = content.getContent();
                    chatManager.getChatHistory().addAssistantMessage(response);
                    return response;
                }).collect(Collectors.toList()));
    }
}
