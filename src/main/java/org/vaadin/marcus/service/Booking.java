package org.vaadin.marcus.service;

import java.time.LocalDate;

public class Booking {
    private String bookingNumber;
    private LocalDate date;
    private String destination;
    private String status;

    public Booking(String bookingNumber, LocalDate date, String destination, String status) {
        this.bookingNumber = bookingNumber;
        this.date = date;
        this.destination = destination;
        this.status = status;
    }

    // Getters and setters
    public String getBookingNumber() { return bookingNumber; }
    public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}