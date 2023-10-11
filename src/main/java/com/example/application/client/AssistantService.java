package com.example.application.client;

import com.example.application.services.CarRentalService;
import com.example.application.services.CustomerSupportAgent;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDate;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final CustomerSupportAgent agent;


    public AssistantService(CustomerSupportAgent agent) {
        this.agent = agent;
    }

    public Flux<String> chat(String chatId, String question) {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        agent.chat(chatId, question)
                .onNext(sink::tryEmitNext)
                .onComplete(aiMessageResponse -> sink.tryEmitComplete())
                .onError(sink::tryEmitError)
                .start();

        return sink.asFlux();
    }
}
