package org.vaadin.marcus.service;

import java.time.LocalDateTime;

public class Interaction {
    private LocalDateTime timestamp;
    private String type;
    private String content;

    public Interaction(LocalDateTime timestamp, String type, String content) {
        this.timestamp = timestamp;
        this.type = type;
        this.content = content;
    }

    // Getters and setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}