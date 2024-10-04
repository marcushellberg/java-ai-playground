package org.vaadin.marcus.client;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.vaadin.marcus.service.FlightService;
import org.vaadin.marcus.springai.SpringAiAssistant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;


@BrowserCallable
@AnonymousAllowed
@Configuration
public class AssistantService {

    public record SeatChangeRequest(String requestId) {
    }

    private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Sinks.Many<SeatChangeRequest>> seatChangeRequests = new ConcurrentHashMap<>();

    private final SpringAiAssistant springAiAssistant;
    private final FlightService flightBookingService;

    public AssistantService(SpringAiAssistant springAiAssistant, FlightService flightBookingService) {
        this.springAiAssistant = springAiAssistant;
        this.flightBookingService = flightBookingService;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        return springAiAssistant.chat(chatId, userMessage);
    }

    public Flux<SeatChangeRequest> seatChangeRequests(String chatId) {
        return seatChangeRequests.computeIfAbsent(
                chatId,
                id -> Sinks.many().unicast().onBackpressureBuffer()
        ).asFlux();
    }

    public void completeSeatChangeRequest(String requestId, String seat) {
        var future = pendingRequests.remove(requestId);
        if (future != null) {
            future.complete(seat);
        }
    }

    public record LLMSeatChangeRequest(String bookingNumber, String firstName, String lastName) {
    }

    @Bean
    @Description("Change seat")
    public Function<LLMSeatChangeRequest, String> changeSeat() {
        return request -> {
            System.out.println("Changing seat for " + request.bookingNumber() + " to a better one");

            var id = UUID.randomUUID().toString();
            CompletableFuture<String> future = new CompletableFuture<>();
            pendingRequests.put(id, future);

            // FIXME: Only send the request to the correct chatId
            // This function is called from the LLM, that does not know the internal chatId, how do we get it?
            // Advisor context maybe?
            seatChangeRequests.values().forEach(sink -> sink.tryEmitNext(new SeatChangeRequest(id)));

            // Wait for the seat selection to complete
            String seat;
            try {
                seat = future.get(); // This will block until completeSeatChangeRequest is called
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Seat selection interrupted", e);
            }

            // Proceed with changing the seat
            flightBookingService.changeSeat(
                    request.bookingNumber(),
                    request.firstName(),
                    request.lastName(),
                    seat
            );

            return "Seat changed successfully";
        };
    }
}
