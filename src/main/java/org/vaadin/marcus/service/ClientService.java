package org.vaadin.marcus.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private Map<String, ClientProfile> clientProfiles = new HashMap<>();

    public ClientService() {
        generateSampleData();
    }

    public void createClientProfile(ClientProfile profile) {
        clientProfiles.put(profile.getId(), profile);
    }

    public ClientProfile getClientProfile(String clientId) {
        return clientProfiles.get(clientId);
    }

    public void updateClientProfile(String clientId, ClientProfile updatedProfile) {
        ClientProfile existingProfile = clientProfiles.get(clientId);
        if (existingProfile != null) {
            // Update the existing profile with the new information
            existingProfile.setName(updatedProfile.getName());
            existingProfile.setContactInfo(updatedProfile.getContactInfo());
            existingProfile.setFrequentFlyerNumber(updatedProfile.getFrequentFlyerNumber());
            existingProfile.setLoyaltyStatus(updatedProfile.getLoyaltyStatus());
            existingProfile.setTravelScore(updatedProfile.getTravelScore());
            existingProfile.setLastTravelDate(updatedProfile.getLastTravelDate());
            existingProfile.setPreferences(updatedProfile.getPreferences());
            
            // Update the map with the modified profile
            clientProfiles.put(clientId, existingProfile);
        }
    }

    public void deleteClientProfile(String clientId) {
        clientProfiles.remove(clientId);
    }

    public List<ClientProfile> getAllClientProfiles() {
        return new ArrayList<>(clientProfiles.values());
    }

    public List<ClientProfile> searchClients(String query) {
        if (query == null || query.isEmpty()) {
            return getAllClientProfiles();
        }
        return clientProfiles.values().stream()
                .filter(profile -> profile.getName().toLowerCase().contains(query.toLowerCase())
                        || profile.getContactInfo().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void generateSampleData() {
        for (int i = 1; i <= 10; i++) {
            String clientId = "client" + i;
            ClientProfile profile = new ClientProfile(
                clientId,
                "Client " + i,
                "client" + i + "@example.com",
                "FF" + (1000 + i),
                calculateLoyaltyStatus(i * 10),
                i * 10,
                LocalDate.now().minusDays(i).toString()
            );
            // Add some sample preferences
            profile.getPreferences().put("seatPreference", i % 2 == 0 ? "Window" : "Aisle");
            profile.getPreferences().put("mealPreference", i % 3 == 0 ? "Vegetarian" : "Regular");
            createClientProfile(profile);
        }
    }

    private LoyaltyStatus calculateLoyaltyStatus(int travelScore) {
        if (travelScore >= 100) return LoyaltyStatus.PLATINUM;
        if (travelScore >= 50) return LoyaltyStatus.GOLD;
        if (travelScore >= 20) return LoyaltyStatus.SILVER;
        return LoyaltyStatus.BRONZE;
    }
}