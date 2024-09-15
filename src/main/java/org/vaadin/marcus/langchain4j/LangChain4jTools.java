package org.vaadin.marcus.langchain4j;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;
import org.vaadin.marcus.service.*;
import org.vaadin.marcus.client.BookingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class LangChain4jTools {

    private final FlightService flightService;
    private final ClientService clientService;

    public LangChain4jTools(FlightService flightService, ClientService clientService, BookingService bookingService) {
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
        clientService.updateProfile(clientId, profileMap);
        return "Client profile updated successfully.";
    }

    @Tool("Get client profile")
    public String getClientProfile(String clientId) {
        ClientProfile profile = clientService.getClientProfile(clientId);
        return profile != null ? profile.toString() : "Client profile not found.";
    }

    @Tool("Add frequent flyer information")
    public String addFrequentFlyerInfo(String clientId, String frequentFlyerNumber) {
        clientService.addFrequentFlyerInfo(clientId, frequentFlyerNumber);
        return "Frequent flyer information added successfully.";
    }

    @Tool("Get client's past bookings")
    public String getClientPastBookings(String clientId) {
        List<String> pastBookings = clientService.getPastBookings(clientId);
        return String.join(", ", pastBookings);
    }

    @Tool("Create client profile")
    public String createClientProfile(String clientId, String name, String contactInfo, String preferences) {
        Map<String, String> preferencesMap = parsePreferences(preferences);
        ClientProfile profile = new ClientProfile(clientId, name, contactInfo, preferencesMap);
        clientService.createClientProfile(clientId, profile);
        return "Client profile created successfully.";
    }

    @Tool("Search clients")
    public String searchClients(String query) {
        List<ClientProfile> results = clientService.searchClients(query);
        return results.toString();
    }

    @Tool("Add booking to client history")
    public String addBooking(String clientId, String bookingNumber, String date, String destination, String status) {
        Booking booking = new Booking(bookingNumber, LocalDate.parse(date), destination, status);
        clientService.addBooking(clientId, booking);
        return "Booking added to client history successfully.";
    }

    @Tool("Get client booking history")
    public String getBookingHistory(String clientId) {
        List<Booking> history = clientService.getBookingHistory(clientId);
        return history.toString();
    }

    @Tool("Log client interaction")
    public String logInteraction(String clientId, String type, String content) {
        Interaction interaction = new Interaction(LocalDateTime.now(), type, content);
        clientService.logInteraction(clientId, interaction);
        return "Client interaction logged successfully.";
    }

    @Tool("Get client interactions")
    public String getClientInteractions(String clientId) {
        List<Interaction> interactions = clientService.getClientInteractions(clientId);
        return interactions.toString();
    }

    @Tool("Segment clients")
    public String segmentClients(boolean highSpender, boolean frequentTraveler) {
        SegmentationCriteria criteria = new SegmentationCriteria();
        criteria.setHighSpender(highSpender);
        criteria.setFrequentTraveler(frequentTraveler);
        List<ClientProfile> segmentedClients = clientService.segmentClients(criteria);
        return segmentedClients.toString();
    }

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

    private Map<String, String> parsePreferences(String preferences) {
        // Implement parsing logic for preferences
        return new HashMap<>();
    }
}
