package org.vaadin.marcus.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dialog.Dialog;
import org.vaadin.marcus.service.ClientProfile;
import org.vaadin.marcus.service.ClientService;

@Route(value = "client-management", layout = MainLayout.class)
public class ClientManagementView extends VerticalLayout {

    private final ClientService clientService;
    private final Grid<ClientProfile> clientGrid;
    private final TextField searchField;

    public ClientManagementView(ClientService clientService) {
        this.clientService = clientService;

        H2 title = new H2("Client Management Dashboard");
        searchField = new TextField("Search Clients");
        Button searchButton = new Button("Search", e -> searchClients());
        clientGrid = new Grid<>(ClientProfile.class);

        add(title, searchField, searchButton, clientGrid);

        configureGrid();
        updateGrid();
    }

    private void configureGrid() {
        clientGrid.addColumn(ClientProfile::getId).setHeader("ID");
        clientGrid.addColumn(ClientProfile::getName).setHeader("Name");
        clientGrid.addColumn(ClientProfile::getContactInfo).setHeader("Contact Info");
        clientGrid.addColumn(ClientProfile::getFrequentFlyerNumber).setHeader("Frequent Flyer Number");
        clientGrid.addComponentColumn(this::createActionButtons).setHeader("Actions");
    }

    private void updateGrid() {
        clientGrid.setItems(clientService.searchClients(""));
    }

    private void searchClients() {
        String query = searchField.getValue();
        clientGrid.setItems(clientService.searchClients(query));
    }

    private HorizontalLayout createActionButtons(ClientProfile client) {
        Button viewButton = new Button("View", e -> viewClient(client));
        Button editButton = new Button("Edit", e -> editClient(client));
        Button deleteButton = new Button("Delete", e -> deleteClient(client));

        return new HorizontalLayout(viewButton, editButton, deleteButton);
    }

    private void viewClient(ClientProfile client) {
        Dialog dialog = new Dialog();
        dialog.add(new H3("Client Details"));
        dialog.add(new Paragraph("ID: " + client.getId()));
        dialog.add(new Paragraph("Name: " + client.getName()));
        dialog.add(new Paragraph("Contact: " + client.getContactInfo()));
        dialog.add(new Paragraph("Frequent Flyer: " + client.getFrequentFlyerNumber()));
        dialog.add(new Paragraph("Preferences: " + client.getPreferences()));
        dialog.open();
    }

    private void editClient(ClientProfile client) {
        ClientProfileDialog dialog = new ClientProfileDialog(clientService, client);
        dialog.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                updateGrid();
            }
        });
        dialog.open();
    }

    private void deleteClient(ClientProfile client) {
        clientService.deleteClientProfile(client.getId());
        updateGrid();
    }
}