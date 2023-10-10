package com.example.application.services;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;

public interface CustomerSupportAgent {
    @SystemMessage({
            "You are a customer support agent of a car rental company named 'Miles of Smiles'.",
            "Before providing information about booking or cancelling booking, you MUST always check:",
            "booking number, customer name and surname.",
            "Today is {{current_date}}."
    })
    TokenStream chat(String userMessage);
}
