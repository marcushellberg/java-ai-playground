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

    @Tool
    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        return service.getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Tool
    public void changeBooking(String bookingNumber, String firstName, String lastName, String date, String from, String to) {
        service.changeBooking(bookingNumber, firstName, lastName, date, from, to);
    }

    @Tool
    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        service.cancelBooking(bookingNumber, firstName, lastName);
    }

}
