package com.example.application.services;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface CustomerSupportAgent {
    @SystemMessage({
            "You are a customer support agent of a car rental company named 'Miles of Smiles'.",
            "Before providing information about a booking or cancelling a booking, ",
            "you MUST always get the following information from the user: ",
            "booking number, customer first name and last name.",
            "Today is {{current_date}}. ",
            "Before changing a booking, ensure it is permitted by the terms.",
    })
    TokenStream chat(@MemoryId String chatId, @UserMessage String userMessage);
}
