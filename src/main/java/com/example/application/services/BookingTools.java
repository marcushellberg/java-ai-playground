package com.example.application.services;

import org.springframework.stereotype.Component;

@Component
public class BookingTools {

    private final CarRentalService carRentalService;

    public BookingTools(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }


}
