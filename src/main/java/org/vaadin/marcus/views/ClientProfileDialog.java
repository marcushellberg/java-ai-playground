package org.vaadin.marcus.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.vaadin.marcus.service.ClientProfile;
import org.vaadin.marcus.service.ClientService;

public class ClientProfileDialog extends Dialog {

    private final ClientService clientService;
    private final ClientProfile clientProfile;
    private final Binder<ClientProfile> binder;

    public ClientProfileDialog(ClientService clientService, ClientProfile clientProfile) {
        this.clientService = clientService;
        this.clientProfile = clientProfile;
        this.binder = new Binder<>(ClientProfile.class);

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Name");
        TextField contactInfoField = new TextField("Contact Info");
        TextField frequentFlyerField = new TextField("Frequent Flyer Number");

        formLayout.add(nameField, contactInfoField, frequentFlyerField);

        binder.bindInstanceFields(this);
        binder.readBean(clientProfile);

        Button saveButton = new Button("Save", e -> saveProfile());
        Button cancelButton = new Button("Cancel", e -> close());

        add(formLayout, saveButton, cancelButton);
    }

    private void saveProfile() {
        binder.writeBeanIfValid(clientProfile);
        clientService.updateClientProfile(clientProfile.getId(), clientProfile);
        close();
    }
}