package org.vaadin.marcus.client;


import org.vaadin.marcus.langchain4j.LangChain4jAssistant;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.vaadin.marcus.springai.SpringAiAssistant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final LangChain4jAssistant langChain4JAssistant;
    private final SpringAiAssistant springAiAssistant;


    public AssistantService(LangChain4jAssistant langChain4JAssistant, SpringAiAssistant springAiAssistant) {
        this.langChain4JAssistant = langChain4JAssistant;
        this.springAiAssistant = springAiAssistant;
    }

    public Flux<String> chat(String chatId, String userMessage, String library) {
        if("LangChain4j".equals(library)) {
            return callLangChain4j(chatId, userMessage);
        } else if("Spring AI".equals(library)) {
            return springAiAssistant.chat(chatId, userMessage);
        } else {
            throw new IllegalArgumentException("Unknown library: " + library);
        }
    }


    private Flux<String> callLangChain4j(String chatId, String userMessage) {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        langChain4JAssistant.chat(chatId, userMessage)
                .onNext(sink::tryEmitNext)
                .onComplete(aiMessageResponse -> sink.tryEmitComplete())
                .onError(sink::tryEmitError)
                .start();

        return sink.asFlux();
    }
}
