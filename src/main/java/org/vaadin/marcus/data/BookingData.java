package org.vaadin.marcus.data;

import java.util.ArrayList;
import java.util.List;

public class BookingData {

    private List<Customer> customers = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();


    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    //get available bookings ones with no first and last name
    public List<Booking> getAvailableBookings() {
        return bookings.stream().filter(b -> b.getCustomer().getFirstName() == null && b.getCustomer().getLastName() == null).toList();
    }

    //update booking in db
    public void updateBooking(Booking booking) {
        bookings.set(bookings.indexOf(booking), booking);
    }

   
}
