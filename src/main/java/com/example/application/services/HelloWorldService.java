package com.example.application.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

@BrowserCallable
@AnonymousAllowed
@Service
public class HelloWorldService {

    public String sayHello(String name) {
        if (name.isEmpty()) {
            return "Hello stranger";
        } else {
            return "Hello " + name;
        }
    }
}
