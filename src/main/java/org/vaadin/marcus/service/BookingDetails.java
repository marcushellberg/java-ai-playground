package org.vaadin.marcus.service;

import org.vaadin.marcus.data.BookingStatus;

public class BookingDetails {
    private String bookingNumber;
    private String firstName;
    private String lastName;
    private String date;
    private String from;
    private String to;
    private String bookingStatus;
    private String bookingClass;

    // Constructor
    public BookingDetails(String bookingNumber, String firstName, String lastName, String date, String from, String to, String bookingStatus, String bookingClass) {
        this.bookingNumber = bookingNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.from = from;
        this.to = to;
        this.bookingStatus = bookingStatus;
        this.bookingClass = bookingClass;
    }

    // Getters and setters
    public String getBookingNumber() { return bookingNumber; }
    public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getBookingClass() { return bookingClass; }
    public void setBookingClass(String bookingClass) { this.bookingClass = bookingClass; }
}
