package org.vaadin.marcus.semantickernel;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

public class SKChatManager {

    private String chatId;
    private final ChatHistory chatHistory;

    public SKChatManager(String chatId) {
        this.chatId = chatId;
        this.chatHistory = new ChatHistory("""
           You are a customer chat support agent of an airline named "Funnair",
           Respond in a friendly, helpful, and joyful manner.
           Before providing information about a booking or cancelling a booking,
           you MUST always get the following information from the user:
           booking number, customer first name and last name.
           Before changing a booking you MUST ensure it is permitted by the terms.
           If there is a charge for the change, you MUST ask the user to consent before proceeding.
           Today is {{current_date}}.
           """);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ChatHistory getChatHistory() {
        return chatHistory;
    }

}
