package org.vaadin.marcus.service;

public class SegmentationCriteria {
    private boolean highSpender;
    private boolean frequentTraveler;

    // Getters and setters
    public boolean isHighSpender() { return highSpender; }
    public void setHighSpender(boolean highSpender) { this.highSpender = highSpender; }
    public boolean isFrequentTraveler() { return frequentTraveler; }
    public void setFrequentTraveler(boolean frequentTraveler) { this.frequentTraveler = frequentTraveler; }

    public boolean matches(ClientProfile profile) {
        boolean matchesHighSpender = !highSpender || profile.getTravelScore() > 100; // Example threshold
        boolean matchesFrequentTraveler = !frequentTraveler || profile.getTravelScore() > 50; // Example threshold
        return matchesHighSpender && matchesFrequentTraveler;
    }
}