package org.vaadin.marcus.client;


import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.marcus.semantickernel.SKAssistant;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    Logger log = LoggerFactory.getLogger(AssistantService.class);

    private final SKAssistant skAssistant;

    public AssistantService(SKAssistant skAssistant) {
        this.skAssistant = skAssistant;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        try {
            return this.skAssistant.chat(chatId, userMessage);
        } catch (ServiceNotFoundException e) {
            log.error("Error invoking semantic kernel", e);
            return Flux.just("Something went wrong, please try again later");
        }
    }
}
