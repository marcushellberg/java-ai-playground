import React, { useState, useEffect } from 'react';
import { Grid, GridColumn, ProgressBar, Button } from '@vaadin/react-components';
import { BookingService } from 'Frontend/generated/endpoints';

interface FlightStatus {
  flightNumber: string;
  departure: string;
  arrival: string;
  status: string;
  departureTime: string;
  arrivalTime: string;
}

const FlightStatusWidget: React.FC = () => {
  const [flightStatuses, setFlightStatuses] = useState<FlightStatus[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchFlightStatuses = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await BookingService.generateFlightStatus();
      let parsedStatuses: FlightStatus[];
      try {
        parsedStatuses = JSON.parse(response);
        if (!Array.isArray(parsedStatuses)) {
          throw new Error('Invalid data format');
        }
        // Validate and sanitize each status object
        parsedStatuses = parsedStatuses.map(status => ({
          flightNumber: String(status.flightNumber || ''),
          departure: String(status.departure || ''),
          arrival: String(status.arrival || ''),
          status: String(status.status || ''),
          departureTime: String(status.departureTime || ''),
          arrivalTime: String(status.arrivalTime || '')
        }));
      } catch (parseError) {
        console.error('Failed to parse flight statuses:', parseError);
        setError('Invalid data received. Please try again later.');
        return;
      }
      if (parsedStatuses.length > 0) {
        setFlightStatuses(parsedStatuses);
      } else {
        setError('No flight status data available.');
      }
    } catch (error) {
      console.error('Failed to fetch flight statuses:', error);
      setError('Failed to fetch flight statuses. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchFlightStatuses();
    const interval = setInterval(fetchFlightStatuses, 30000); // Refresh every 30 seconds
    return () => clearInterval(interval);
  }, []);

  const handleReload = () => {
    fetchFlightStatuses();
  };

  if (error) {
    return <div className="text-red-500">{error}</div>;
  }

  return (
    <div className="bg-white rounded-lg shadow-md p-4 mt-6">
      <div className="flex justify-between items-center mb-3">
        <h3 className="text-xl font-semibold text-blue-600">Live Flight Status</h3>
        <Button onClick={handleReload} disabled={isLoading} theme="primary">
          {isLoading ? 'Reloading...' : 'Reload'}
        </Button>
      </div>
      {isLoading ? (
        <ProgressBar indeterminate />
      ) : (
        <Grid items={flightStatuses} className="flight-status-grid">
          <GridColumn path="flightNumber" header="Flight" autoWidth />
          <GridColumn path="departure" header="From" autoWidth />
          <GridColumn path="arrival" header="To" autoWidth />
          <GridColumn path="departureTime" header="Dep. Time" autoWidth />
          <GridColumn path="arrivalTime" header="Arr. Time" autoWidth />
          <GridColumn path="status" header="Status" autoWidth />
        </Grid>
      )}
    </div>
  );
};

export default FlightStatusWidget;