package org.vaadin.marcus.client;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.vaadin.marcus.service.ClientProfile;
import org.vaadin.marcus.client.ClientProfileService;
import org.vaadin.marcus.simulation.SimulationDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@BrowserCallable
@AnonymousAllowed
@Service
public class BookingService {
    private final ClientProfileService clientProfileService;
    private final SimulationDataService simulationDataService;
    private final ObjectMapper objectMapper;

    public BookingService(ClientProfileService clientProfileService, SimulationDataService simulationDataService, ObjectMapper objectMapper) {
        this.clientProfileService = clientProfileService;
        this.simulationDataService = simulationDataService;
        this.objectMapper = objectMapper;
    }

    public List<ClientProfile> getAllClientProfiles() {
        String clientProfilesJson = simulationDataService.generateClientProfiles(10);
        try {
            return objectMapper.readValue(clientProfilesJson, new TypeReference<List<ClientProfile>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public ClientProfile getClientProfile(String clientId) {
        return clientProfileService.getClientByEmail(clientId); // This method needs to be implemented in ClientProfileService
    }

    public void updateClientProfile(String clientId, ClientProfile profile) {
        clientProfileService.updateClient(profile);
    }

    public void deleteClientProfile(String clientId) {
        clientProfileService.deleteClient(clientId);
    }

    public List<ClientProfile> searchClients(String query) {
        return clientProfileService.getAllClients().stream()
            .filter(client -> client.getName().toLowerCase().contains(query.toLowerCase()) ||
                              client.getContactInfo().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }

    public String generateFlightStatus() {
        return simulationDataService.generateFlightStatus();
    }

    public String generateBookings() {
        return simulationDataService.generateBookings(50); // Generate 50 bookings
    }
}
