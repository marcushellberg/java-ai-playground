package com.example.application.services;

import com.example.application.data.Booking;
import com.example.application.data.BookingStatus;
import com.example.application.data.CarRentalData;
import com.example.application.data.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CarRentalService {

    private final CarRentalData db;

    public CarRentalService() {
        db = new CarRentalData();

        initDemoData();
    }

    private void initDemoData() {
        List<String> firstNames = List.of("John", "Jane", "Michael", "Sarah", "Robert");
        List<String> lastNames = List.of("Doe", "Smith", "Johnson", "Williams", "Taylor");
        Random random = new Random();

        var customers = new ArrayList<Customer>();
        var bookings = new ArrayList<Booking>();

        for (int i = 0; i < 5; i++) {
            String firstName = firstNames.get(i);
            String lastName = lastNames.get(i);
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);

            LocalDate bookingFrom = LocalDate.now().plusDays(2*i);
            LocalDate bookingTo = bookingFrom.plusDays(random.nextInt(7) + 1);

            Booking booking = new Booking("10" + (i + 1), bookingFrom, bookingTo, customer, BookingStatus.CONFIRMED);
            customer.getBookings().add(booking);

            customers.add(customer);
            bookings.add(booking);
        }

        // Reset the database on each start
        db.setCustomers(customers);
        db.setBookings(bookings);

        System.out.println("Demo data initialized");
    }

    public List<BookingDetails> getBookings() {
        return db.getBookings().stream().map(this::toBookingDetails).toList();
    }

    private Booking findBooking(String bookingNumber, String firstName, String lastName) {
        return db.getBookings().stream()
                .filter(b -> b.getBookingNumber().equalsIgnoreCase(bookingNumber))
                .filter(b -> b.getCustomer().getFirstName().equalsIgnoreCase(firstName))
                .filter(b -> b.getCustomer().getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        var booking = findBooking(bookingNumber, firstName, lastName);
        return toBookingDetails(booking);
    }

    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        var booking = findBooking(bookingNumber, firstName, lastName);
        if (booking.getBookingFrom().isBefore(LocalDate.now().plusDays(7))) {
            throw new IllegalArgumentException("Booking cannot be cancelled within 7 days of the start date");
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
    }

    private BookingDetails toBookingDetails(Booking booking){
        return new BookingDetails(
                booking.getBookingNumber(),
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName(),
                booking.getBookingFrom(),
                booking.getBookingTo(),
                booking.getBookingStatus()
        );
    }
}
