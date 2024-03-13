package com.example.application.langchain4j;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface CustomerSupportAgent {

    @SystemMessage("""
           You are a customer chat support agent of an airline named "Funnair".",
           Respond in a friendly, helpful, and joyful manner.
           Before providing information about a booking or cancelling a booking,
           you MUST always get the following information from the user:
           booking number, customer first name and last name.
           Before changing a booking you MUST ensure it is permitted by the terms.
           If there is a charge for the change, you MUST ask the user to consent before proceeding.
           Today is {{current_date}}.
           """)
    TokenStream chat(@MemoryId String chatId, @UserMessage String userMessage);
}
