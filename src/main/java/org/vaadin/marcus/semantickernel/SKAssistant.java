package org.vaadin.marcus.semantickernel;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vaadin.marcus.service.BookingDetails;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        this.chatsInMemory = new HashMap<>();
        // Allowing LLM to find and invoke the right function(s) required to perform a task
        this.invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .build();
        // Setting the type converter globally, so when a BookingDetails object encountered,
        // this can be used to convert the object to string and vice-versa
        ContextVariableTypes.addGlobalConverter(bookingDetailsTypeConverter);
    }

    public Flux<String> chat(String chatId, String userMessage) throws ServiceNotFoundException {
        log.debug("chatid {} and usermsg {}", chatId, userMessage);

        var chatManager = getChatManager(chatId, userMessage);
        var chatCompletionService = this.kernel.getService(ChatCompletionService.class);
        var chatMessageContentsAsync = chatCompletionService
                .getChatMessageContentsAsync(chatManager.getChatHistory(), this.kernel, this.invocationContext);

        return chatMessageContentsAsync.flatMapIterable(list ->
                list.stream().map(content -> {
                    String response = content.getContent();
                    chatManager.getChatHistory().addAssistantMessage(response);
                    return response;
                }).collect(Collectors.toList()));
    }

    private SKChatManager getChatManager(String chatId, String userMessage) {
        if(!this.chatsInMemory.containsKey(chatId)) {
            SKChatManager chatManager = new SKChatManager(chatId);
            this.chatsInMemory.put(chatId, chatManager);
        }

        var chatManager = this.chatsInMemory.get(chatId);
        chatManager.getChatHistory().addUserMessage(userMessage);
        return chatManager;
    }
}
