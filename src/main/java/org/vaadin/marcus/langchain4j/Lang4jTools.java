package org.vaadin.marcus.langchain4j;

import org.vaadin.marcus.service.BookingDetails;
import org.vaadin.marcus.service.FlightService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class Lang4jTools {

    private final FlightService service;

    public Lang4jTools(FlightService service) {
        this.service = service;
    }

    @Tool("Retrieves information about a specific booking. " +
            "This includes details such as booking number, first and last name, flight date, " +
            "booking status, departure and arrival airports and booking class.")
    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        return service.getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Tool("Allows to modify an existing booking. " +
            "This includes making changes such as altering flight dates, departure and arrival airport.")
    public void changeBooking(String bookingNumber, String firstName, String lastName, String date, String from, String to) {
        service.changeBooking(bookingNumber, firstName, lastName, date, from, to);
    }

    @Tool("This includes removing the reservation from the system and handling any associated refund " +
            "or cancellation policies as per the airline’s rules.")
    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        service.cancelBooking(bookingNumber, firstName, lastName);
    }

}
