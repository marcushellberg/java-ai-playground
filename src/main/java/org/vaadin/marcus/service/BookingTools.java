package org.vaadin.marcus.service;

import dev.langchain4j.agent.tool.Tool;

import java.time.LocalDate;

public class BookingTools {
    private final FlightService flightService;

    public BookingTools(FlightService flightService) {
        this.flightService = flightService;
    }

    @Tool
    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName){
        return flightService.getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Tool
    public void changeBooking(String bookingNumber, String firstName, String lastName, LocalDate newDate, String newFrom, String newTo){
        flightService.changeBooking(bookingNumber, firstName, lastName, newDate, newFrom, newTo);
    }

    @Tool
    public void cancelBooking(String bookingNumber, String firstName, String lastName){
        flightService.cancelBooking(bookingNumber, firstName, lastName);
    }
}
