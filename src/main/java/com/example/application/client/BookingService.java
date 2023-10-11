package com.example.application.client;

import com.example.application.data.BookingStatus;
import com.example.application.services.CarRentalService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;

import java.time.LocalDate;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class BookingService {
    private final CarRentalService carRentalService;

    public BookingService(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }

    public record BookingInfo(String bookingNumber,
                              String firstName,
                              String lastName,
                              LocalDate bookingFrom,
                              LocalDate bookingTo,
                              BookingStatus bookingStatus) {}

    public List<BookingInfo> getBookings() {
        return carRentalService.getBookings().stream().map(booking -> new BookingInfo(
                booking.getBookingNumber(),
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName(),
                booking.getBookingFrom(),
                booking.getBookingTo(),
                booking.getBookingStatus()
        )).toList();
    }
}
