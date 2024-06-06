/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.marcus.springai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.NestedExceptionUtils;
import org.vaadin.marcus.service.BookingDetails;
import org.vaadin.marcus.service.FlightService;

import java.util.function.Function;

/**
 * @author Christian Tzolov
 */
@Configuration
public class SpringAiTools {

    private static final Logger logger = LoggerFactory.getLogger(SpringAiTools.class);

    public record BookingDetailsRequest(
            String bookingNumber,
            String firstName,
            String lastName) {
    }

    public record ChangeBookingDatesRequest(
            String bookingNumber,
            String firstName,
            String lastName,
            String date,
            String from,
            String to) {
    }

    public record CancelBookingRequest(
            String bookingNumber,
            String firstName,
            String lastName) {
    }

    private final FlightService flightBookingService;

    public SpringAiTools(FlightService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }

    @Bean
    @Description("Get booking details")
    public Function<BookingDetailsRequest, BookingDetails> getBookingDetails() {
        return request -> {
            try {
                return flightBookingService.getBookingDetails(request.bookingNumber(), request.firstName(),
                        request.lastName());
            }
            catch (Exception e) {
                logger.warn("Booking details: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                return new BookingDetails(request.bookingNumber(), request.firstName(), request.lastName,
                        null, null, null, null, null);
            }
        };
    }

    @Bean
    @Description("Change booking dates")
    public Function<ChangeBookingDatesRequest, String> changeBooking() {
        return request -> {
            flightBookingService.changeBooking(request.bookingNumber(), request.firstName(), request.lastName(),
                    request.date(), request.from(), request.to());
            return "DONE";
        };
    }

    @Bean
    @Description("Cancel booking")
    public Function<CancelBookingRequest, String> cancelBooking() {
        return request -> {
            flightBookingService.cancelBooking(request.bookingNumber(), request.firstName(), request.lastName());
            return "DONE";
        };
    }
}
