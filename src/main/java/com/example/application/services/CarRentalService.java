package com.example.application.services;

import com.example.application.data.CarRentalData;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class CarRentalService {

    private final CarRentalData db;
    private final EmbeddedStorageManager storeManager;

    public CarRentalService() {
        db = new CarRentalData();
        storeManager = EmbeddedStorage.start();
        storeManager.setRoot(db);
    }

}
