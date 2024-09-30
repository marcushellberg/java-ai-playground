package org.vaadin.marcus.semantickernel;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.vaadin.marcus.semantickernel.search.SKContentRetriever;
import org.vaadin.marcus.service.BookingDetails;
import org.vaadin.marcus.service.FlightService;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SKFunctions {

    Logger log = LoggerFactory.getLogger(SKFunctions.class);

    private final FlightService service;
    private final SKContentRetriever contentRetriever;

    public SKFunctions(FlightService service, SKContentRetriever contentRetriever) {
        this.service = service;
        this.contentRetriever = contentRetriever;
    }

    @DefineKernelFunction(
            name = "SearchFromQuestion",
            description = "find information related flight change, update, cancellation policies",
            returnType = "string")
    public Mono<List<String>> searchInAnIndex(
            @KernelFunctionParameter(
                    description = "the query to answer",
                    name = "query")
            String query) {
        log.debug("invoked search for policies for query {}", query);
        return contentRetriever.searchTermsAndConditions(query);
    }

    @DefineKernelFunction(
            name = "getBookingDetails",
            description = "find booking details based on bookingNumber, firstName and lastName",
            returnType = "org.vaadin.marcus.service.BookingDetails")
    public BookingDetails getBookingDetails(
            @KernelFunctionParameter(
                    description = "booking number of the flight",
                    name = "bookingNumber")
            String bookingNumber,
            @KernelFunctionParameter(
                    description = "first name of the passenger",
                    name = "firstName")
            String firstName,
            @KernelFunctionParameter(
                    description = "last name of the passenger",
                    name = "lastName")
            String lastName) {
        log.debug("invoked getbooking details for {}, {}, {}", bookingNumber, firstName, lastName);
        try {
            return service.getBookingDetails(bookingNumber, firstName, lastName);
        } catch (IllegalArgumentException e) {
            log.error("error in getting booking detials", e);
            return new BookingDetails("", "", "", null, null, "", "", "");
        }
    }

    @DefineKernelFunction(
            name = "changeBooking",
            description = "update or change booking details based on bookingNumber, firstName and lastName, date, from and to",
            returnType = "string")
    public String changeBooking(
          @KernelFunctionParameter(
            description = "booking number of the flight",
            name = "bookingNumber")
              String bookingNumber,
          @KernelFunctionParameter(
                  description = "first name of the passenger",
                  name = "firstName")
              String firstName,
          @KernelFunctionParameter(
                  description = "last name of the passenger",
                  name = "lastName")
              String lastName,
          @KernelFunctionParameter(
                description = "date on which the flight is scheduled",
                name = "date")
            String date,
          @KernelFunctionParameter(
            description = "from which city or airport the flight is taking of",
            name = "from")
            String from,
          @KernelFunctionParameter(
            description = "to which city or airport the flight is going to",
            name = "to")
            String to) {
        log.debug("invoked change booking details for {}, {}, {}, {}, {}", bookingNumber, firstName, lastName, from, to);

        try {
            service.changeBooking(bookingNumber, firstName, lastName, date, from, to);
        } catch (Exception e) {
            log.error("Exception in update booking", e);
            return "Problem updating the flight :" + e.getLocalizedMessage();
        }

        return "Flight updated successfully";
    }

    @DefineKernelFunction(
            name = "cancelBooking",
            description = "cancel booking details based on bookingNumber, firstName and lastName",
            returnType = "string")
    public String cancelBooking(
            @KernelFunctionParameter(
                    description = "booking number of the flight",
                    name = "bookingNumber")
            String bookingNumber,
            @KernelFunctionParameter(
                    description = "first name of the passenger",
                    name = "firstName")
            String firstName,
            @KernelFunctionParameter(
                    description = "last name of the passenger",
                    name = "lastName")
            String lastName) {
        log.debug("invoked cancel booking details for {}, {}, {}", bookingNumber, firstName, lastName);
        try {
            service.cancelBooking(bookingNumber, firstName, lastName);
        } catch (Exception e) {
            log.error("Exception in cancel booking", e);
            return "Problem cancelling the flight :" + e.getLocalizedMessage();
        }

        return "Flight cancelled successfully";
    }

}
