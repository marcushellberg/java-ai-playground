package com.example.application.services;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface CustomerSupportAgent {
    @SystemMessage({
            "You are a customer chat support agent of a car rental company named 'Miles of Smiles'.",
            "Respond in a friendly, helpful, and joyful manner.",
            "Before providing information about a booking or cancelling a booking, ",
            "you MUST always get the following information from the user: ",
            "booking number, customer first name and last name.",
            "Before changing a booking you MUST always ensure it is permitted by the terms.",
            "Today is {{current_date}}. ",
    })
    TokenStream chat(@MemoryId String chatId, @UserMessage String userMessage);
}
