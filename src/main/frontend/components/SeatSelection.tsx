import React, {useState, useEffect} from 'react';

type SeatSelectionProps = {
  selectedSeat?: string;
  onSeatSelect?: (seatNumber: string) => void;
};

export default function SeatSelection({selectedSeat, onSeatSelect}: SeatSelectionProps) {
  // Define the seat layout
  const rows = 12;
  const seatsPerRow = 6;
  const seatLetters = ['A', 'B', 'C', 'D', 'E', 'F'];

  // Local state for selected seat
  const [currentSelectedSeat, setCurrentSelectedSeat] = useState<string | null>(selectedSeat || null);

  // Update local selected seat if prop changes
  useEffect(() => {
    setCurrentSelectedSeat(selectedSeat || null);
  }, [selectedSeat]);

  // Handler for seat selection
  const handleSeatClick = (seatId: string) => {
    setCurrentSelectedSeat(seatId);
    if (onSeatSelect) {
      onSeatSelect(seatId);
    }
  };

  // Render the seats
  const renderSeats = () => {
    const seatRows = [];
    for (let row = 1; row <= rows; row++) {
      const seatRow = [];
      for (let seatIndex = 0; seatIndex < seatsPerRow; seatIndex++) {
        const seatLetter = seatLetters[seatIndex];
        const seatId = `${row}${seatLetter}`;
        const isSelected = currentSelectedSeat === seatId;
        seatRow.push(
          <div
            key={seatId}
            onClick={() => handleSeatClick(seatId)}
            style={{
              width: 30,
              height: 30,
              margin: 2,
              backgroundColor: isSelected ? 'blue' : 'lightgray',
              color: isSelected ? 'white' : 'black',
              textAlign: 'center',
              lineHeight: '30px',
              cursor: 'pointer',
            }}
          >
            {seatLetter}
          </div>
        );
        // Insert aisle after seat C
        if (seatLetter === 'C') {
          seatRow.push(
            <div key={`aisle-${row}`} style={{width: 20}}></div>
          );
        }
      }
      seatRows.push(
        <div key={row} style={{display: 'flex', alignItems: 'center'}}>
          <div style={{width: 20}}>{row}</div>
          {seatRow}
        </div>
      );
    }
    return seatRows;
  };

  return (
    <div>
      {renderSeats()}
    </div>
  );
};