import { useState, useEffect } from 'react';
import { Dialog } from '@vaadin/react-components/Dialog.js';
import { Grid } from '@vaadin/react-components/Grid.js';
import { GridColumn } from '@vaadin/react-components/GridColumn.js';
import { ComboBox } from '@vaadin/react-components/ComboBox.js';
import { BookingService } from 'Frontend/generated/endpoints';
import { ClientProfile } from 'Frontend/generated/org/vaadin/marcus/service/ClientProfile';

interface ClientManagementModalProps {
  open: boolean;
  onClose: () => void;
}

export default function ClientManagementModal({ open, onClose }: ClientManagementModalProps) {
  const [clients, setClients] = useState<ClientProfile[]>([]);
  const [statusFilter, setStatusFilter] = useState<string | null>(null);

  useEffect(() => {
    if (open) {
      fetchClients();
    }
  }, [open]);

  const fetchClients = async () => {
    try {
      const fetchedClients = await BookingService.getAllClients();
      setClients(fetchedClients);
    } catch (error) {
      console.error('Failed to fetch clients:', error);
    }
  };

  const filteredClients = statusFilter
    ? clients.filter(client => client.status === statusFilter)
    : clients;

  const statusOptions = Array.from(new Set(clients.map(client => client.status))).filter(Boolean);

  return (
    <Dialog 
      opened={open} 
      onOpenedChanged={(e) => !e.detail.value && onClose()} 
      header="Client Management"
      draggable
      resizable
      modeless
    >
      <div className="p-4" style={{ minWidth: '300px', minHeight: '200px' }}>
        <h3 className="text-lg font-semibold mb-4">Client List</h3>
        <ComboBox
          label="Filter by Status"
          items={statusOptions}
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          clearButtonVisible
          className="mb-4"
        />
        <Grid items={filteredClients}>
          {clients.length > 0 && clients[0].firstName !== undefined && (
            <GridColumn path="firstName" header="First Name" />
          )}
          {clients.length > 0 && clients[0].lastName !== undefined && (
            <GridColumn path="lastName" header="Last Name" />
          )}
          <GridColumn path="email" header="Email" />
          <GridColumn path="status" header="Status" />
        </Grid>
      </div>
    </Dialog>
  );
}