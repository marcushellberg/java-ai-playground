package org.vaadin.marcus.service;

import org.springframework.stereotype.Service;
import org.vaadin.marcus.data.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

@Service
public class FlightService {

    private final BookingData db;
    private Set<String> usedFlightNumbers = new HashSet<>();
    private Random random = new Random();
    private List<String> airlineCodes = List.of("FA", "AA", "VA", "BA", "LH", "UA");

    public FlightService() {
        db = new BookingData();

        initDemoData();
    }

    private void initDemoData() {
        List<String> firstNames = List.of("John", "Jane", "Michael", "Sarah", "Robert");
        List<String> lastNames = List.of("Doe", "Smith", "Johnson", "Williams", "Taylor");
        List<String> airportCodes = List.of("LAX", "SFO", "JFK", "LHR", "CDG", "ARN", "HEL", "TXL", "MUC", "FRA", "MAD", "SJC");
        Random random = new Random();

        var customers = new ArrayList<Customer>();
        var bookings = new ArrayList<Booking>();

        for (int i = 0; i < 5; i++) {
            String firstName = firstNames.get(i);
            String lastName = lastNames.get(i);
            String from = airportCodes.get(random.nextInt(airportCodes.size()));
            String to = airportCodes.get(random.nextInt(airportCodes.size()));
            BookingClass bookingClass = BookingClass.values()[random.nextInt(BookingClass.values().length)];
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);

            LocalDate date = LocalDate.now().plusDays(2*i);

            Booking booking = new Booking(generateUniqueFlightNumber(), date, customer, BookingStatus.AWAITING_CONFIRMATION, from, to, bookingClass);
            customer.getBookings().add(booking);

            customers.add(customer);
            bookings.add(booking);
        }

        //generate some available bookings
        for(int i = 0; i < 30; i++){
            String from = airportCodes.get(random.nextInt(airportCodes.size()));
            String to = airportCodes.get(random.nextInt(airportCodes.size()));
            BookingClass bookingClass = BookingClass.values()[random.nextInt(BookingClass.values().length)];
            Booking booking = new Booking(generateUniqueFlightNumber(), LocalDate.now().plusDays(2*i), new Customer(), BookingStatus.AVAILABLE, from, to, bookingClass);
            bookings.add(booking);
        }

        // Reset the database on each start
        db.setCustomers(customers);
        db.setBookings(bookings);

        System.out.println("Demo data initialized");
    }

    public List<BookingDetails> getBookings() {
        return db.getBookings().stream().map(this::toBookingDetails).toList();
    }

    private Booking findBooking(String bookingNumber, String firstName, String lastName) {
        return db.getBookings().stream()
                .filter(b -> b.getBookingNumber().equalsIgnoreCase(bookingNumber))
                .filter(b -> b.getCustomer().getFirstName().equalsIgnoreCase(firstName))
                .filter(b -> b.getCustomer().getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public BookingDetails getBookingDetails(String bookingNumber, String firstName, String lastName) {
        var booking = findBooking(bookingNumber, firstName, lastName);
        return toBookingDetails(booking);
    }

    public void changeBooking(String bookingNumber, String firstName, String lastName,
                              LocalDate newFlightDate, String newDepartureAirport, String newArrivalAirport) {
        var booking = findBooking(bookingNumber, firstName, lastName);
        if(booking.getDate().isBefore(LocalDate.now().plusDays(1))){
            throw new IllegalArgumentException("Booking cannot be changed within 24 hours of the start date.");
        }
        booking.setDate(newFlightDate);
        booking.setFrom(newDepartureAirport);
        booking.setTo(newArrivalAirport);
    }

    public void cancelBooking(String bookingNumber, String firstName, String lastName) {
        var booking = findBooking(bookingNumber, firstName, lastName);
        if (booking.getDate().isBefore(LocalDate.now().plusDays(2))) {
            throw new IllegalArgumentException("Booking cannot be cancelled within 48 hours of the start date.");
        }
        booking.setBookingStatus(BookingStatus.AVAILABLE);
    }

    public void updateBooking(String bookingNumber, String firstName, String lastName){
        var booking = getBookingFromFlightNumber(bookingNumber);
        booking.setCustomer(new Customer(firstName, lastName));
        booking.setBookingStatus(BookingStatus.AWAITING_CONFIRMATION);
        db.updateBooking(booking);
    }

    public List<BookingDetails> getAvailableBookings() {
        return db.getAvailableBookings().stream().map(this::toBookingDetails).toList();
    }

    public void confirmBooking(String bookingNumber, String firstName, String lastName) {
        var booking = findBooking(bookingNumber, firstName, lastName);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        db.updateBooking(booking);
    }

    //get booking froom flight number
    public Booking getBookingFromFlightNumber(String flightNumber){
        return db.getBookings().stream()
                .filter(b -> b.getBookingNumber().equalsIgnoreCase(flightNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    private BookingDetails toBookingDetails(Booking booking){
        return new BookingDetails(
                booking.getBookingNumber(),
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName(),
                booking.getDate(),
                booking.getBookingStatus(),
                booking.getFrom(),
                booking.getTo(),
                booking.getBookingClass().toString()
        );
    }

    public String generateUniqueFlightNumber() {
        String flightNumber;
        do {
            flightNumber = generateFlightNumber();
        } while (!usedFlightNumbers.add(flightNumber));
        return flightNumber;
    }

    private String generateFlightNumber() {
        String airline = airlineCodes.get(random.nextInt(airlineCodes.size()));
        int number = random.nextInt(1000, 9999);
        return airline + number;
    }
}
