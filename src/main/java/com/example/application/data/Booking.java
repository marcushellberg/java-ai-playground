package com.example.application.data;

import java.time.LocalDate;

public class Booking {

    private String bookingNumber;
    private LocalDate bookingFrom;
    private LocalDate bookingTo;
    private Customer customer;

    private BookingStatus bookingStatus;

    public Booking(String bookingNumber, LocalDate bookingFrom, LocalDate bookingTo, Customer customer, BookingStatus bookingStatus) {
        this.bookingNumber = bookingNumber;
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.customer = customer;
        this.bookingStatus = bookingStatus;
    }


    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public LocalDate getBookingFrom() {
        return bookingFrom;
    }

    public void setBookingFrom(LocalDate bookingFrom) {
        this.bookingFrom = bookingFrom;
    }

    public LocalDate getBookingTo() {
        return bookingTo;
    }

    public void setBookingTo(LocalDate bookingTo) {
        this.bookingTo = bookingTo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}