import { useState, useEffect } from 'react';
import { Dialog } from '@vaadin/react-components/Dialog.js';
import { Button } from '@vaadin/react-components/Button.js';
import { TextField } from '@vaadin/react-components/TextField.js';
import { Grid } from '@vaadin/react-components/Grid.js';
import { GridColumn } from '@vaadin/react-components/GridColumn.js';
import { BookingService } from 'Frontend/generated/endpoints';
import { ClientProfile } from 'Frontend/generated/org/vaadin/marcus/service/ClientProfile';

interface ClientManagementModalProps {
  open: boolean;
  onClose: () => void;
}

export default function ClientManagementModal({ open, onClose }: ClientManagementModalProps) {
  const [clients, setClients] = useState<ClientProfile[]>([]);
  const [newClient, setNewClient] = useState<Partial<ClientProfile>>({});

  useEffect(() => {
    if (open) {
      fetchClients();
    }
  }, [open]);

  const fetchClients = async () => {
    // In a real application, you would fetch clients from the server
    // For now, we'll use dummy data
    const dummyClients: ClientProfile[] = [
      { id: '1', firstName: 'John', lastName: 'Doe', email: 'john@example.com' },
      { id: '2', firstName: 'Jane', lastName: 'Smith', email: 'jane@example.com' },
    ];
    setClients(dummyClients);
  };

  // Remove the handleAddClient function
  const handleAddClient = async () => {
    try {
      const addedClient = await BookingService.addNewClient(newClient as ClientProfile);
      setClients([...clients, addedClient]);
      setNewClient({});
    } catch (error) {
      console.error('Failed to add new client:', error);
    }
  };

  return (
    <Dialog opened={open} onOpenedChanged={(e) => !e.detail.value && onClose()} header="Client Management">
      <div className="p-4">
        <h3 className="text-lg font-semibold mb-4">Client List</h3>
        <Grid items={clients}>
          <GridColumn path="firstName" header="First Name" />
          <GridColumn path="lastName" header="Last Name" />
          <GridColumn path="email" header="Email" />
        </Grid>
      </div>
    </Dialog>
  );
}