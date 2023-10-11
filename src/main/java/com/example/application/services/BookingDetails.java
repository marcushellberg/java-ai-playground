package com.example.application.services;

import com.example.application.data.BookingStatus;

import java.time.LocalDate;

public record BookingDetails(String bookingNumber,
                             String firstName,
                             String lastName,
                             LocalDate bookingFrom,
                             LocalDate bookingTo,
                             BookingStatus bookingStatus) {
}
