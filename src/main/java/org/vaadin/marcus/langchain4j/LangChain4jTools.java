package org.vaadin.marcus.langchain4j;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;
import org.vaadin.marcus.service.BookingDetails;
import org.vaadin.marcus.service.FlightService;

import java.time.LocalDate;

@Component
public class LangChain4jTools {

    private final FlightService service;

    public LangChain4jTools(FlightService service) {
        this.service = service;
    }

    @Tool("""
            Retrieves information about an existing booking,
            such as the flight date, booking status, departure and arrival airports, and booking class.
            """)
    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        return service.getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Tool("""
            Modifies an existing booking.
            This includes making changes to the flight date, and the departure and arrival airports.
            """)
    public void changeBooking(
        String bookingNumber,
        String firstName,
        String lastName,
        LocalDate newFlightDate,
        @P("3-letter code for departure airport") String newDepartureAirport,
        @P("3-letter code for arrival airport") String newArrivalAirport
    ) {
        service.changeBooking(bookingNumber, firstName, lastName, newFlightDate, newDepartureAirport, newArrivalAirport);
    }

    @Tool
    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        service.cancelBooking(bookingNumber, firstName, lastName);
    }

}
