package org.vaadin.marcus.service;

import java.util.Map;

public class ClientProfile {
    private String id;
    private String name;
    private String contactInfo;
    private String frequentFlyerNumber;
    private Map<String, String> preferences;

    public ClientProfile(String id, String name, String contactInfo, Map<String, String> preferences) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.preferences = preferences;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public String getFrequentFlyerNumber() { return frequentFlyerNumber; }
    public void setFrequentFlyerNumber(String frequentFlyerNumber) { this.frequentFlyerNumber = frequentFlyerNumber; }
    public Map<String, String> getPreferences() { return preferences; }
    public void setPreferences(Map<String, String> preferences) { this.preferences = preferences; }
}