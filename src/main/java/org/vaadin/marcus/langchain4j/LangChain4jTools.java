package org.vaadin.marcus.langchain4j;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;
import org.vaadin.marcus.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class LangChain4jTools {

    private final FlightService flightService;
    private final ClientService clientService;

    public LangChain4jTools(FlightService flightService, ClientService clientService) {
        this.flightService = flightService;
        this.clientService = clientService;
    }

    @Tool("Retrieves information about an existing booking")
    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        return flightService.getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Tool("Modifies an existing booking")
    public void changeBooking(String bookingNumber, String firstName, String lastName,
                              LocalDate newFlightDate, String newDepartureAirport, String newArrivalAirport) {
        flightService.changeBooking(bookingNumber, firstName, lastName, newFlightDate, newDepartureAirport, newArrivalAirport);
    }

    @Tool("Cancels an existing booking")
    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        flightService.cancelBooking(bookingNumber, firstName, lastName);
    }

    @Tool("Books a flight")
    public void bookFlight(String bookingNumber, String firstName, String lastName) {
        flightService.updateBooking(bookingNumber, firstName, lastName);
    }

    @Tool("Retrieves a list of available bookings")
    public List<BookingDetails> getAvailableBookings() {
        return flightService.getAvailableBookings();
    }

    @Tool("Confirms an existing booking")
    public void confirmBooking(String bookingNumber, String firstName, String lastName) {
        flightService.confirmBooking(bookingNumber, firstName, lastName);
    }

    @Tool("Retrieves a list of confirmed bookings")
    public List<BookingDetails> getConfirmedBookings() {
        return flightService.getConfirmedBookings();
    }

    @Tool("Update client profile")
    public String updateClientProfile(String clientId, String profileData) {
        Map<String, String> profileMap = parseProfileData(profileData);
        ClientProfile profile = clientService.getClientProfile(clientId);
        if (profile != null) {
            updateProfileFromMap(profile, profileMap);
            clientService.updateClientProfile(clientId, profile);
            return "Client profile updated successfully.";
        }
        return "Client profile not found.";
    }

    private void updateProfileFromMap(ClientProfile profile, Map<String, String> profileMap) {
        if (profileMap.containsKey("name")) profile.setName(profileMap.get("name"));
        if (profileMap.containsKey("contactInfo")) profile.setContactInfo(profileMap.get("contactInfo"));
        if (profileMap.containsKey("frequentFlyerNumber")) profile.setFrequentFlyerNumber(profileMap.get("frequentFlyerNumber"));
        // Add more fields as needed
    }

    @Tool("Add frequent flyer information")
    public String addFrequentFlyerInfo(String clientId, String frequentFlyerNumber) {
        ClientProfile profile = clientService.getClientProfile(clientId);
        if (profile != null) {
            profile.setFrequentFlyerNumber(frequentFlyerNumber);
            clientService.updateClientProfile(clientId, profile);
            return "Frequent flyer information added successfully.";
        }
        return "Client profile not found.";
    }

    @Tool("Get client's past bookings")
    public String getClientPastBookings(String clientId) {
        ClientProfile profile = clientService.getClientProfile(clientId);
        if (profile != null) {
            // Implement logic to get past bookings
            return "Past bookings retrieval not implemented yet.";
        }
        return "Client profile not found.";
    }

    @Tool("Create client profile")
    public String createClientProfile(String clientId, String name, String contactInfo, String frequentFlyerNumber, String loyaltyStatus, int travelScore, String lastTravelDate) {
        ClientProfile profile = new ClientProfile(clientId, name, contactInfo, frequentFlyerNumber, LoyaltyStatus.valueOf(loyaltyStatus), travelScore, lastTravelDate);
        clientService.createClientProfile(profile);
        return "Client profile created successfully.";
    }

    // Remove or update methods that are no longer supported by ClientService
    // For example, you might want to remove or update these methods:
    // addBooking, getBookingHistory, logInteraction, getClientInteractions, segmentClients

    private Map<String, String> parseProfileData(String profileData) {
        Map<String, String> profileMap = new HashMap<>();
        String[] pairs = profileData.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                profileMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return profileMap;
    }
}
