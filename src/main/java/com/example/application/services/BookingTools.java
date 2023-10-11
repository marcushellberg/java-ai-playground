package com.example.application.services;

import com.example.application.data.Booking;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class BookingTools {

    private final CarRentalService carRentalService;

    public BookingTools(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }

    @Tool
    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        return carRentalService.getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Tool
    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        carRentalService.cancelBooking(bookingNumber, firstName, lastName);
    }

}
