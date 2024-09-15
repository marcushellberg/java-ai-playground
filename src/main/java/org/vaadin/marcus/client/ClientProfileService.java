package org.vaadin.marcus.client;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.stereotype.Service;
import org.vaadin.marcus.service.ClientProfile;
import org.vaadin.marcus.service.ClientService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@BrowserCallable
@AnonymousAllowed
@Service
public class ClientProfileService {
    private final Map<String, ClientProfile> clients = new ConcurrentHashMap<>();

    public ClientProfileService(ClientService clientService) {
        // Assuming clientService is not needed for this implementation
    }

    public List<ClientProfile> getAllClients() {
        return List.copyOf(clients.values());
    }

    public ClientProfile getClientByEmail(String email) {
        return clients.get(email);
    }

    public void updateClient(ClientProfile profile) {
        clients.put(profile.getEmail(), profile);
    }

    public ClientProfile addClient(ClientProfile profile) {
        clients.put(profile.getEmail(), profile);
        return profile;
    }

    public void deleteClient(String email) {
        clients.remove(email);
    }
}