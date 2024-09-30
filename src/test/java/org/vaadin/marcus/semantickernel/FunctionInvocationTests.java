package org.vaadin.marcus.semantickernel;

import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.vaadin.marcus.semantickernel.search.SKContentRetriever;
import org.vaadin.marcus.service.FlightService;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class FunctionInvocationTests {
    @SpyBean
    private FlightService flightService;
    @SpyBean
    private SKContentRetriever contentRetriever;
    @SpyBean
    private SKAssistant skAssistant;

    @ParameterizedTest
    @MethodSource("functionInvocationTestParams")
    void validateFunctionInvocation(String chatId, String userMessage, int iTermsAndConditions, int iBookingDetails,
                                    int iCancelBooking, int iChangeBooking) throws ServiceNotFoundException, IOException {
        // do
        Flux<String> policy = skAssistant.chat(chatId, userMessage);
        // when
        StepVerifier.create(policy.log()).expectNextCount(1).expectComplete().verify();
        // then
        verify(contentRetriever, times(iTermsAndConditions)).searchTermsAndConditions(anyString());
        verify(flightService, times(iBookingDetails)).getBookingDetails(anyString(), anyString(), anyString());
        verify(flightService, times(iCancelBooking)).cancelBooking(anyString(), anyString(), anyString());
        verify(flightService, times(iChangeBooking)).changeBooking(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    private static Stream<Arguments> functionInvocationTestParams() {
        return Stream.of(
                // get terms and policies and get booking details test
                Arguments.of("first", "what is the cancellation policy", 1, 0, 0, 0),
                Arguments.of("first", "My name is Robert Taylor and my booking num is 105, can you get me my booking details", 0, 1, 0, 0),
                // cancellation test, but first the kernel will get booking details and terms and policies
                Arguments.of("second", "My name is Robert Taylor and my booking num is 105, can you cancel my booking", 1, 1, 0, 0),
                Arguments.of("second", "yes please proceed with cancellation", 0, 0, 1, 0),
                // update booking test, but first the kernel will get booking details and terms and policies
                Arguments.of("third", "My name is Sarah Williams and my booking num is 104, can you update my booking for 2024-05-16 from Amsterdam to Paris", 1, 1, 0, 0),
                Arguments.of("third", "yes please proceed with the update", 0, 0, 0, 1)
        );
    }

}
