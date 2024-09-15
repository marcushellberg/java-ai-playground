package org.vaadin.marcus.service;

import java.util.Map;
import java.util.HashMap;

public class ClientProfile {
    private String id;
    private String name;
    private String contactInfo;
    private String frequentFlyerNumber;
    private LoyaltyStatus loyaltyStatus;
    private int travelScore;
    private String lastTravelDate;
    private Map<String, String> preferences;

    public ClientProfile(String id, String name, String contactInfo, String frequentFlyerNumber, LoyaltyStatus loyaltyStatus, int travelScore, String lastTravelDate) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.frequentFlyerNumber = frequentFlyerNumber;
        this.loyaltyStatus = loyaltyStatus;
        this.travelScore = travelScore;
        this.lastTravelDate = lastTravelDate;
        this.preferences = new HashMap<>(); // Initialize preferences
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
    public LoyaltyStatus getLoyaltyStatus() { return loyaltyStatus; }
    public void setLoyaltyStatus(LoyaltyStatus loyaltyStatus) { this.loyaltyStatus = loyaltyStatus; }
    public int getTravelScore() { return travelScore; }
    public void setTravelScore(int travelScore) { this.travelScore = travelScore; }
    public String getLastTravelDate() { return lastTravelDate; }
    public void setLastTravelDate(String lastTravelDate) { this.lastTravelDate = lastTravelDate; }
    public Map<String, String> getPreferences() { return preferences != null ? preferences : new HashMap<>(); }
    public void setPreferences(Map<String, String> preferences) { this.preferences = preferences != null ? preferences : new HashMap<>(); }
}