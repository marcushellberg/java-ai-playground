package org.vaadin.marcus.client;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.vaadin.marcus.semantickernel.SKAssistant;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final SKAssistant skAssistant;

    public AssistantService(SKAssistant skAssistant) {
        this.skAssistant = skAssistant;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        return this.skAssistant.chat(chatId, userMessage);
    }
}
