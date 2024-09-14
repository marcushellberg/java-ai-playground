package org.vaadin.marcus.client;

import org.vaadin.marcus.service.BookingDetails;
import org.vaadin.marcus.service.FlightService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.time.LocalDate; // Add this import statement

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class BookingService {
    private final FlightService flightService;

    public BookingService(FlightService carRentalService) {
        this.flightService = carRentalService;
    }

    public List<BookingDetails> getBookings() {
        return flightService.getBookings();
    }

    
}
