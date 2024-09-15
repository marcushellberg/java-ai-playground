import { useState, useEffect } from 'react';
import { Dialog } from '@vaadin/react-components/Dialog.js';
import { Grid } from '@vaadin/react-components/Grid.js';
import { GridColumn } from '@vaadin/react-components/GridColumn.js';
import { ComboBox } from '@vaadin/react-components/ComboBox.js';
import { Button } from '@vaadin/react-components/Button.js';
import { TextField } from '@vaadin/react-components/TextField.js';
import { ProgressBar } from '@vaadin/react-components/ProgressBar.js';
import { BookingService } from 'Frontend/generated/endpoints';
import ClientProfile from 'Frontend/generated/org/vaadin/marcus/service/ClientProfile';
import LoyaltyStatus from 'Frontend/generated/org/vaadin/marcus/service/LoyaltyStatus';

interface ClientManagementModalProps {
  open: boolean;
  onClose: () => void;
}

export default function ClientManagementModal({ open, onClose }: ClientManagementModalProps) {
  const [clients, setClients] = useState<ClientProfile[]>([]);
  const [loyaltyStatusFilter, setLoyaltyStatusFilter] = useState<LoyaltyStatus | undefined>(undefined);
  const [searchTerm, setSearchTerm] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isMaximized, setIsMaximized] = useState(false);

  useEffect(() => {
    if (open) {
      fetchClients();
    }
  }, [open]);

  const fetchClients = async () => {
    setIsLoading(true);
    try {
      const fetchedClients = await BookingService.getAllClientProfiles();
      setClients(fetchedClients);
    } catch (error) {
      console.error('Failed to fetch clients:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSearch = async () => {
    setIsLoading(true);
    try {
      const searchResults = await BookingService.searchClients(searchTerm);
      setClients(searchResults);
    } catch (error) {
      console.error('Failed to search clients:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const filteredClients = clients
    .filter(client => !loyaltyStatusFilter || client.loyaltyStatus === loyaltyStatusFilter);

  const loyaltyStatusOptions = Object.values(LoyaltyStatus);

  const handleClose = () => onClose();
  const handleMinimize = () => {/* Implement minimize functionality */};
  const handleMaximize = () => setIsMaximized(!isMaximized);

  return (
    <Dialog 
      opened={open} 
      onOpenedChanged={(e) => !e.detail.value && onClose()} 
      headerTitle="Client Management"
      draggable
      resizable
      modeless
      className={`windows-style ${isMaximized ? 'maximized' : ''}`}
    >
      <div slot="header" className="windows-title-bar">
        <div className="title">Client Management</div>
        <div className="window-controls">
          <Button theme="tertiary icon" aria-label="Minimize" onClick={handleMinimize}>_</Button>
          <Button theme="tertiary icon" aria-label="Maximize" onClick={handleMaximize}>□</Button>
          <Button theme="tertiary icon" aria-label="Close" onClick={handleClose}>×</Button>
        </div>
      </div>
      <div className="p-4 windows-content" style={{ minWidth: '600px', minHeight: '400px' }}>
        {isLoading ? (
          <ProgressBar indeterminate />
        ) : (
          <>
            <div className="mb-4 flex justify-between items-end">
              <TextField
                placeholder="Search clients..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                className="mr-2 flex-grow"
              />
              <Button onClick={handleSearch} theme="primary">Search</Button>
              <ComboBox
                label="Filter by Loyalty Status"
                items={loyaltyStatusOptions}
                value={loyaltyStatusFilter}
                onChange={(e) => setLoyaltyStatusFilter(e.target.value as LoyaltyStatus)}
                clearButtonVisible
                className="ml-2"
              />
            </div>
            <Grid items={filteredClients} className="client-grid">
              <GridColumn path="name" header="Name" />
              <GridColumn path="contactInfo" header="Contact Info" />
              <GridColumn path="frequentFlyerNumber" header="Frequent Flyer Number" />
              <GridColumn path="loyaltyStatus" header="Loyalty Status" />
              <GridColumn path="travelScore" header="Travel Score" />
              <GridColumn path="lastTravelDate" header="Last Travel" />
            </Grid>
          </>
        )}
      </div>
    </Dialog>
  );
}