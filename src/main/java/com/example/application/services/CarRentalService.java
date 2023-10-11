package com.example.application.services;

import com.example.application.client.AssistantService;
import com.example.application.data.Booking;
import com.example.application.data.BookingStatus;
import com.example.application.data.CarRentalData;
import com.example.application.data.Customer;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CarRentalService {

    private final CarRentalData db;
    private final EmbeddedStorageManager storeManager;

    public CarRentalService() {
        db = new CarRentalData();
        storeManager = EmbeddedStorage.start();
        storeManager.setRoot(db);

        initDemoData();
    }

    private void initDemoData() {
        if (!db.getCustomers().isEmpty()) {
            return;
        }

        List<String> firstNames = List.of("John", "Jane", "Michael", "Sarah", "Robert");
        List<String> lastNames = List.of("Doe", "Smith", "Johnson", "Williams", "Taylor");
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            String firstName = firstNames.get(i);
            String lastName = lastNames.get(i);
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);

            LocalDate bookingFrom = LocalDate.now().plusDays(random.nextInt(60) + 1);  // booking starts 1 to 60 days from now
            LocalDate bookingTo = bookingFrom.plusDays(random.nextInt(7) + 1);  // booking lasts 1 to 7 days

            Booking booking = new Booking("BK-" + (i + 1), bookingFrom, bookingTo, customer, BookingStatus.CONFIRMED);
            customer.getBookings().add(booking);  // Assuming there's a getBookings method on Customer

            db.getCustomers().add(customer);
            db.getBookings().add(booking);
        }

        storeManager.storeRoot();
        System.out.println("Demo data initialized");
    }

    public List<Booking> getBookings() {
        return db.getBookings();
    }
}
