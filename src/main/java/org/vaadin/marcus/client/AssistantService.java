package org.vaadin.marcus.client;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.vaadin.marcus.springai.SpringAiAssistant;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final SpringAiAssistant springAiAssistant;

    public AssistantService(SpringAiAssistant springAiAssistant) {
        this.springAiAssistant = springAiAssistant;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        return springAiAssistant.chat(chatId, userMessage);
    }

}
