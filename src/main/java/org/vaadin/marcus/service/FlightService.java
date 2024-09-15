package org.vaadin.marcus.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private List<BookingDetails> bookings = new ArrayList<>();

    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        return bookings.stream()
                .filter(b -> b.getBookingNumber().equals(bookingNumber)
                        && b.getFirstName().equals(firstName)
                        && b.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    public void changeBooking(String bookingNumber, String firstName, String lastName,
                              LocalDate newFlightDate, String newDepartureAirport, String newArrivalAirport) {
        BookingDetails booking = getBookingDetails(bookingNumber, firstName, lastName);
        if (booking != null) {
            booking.setDate(newFlightDate.toString());
            booking.setFrom(newDepartureAirport);
            booking.setTo(newArrivalAirport);
        }
    }

    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        BookingDetails booking = getBookingDetails(bookingNumber, firstName, lastName);
        if (booking != null) {
            booking.setBookingStatus("CANCELLED");
        }
    }

    public void updateBooking(String bookingNumber, String firstName, String lastName) {
        BookingDetails booking = getBookingDetails(bookingNumber, firstName, lastName);
        if (booking != null) {
            booking.setFirstName(firstName);
            booking.setLastName(lastName);
            booking.setBookingStatus("CONFIRMED");
        }
    }

    public List<BookingDetails> getAvailableBookings() {
        return bookings.stream()
                .filter(b -> b.getBookingStatus().equals("AVAILABLE"))
                .collect(Collectors.toList());
    }

    public void confirmBooking(String bookingNumber, String firstName, String lastName) {
        BookingDetails booking = getBookingDetails(bookingNumber, firstName, lastName);
        if (booking != null) {
            booking.setBookingStatus("CONFIRMED");
        }
    }

    public List<BookingDetails> getConfirmedBookings() {
        return bookings.stream()
                .filter(b -> b.getBookingStatus().equals("CONFIRMED"))
                .collect(Collectors.toList());
    }

    public List<BookingDetails> getBookings() {
        return new ArrayList<>(bookings);
    }

    // Method to add a booking (for testing purposes)
    public void addBooking(BookingDetails booking) {
        bookings.add(booking);
    }
}
