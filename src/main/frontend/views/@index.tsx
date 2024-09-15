import { useEffect, useState } from "react";
import { nanoid } from "nanoid";
import { Tooltip } from "@vaadin/react-components/Tooltip.js";

// Vaadin components
import { 
  Button, 
  Grid, 
  GridColumn, 
  MessageInput, 
  Notification, 
  SplitLayout, 
  TextField,
  ProgressBar,
  ComboBox
} from "@vaadin/react-components";

// Local imports
import { AssistantService, BookingService } from "../generated/endpoints";
import Message, { MessageItem } from "../components/Message";
import MessageList from "Frontend/components/MessageList";
import CustomButton from "../components/CustomButton";
import ClientManagementModal from "../components/ClientManagementModal";
import FlightStatusWidget from '../components/FlightStatusWidget';

const statusIcons: { [key: string]: string } = {
  CONFIRMED: "✅",
  COMPLETED: "🏁",
  CANCELLED: "❌",
  AWAITING_CONFIRMATION: "⏳",
  AVAILABLE: "🟢"
};

const firstNames = ["James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth"];
const lastNames = ["Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"];

export default function Index() {
  const [chatId] = useState(nanoid());
  const [working, setWorking] = useState(false);
  const [bookings, setBookings] = useState<any[]>([]);
  const [messages, setMessages] = useState<MessageItem[]>([{
    role: 'assistant',
    content: 'Welcome to Funnair! How can I help you?'
  }]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [errorNotification, setErrorNotification] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string | undefined>(undefined);
  const [visibleBookings, setVisibleBookings] = useState(10);
  const [clientManagementOpen, setClientManagementOpen] = useState(false);

  const filteredBookings = bookings.filter(booking => 
    (booking.bookingNumber?.toLowerCase().includes(searchTerm.toLowerCase()) ?? false) ||
    (booking.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ?? false) ||
    (booking.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ?? false)
  ).filter(booking => !statusFilter || booking.bookingStatus === statusFilter);

  useEffect(() => {
    setIsLoading(true);
    setError(null);
    // Generate mock data with 50 records
    const mockBookings: any[] = Array.from({ length: 50 }, (_, i) => {
      const statuses = ["CONFIRMED", "COMPLETED", "CANCELLED", "AWAITING_CONFIRMATION", "AVAILABLE"];
      const cities = ["New York", "London", "Paris", "Tokyo", "Sydney", "Los Angeles", "Chicago", "Berlin", "Moscow", "Beijing", "Dubai", "Rome", "Amsterdam", "Singapore", "Toronto"];
      const classes = ["Economy", "Business", "First"];
      const randomDate = () => {
        const start = new Date(2024, 0, 1);
        const end = new Date(2024, 11, 31);
        return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime())).toISOString().split('T')[0];
      };
      const randomCity = () => cities[Math.floor(Math.random() * cities.length)];
      const status = statuses[Math.floor(Math.random() * statuses.length)];
      return {
        bookingNumber: `B${(i + 1).toString().padStart(3, '0')}`,
        firstName: status === "AVAILABLE" ? "" : firstNames[Math.floor(Math.random() * firstNames.length)],
        lastName: status === "AVAILABLE" ? "" : lastNames[Math.floor(Math.random() * lastNames.length)],
        date: randomDate(),
        from: randomCity(),
        to: randomCity(),
        bookingStatus: status,
        bookingClass: classes[Math.floor(Math.random() * classes.length)]
      };
    });
    
    setTimeout(() => {
      setBookings(mockBookings);
      setIsLoading(false);
    }, 1000); // Simulate network delay
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
      .onNext((token: string) => {
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

  const renderStatus = (booking: any) => {
    const status = booking.bookingStatus;
    return statusIcons[status as keyof typeof statusIcons] || status;
  };

  const renderBookingGrid = () => (
    <>
      <Grid items={filteredBookings.slice(0, visibleBookings)} className="flex-shrink-0">
        <GridColumn path="bookingNumber" header="#" autoWidth/>
        <GridColumn path="firstName" header="First Name" autoWidth/>
        <GridColumn path="lastName" header="Last Name" autoWidth/>
        <GridColumn path="date" header="Date" autoWidth/>
        <GridColumn path="from" header="From" autoWidth/>
        <GridColumn path="to" header="To" autoWidth/>
        <GridColumn header="Status" autoWidth>
          {({ item }) => renderStatus(item)}
        </GridColumn>
        <GridColumn path="bookingClass" header="Class" autoWidth/>
      </Grid>
      {filteredBookings.length > visibleBookings && (
        <Button onClick={() => setVisibleBookings(prev => prev + 10)} theme="primary" className="mt-4">
          Load More
        </Button>
      )}
    </>
  );

  const toggleClientManagement = () => {
    setClientManagementOpen(!clientManagementOpen);
  };

  if (isLoading) {
    return <div className="flex justify-center items-center h-full">Loading...</div>;
  }

  if (error) {
    return <div className="flex justify-center items-center h-full text-red-500">{error}</div>;
  }

  return (
    <>
      <SplitLayout className="h-full">
        <div className="flex flex-col gap-4 p-4 box-border h-full w-full md:w-1/3 bg-gray-100">
          <h2 className="text-2xl font-bold text-blue-600">Funnair Chat Support</h2>
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
        <div className="flex flex-col gap-6 p-6 box-border overflow-auto w-full md:w-2/3 bg-gray-50">
          <div className="flex justify-between items-center">
            <h2 className="text-3xl font-bold text-blue-700">Flight Management Dashboard</h2>
            <Button
              theme="primary"
              onClick={toggleClientManagement}
            >
              Client Management
            </Button>
          </div>
          <div className="flex gap-4 mb-4">
            <TextField
              placeholder="Search bookings..."
              value={searchTerm}
              onValueChanged={e => setSearchTerm(e.detail.value)}
              className="flex-grow"
            />
            <ComboBox
              label="Filter by Status"
              items={["CONFIRMED", "COMPLETED", "CANCELLED", "AWAITING_CONFIRMATION", "AVAILABLE"]}
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              clearButtonVisible
            />
          </div>
          <section className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow duration-300">
            <h3 className="text-xl font-semibold mb-3 text-blue-600">All Bookings</h3>
            {renderBookingGrid()}
          </section>
          <FlightStatusWidget />
        </div>
      </SplitLayout>
      <ClientManagementModal
        open={clientManagementOpen}
        onClose={toggleClientManagement}
      />
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
