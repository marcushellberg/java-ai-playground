import { useEffect, useState } from "react";
import { nanoid } from "nanoid";

// Vaadin components
import { 
  Button, 
  Grid, 
  GridColumn, 
  MessageInput, 
  Notification, 
  SplitLayout, 
  TextField,
  ProgressBar // Use ProgressBar instead of CircularProgress
} from "@vaadin/react-components";

// Remove this line
// import { CircularProgress } from "@vaadin/react-components/CircularProgress.js";

// Local imports
import { AssistantService, BookingService } from "Frontend/generated/endpoints";
import BookingDetails from "../generated/org/vaadin/marcus/service/BookingDetails";
import Message, { MessageItem } from "../components/Message";
import MessageList from "Frontend/components/MessageList";
import CustomButton from "../components/CustomButton";

const statusIcons: { [key: string]: string } = {
  CONFIRMED: "✅",
  COMPLETED: "🏁",
  CANCELLED: "❌",
  AWAITING_CONFIRMATION: "⏳",
  AVAILABLE: "🟢"
};

export default function Index() {
  const [chatId] = useState(nanoid());
  const [working, setWorking] = useState(false);
  const [bookings, setBookings] = useState<BookingDetails[]>([]);
  const [messages, setMessages] = useState<MessageItem[]>([{
    role: 'assistant',
    content: 'Welcome to Funnair! How can I help you?'
  }]);
  const [showConfirmed, setShowConfirmed] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [errorNotification, setErrorNotification] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [visibleBookings, setVisibleBookings] = useState(10);

  const filteredBookings = bookings.filter(booking => 
    (booking.bookingNumber?.toLowerCase().includes(searchTerm.toLowerCase()) ?? false) ||
    (booking.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ?? false) ||
    (booking.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ?? false)
  );

  useEffect(() => {
    setIsLoading(true);
    setError(null);
    BookingService.getBookings()
      .then(setBookings)
      .catch(err => setError("Failed to load bookings. Please try again."))
      .finally(() => setIsLoading(false));
  }, []);

  const addMessage = (message: MessageItem) => {
    setMessages(messages => [...messages, message]);
  };

  const appendToLatestMessage = (chunk: string) => {
    setMessages(messages => {
      const latestMessage = messages[messages.length - 1];
      latestMessage.content += chunk;
      return [...messages.slice(0, -1), latestMessage];
    });
  };

  const sendMessage = async (message: string) => {
    setWorking(true);
    addMessage({ role: 'user', content: message });
    let first = true;
    AssistantService.chat(chatId, message)
      .onNext(token => {
        if (first && token) {
          addMessage({ role: 'assistant', content: token });
          first = false;
        } else {
          appendToLatestMessage(token);
        }
      })
      .onError(() => {
        setErrorNotification("Failed to send message. Please try again.");
        setWorking(false);
      })
      .onComplete(() => setWorking(false));
  };

  const renderStatus = (booking: BookingDetails) => {
    const status = booking.bookingStatus;
    return statusIcons[status as keyof typeof statusIcons] || status;
  };

  const awaitingConfirmationBookings = filteredBookings.filter(booking => booking.bookingStatus === "AWAITING_CONFIRMATION");
  const availableFlights = filteredBookings.filter(booking => booking.bookingStatus === "AVAILABLE");
  const confirmedBookings = filteredBookings.filter(booking => booking.bookingStatus === "CONFIRMED");

  const renderBookingGrid = (items: BookingDetails[], showNames: boolean = true) => (
    <>
      <Grid items={items.slice(0, visibleBookings)} className="flex-shrink-0">
        <GridColumn path="bookingNumber" header="#" autoWidth/>
        {showNames && (
          <>
            <GridColumn path="firstName" autoWidth/>
            <GridColumn path="lastName" autoWidth/>
          </>
        )}
        <GridColumn path="date" autoWidth/>
        <GridColumn path="from" autoWidth/>
        <GridColumn path="to" autoWidth/>
        <GridColumn header="Status" autoWidth>
          {({ item }) => renderStatus(item)}
        </GridColumn>
        <GridColumn path="bookingClass" autoWidth/>
      </Grid>
      {items.length > visibleBookings && (
        <Button onClick={() => setVisibleBookings(prev => prev + 10)} theme="primary" className="mt-4">
          Load More
        </Button>
      )}
    </>
  );

  if (isLoading) {
    return <div className="flex justify-center items-center h-full">Loading...</div>;
  }

  if (error) {
    return <div className="flex justify-center items-center h-full text-red-500">{error}</div>;
  }

  return (
    <>
      <SplitLayout className="h-full">
        <div className="flex flex-col gap-4 p-4 box-border h-full w-full md:w-1/4 bg-gray-100">
          <h2 className="text-2xl font-bold text-blue-600 border-b-2 border-blue-300 pb-2">Funnair Chat Support</h2>
          <MessageList messages={messages} className="flex-grow overflow-auto bg-white rounded-lg shadow-md p-4"/>
          <div className="relative">
            <MessageInput 
              onSubmit={e => sendMessage(e.detail.value)} 
              className="px-4 py-2 rounded-lg shadow-md" 
              disabled={working}
            />
            {working && (
              <div className="absolute right-2 top-1/2 transform -translate-y-1/2">
                <ProgressBar indeterminate theme="small" />
              </div>
            )}
          </div>
        </div>
        <div className="flex flex-col gap-6 p-6 box-border overflow-auto w-full md:w-3/4 bg-gray-50">
          <h2 className="text-3xl font-bold text-blue-700 border-b-2 border-blue-300 pb-2">Flight Management Dashboard</h2>
          <TextField
            placeholder="Search bookings..."
            value={searchTerm}
            onValueChanged={e => setSearchTerm(e.detail.value)}
            className="mb-4"
          />
          <section className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow duration-300">
            <h3 className="text-xl font-semibold mb-3 text-blue-600">Bookings Awaiting Confirmation</h3>
            {renderBookingGrid(awaitingConfirmationBookings)}
          </section>
          <section className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow duration-300">
            <h3 className="text-xl font-semibold mb-3 text-blue-600">Available Flight Options</h3>
            {renderBookingGrid(availableFlights, false)}
          </section>
          <section className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow duration-300">
            <CustomButton onClick={() => setShowConfirmed(!showConfirmed)} className="mb-3">
              {showConfirmed ? 'Hide' : 'Show'} Confirmed Bookings ({confirmedBookings.length})
            </CustomButton>
            {showConfirmed && (
              confirmedBookings.length > 0 
                ? renderBookingGrid(confirmedBookings)
                : <p className="text-gray-600 italic">No confirmed bookings available at this time.</p>
            )}
          </section>
        </div>
      </SplitLayout>
      {errorNotification && (
        <Notification
          theme="error"
          position="bottom-center"
          duration={5000}
          opened
          onOpenedChanged={() => setErrorNotification(null)}
        >
          {errorNotification}
        </Notification>
      )}
    </>
  );
}
