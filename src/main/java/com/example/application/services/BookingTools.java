package com.example.application.services;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class BookingTools {

    private final FlightService carRentalService;

    public BookingTools(FlightService carRentalService) {
        this.carRentalService = carRentalService;
    }

    @Tool
    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        return carRentalService.getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Tool
    public void changeBooking(String bookingNumber, String firstName, String lastName, String date, String from, String to) {
        carRentalService.changeBooking(bookingNumber, firstName, lastName, date, from, to);
    }

    @Tool
    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        carRentalService.cancelBooking(bookingNumber, firstName, lastName);
    }

}
