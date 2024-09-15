package org.vaadin.marcus.simulation;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.agent.tool.Tool;
import org.vaadin.marcus.service.*;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SimulationDataService {
    private static final Logger logger = LoggerFactory.getLogger(SimulationDataService.class);
    private final ObjectMapper objectMapper;
    private final Random random = new Random();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final FlightService flightService;
    private final ClientService clientService;

    private final List<String> cities = List.of("New York", "London", "Paris", "Tokyo", "Sydney", "Dubai", "Singapore", "Hong Kong", "Frankfurt", "Amsterdam");
    private final List<String> statuses = List.of("On Time", "Delayed", "Boarding", "Departed", "Arrived");
    private final List<String> bookingStatuses = List.of("CONFIRMED", "COMPLETED", "CANCELLED", "AWAITING_CONFIRMATION", "AVAILABLE");
    private final List<String> bookingClasses = Arrays.asList("Economy", "Business", "First");
    private final List<String> firstNames = Arrays.asList("James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth");
    private final List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez");

    public SimulationDataService(ObjectMapper objectMapper, FlightService flightService, ClientService clientService) {
        this.objectMapper = objectMapper;
        this.flightService = flightService;
        this.clientService = clientService;
    }

    public String generateFlightStatus() {
        return generateFallbackFlightStatus();
    }

    private String generateFallbackFlightStatus() {
        try {
            ArrayNode flightStatuses = objectMapper.createArrayNode();
            for (int i = 0; i < 5; i++) {
                flightStatuses.add(createFlightStatus());
            }
            return objectMapper.writeValueAsString(flightStatuses);
        } catch (Exception e) {
            logger.error("Error generating fallback flight statuses", e);
            return "[]";
        }
    }

    public String generateBookings(int count) {
        try {
            ArrayNode bookings = objectMapper.createArrayNode();
            for (int i = 0; i < count; i++) {
                bookings.add(createBooking(i + 1));
            }
            return objectMapper.writeValueAsString(bookings);
        } catch (Exception e) {
            logger.error("Error generating bookings", e);
            return "[]";
        }
    }

    public String generateClientProfiles(int count) {
        try {
            ArrayNode clientProfiles = objectMapper.createArrayNode();
            for (int i = 0; i < count; i++) {
                clientProfiles.add(createClientProfile(i + 1));
            }
            return objectMapper.writeValueAsString(clientProfiles);
        } catch (Exception e) {
            logger.error("Error generating client profiles", e);
            return "[]";
        }
    }

    private ObjectNode createBooking(int index) {
        ObjectNode booking = objectMapper.createObjectNode();
        String status = bookingStatuses.get(random.nextInt(bookingStatuses.size()));
        booking.put("bookingNumber", "B" + String.format("%03d", index));
        booking.put("firstName", status.equals("AVAILABLE") ? "" : firstNames.get(random.nextInt(firstNames.size())));
        booking.put("lastName", status.equals("AVAILABLE") ? "" : lastNames.get(random.nextInt(lastNames.size())));
        booking.put("date", LocalDate.now().plusDays(random.nextInt(365)).format(dateFormatter));
        booking.put("from", cities.get(random.nextInt(cities.size())));
        booking.put("to", cities.get(random.nextInt(cities.size())));
        booking.put("bookingStatus", status);
        booking.put("bookingClass", bookingClasses.get(random.nextInt(bookingClasses.size())));
        return booking;
    }

    private ObjectNode createClientProfile(int index) {
        ObjectNode profile = objectMapper.createObjectNode();
        profile.put("id", "client" + index);
        profile.put("name", firstNames.get(random.nextInt(firstNames.size())) + " " + lastNames.get(random.nextInt(lastNames.size())));
        profile.put("contactInfo", "client" + index + "@example.com");
        profile.put("frequentFlyerNumber", "FF" + (1000 + index));
        profile.put("loyaltyStatus", calculateLoyaltyStatus(index * 10));
        profile.put("travelScore", index * 10);
        profile.put("lastTravelDate", LocalDate.now().minusDays(index).format(dateFormatter));
        
        ObjectNode preferences = objectMapper.createObjectNode();
        preferences.put("seatPreference", index % 2 == 0 ? "Window" : "Aisle");
        preferences.put("mealPreference", index % 3 == 0 ? "Vegetarian" : "Regular");
        profile.set("preferences", preferences);
        
        return profile;
    }

    private ObjectNode createFlightStatus() {
        String flightNumber = "FA" + (100 + random.nextInt(900));
        String departure = cities.get(random.nextInt(cities.size()));
        String arrival;
        do {
            arrival = cities.get(random.nextInt(cities.size()));
        } while (arrival.equals(departure));
        String status = statuses.get(random.nextInt(statuses.size()));
        LocalTime departureTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
        LocalTime arrivalTime = departureTime.plusHours(1 + random.nextInt(10));

        ObjectNode flightStatus = objectMapper.createObjectNode();
        flightStatus.put("flightNumber", flightNumber);
        flightStatus.put("departure", departure);
        flightStatus.put("arrival", arrival);
        flightStatus.put("status", status);
        flightStatus.put("departureTime", departureTime.format(timeFormatter));
        flightStatus.put("arrivalTime", arrivalTime.format(timeFormatter));
        return flightStatus;
    }

    private String calculateLoyaltyStatus(int travelScore) {
        if (travelScore >= 100) return "PLATINUM";
        if (travelScore >= 50) return "GOLD";
        if (travelScore >= 20) return "SILVER";
        return "BRONZE";
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
}