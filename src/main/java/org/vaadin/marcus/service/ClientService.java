package org.vaadin.marcus.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private Map<String, ClientProfile> clientProfiles = new HashMap<>();
    private Map<String, List<Booking>> bookingHistory = new HashMap<>();
    private Map<String, List<Interaction>> clientInteractions = new HashMap<>();

    public void createClientProfile(String clientId, ClientProfile profile) {
        clientProfiles.put(clientId, profile);
    }

    public ClientProfile getClientProfile(String clientId) {
        return clientProfiles.get(clientId);
    }

    public void updateClientProfile(String clientId, ClientProfile profile) {
        clientProfiles.put(clientId, profile);
    }

    public void deleteClientProfile(String clientId) {
        clientProfiles.remove(clientId);
    }

    public List<ClientProfile> searchClients(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>(clientProfiles.values());
        }
        return clientProfiles.values().stream()
                .filter(profile -> profile.getName().toLowerCase().contains(query.toLowerCase())
                        || profile.getContactInfo().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void addBooking(String clientId, Booking booking) {
        bookingHistory.computeIfAbsent(clientId, k -> new ArrayList<>()).add(booking);
    }

    public List<Booking> getBookingHistory(String clientId) {
        return bookingHistory.getOrDefault(clientId, new ArrayList<>());
    }

    public void logInteraction(String clientId, Interaction interaction) {
        clientInteractions.computeIfAbsent(clientId, k -> new ArrayList<>()).add(interaction);
    }

    public List<Interaction> getClientInteractions(String clientId) {
        return clientInteractions.getOrDefault(clientId, new ArrayList<>());
    }

    public List<ClientProfile> segmentClients(SegmentationCriteria criteria) {
        // Implement segmentation logic
        return new ArrayList<>();
    }

    public void updateProfile(String clientId, Map<String, String> profileMap) {
        ClientProfile profile = clientProfiles.get(clientId);
        if (profile != null) {
            profile.setName(profileMap.getOrDefault("name", profile.getName()));
            profile.setContactInfo(profileMap.getOrDefault("contactInfo", profile.getContactInfo()));
            profile.setFrequentFlyerNumber(profileMap.getOrDefault("frequentFlyerNumber", profile.getFrequentFlyerNumber()));
        }
    }

    public void addFrequentFlyerInfo(String clientId, String frequentFlyerNumber) {
        ClientProfile profile = clientProfiles.get(clientId);
        if (profile != null) {
            profile.setFrequentFlyerNumber(frequentFlyerNumber);
        }
    }

    public List<String> getPastBookings(String clientId) {
        List<Booking> bookings = bookingHistory.get(clientId);
        if (bookings != null) {
            return bookings.stream().map(Booking::getBookingNumber).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void generateSampleData() {
        for (int i = 1; i <= 10; i++) {
            String clientId = "client" + i;
            ClientProfile profile = new ClientProfile(
                clientId,
                "Client " + i,
                String.format("client%d@example.com", i),
                Map.of("preferredSeat", (i % 2 == 0) ? "Window" : "Aisle")
            );
            profile.setFrequentFlyerNumber(String.format("FF%d", 1000 + i));
            createClientProfile(clientId, profile);
            
            // Add a sample booking
            Booking booking = new Booking("B" + (2000 + i), LocalDate.now().plusDays(i), "Destination " + i, "CONFIRMED");
            addBooking(clientId, booking);
            
            // Add a sample interaction
            Interaction interaction = new Interaction(LocalDateTime.now().minusDays(i), "Email", "Inquiry about flight " + i);
            logInteraction(clientId, interaction);
        }
    }
}