import {useEffect, useState} from "react";
import {AssistantService, BookingService} from "Frontend/generated/endpoints";
import BookingDetails from "Frontend/generated/org/vaadin/marcus/service/BookingDetails";
import {GridColumn} from "@hilla/react-components/GridColumn";
import {Grid} from "@hilla/react-components/Grid";
import {MessageInput} from "@hilla/react-components/MessageInput";
import {nanoid} from "nanoid";
import {SplitLayout} from "@hilla/react-components/SplitLayout";
import Message, {MessageItem} from "Frontend/Message";
import {Select} from "@hilla/react-components/Select";
import {ComboBox} from "@hilla/react-components/ComboBox";

const supportedLibraries = ['LangChain4j', 'Spring AI'];

export default function App() {
  const [chatId, setChatId] = useState(nanoid());
  const [library, setLibrary] = useState(supportedLibraries[0]);
  const [working, setWorking] = useState(false);
  const [bookings, setBookings] = useState<BookingDetails[]>([]);
  const [messages, setMessages] = useState<MessageItem[]>([{
    role: 'assistant',
    content: 'Welcome to Funnair! How can I help you?'
  }]);

  useEffect(() => {
    // Update bookings when we have received the full response
    if (!working) {
      BookingService.getBookings().then(setBookings);
    }
  }, [working]);

  function addMessage(message: MessageItem) {
    setMessages(messages => [...messages, message]);
  }

  function appendToLatestMessage(chunk: string) {
    setMessages(messages => {
      const latestMessage = messages[messages.length - 1];
      latestMessage.content += chunk;
      return [...messages.slice(0, -1), latestMessage];
    });
  }

  async function sendMessage(message: string) {
    setWorking(true);
    addMessage({
      role: 'user',
      content: message
    });
    let first = true;
    AssistantService.chat(chatId, message, library)
      .onNext(chunk => {
        if (first && chunk) {
          addMessage({
            role: 'assistant',
            content: chunk
          });

          first = false;
        } else {
          appendToLatestMessage(chunk);
        }
      })
      .onError(() => setWorking(false))
      .onComplete(() => setWorking(false));
  }

  function changeLibrary(library: string) {
    setLibrary(library);
    setChatId(nanoid());
    setMessages([{
      role: 'assistant',
      content: 'Welcome to Funnair! How can I help you?'
    }]);
  }

  return (
    <SplitLayout className="h-full">
      <div className="flex flex-col gap-m p-m box-border h-full" style={{width: '30%'}}>
        <div className="flex gap-s items-baseline">
          <h3 className="flex-grow">Funnair support</h3>
          <ComboBox
            items={supportedLibraries}
            style={{width: "140px"}}
            value={library}
            onChange={e => changeLibrary(e.target.value)}
          />
        </div>
        <div className="flex-grow overflow-scroll">
          {messages.map((message, index) => (
            <Message
              key={index}
              message={message}
            />
          ))}
        </div>
        <MessageInput onSubmit={e => sendMessage(e.detail.value)} className="px-0"/>
      </div>
      <div className="flex flex-col gap-m p-m box-border" style={{width: '70%'}}>
        <h3>Bookings database</h3>
        <Grid items={bookings} className="flex-shrink-0">
          <GridColumn path="bookingNumber" autoWidth header="#"/>
          <GridColumn path="firstName" autoWidth/>
          <GridColumn path="lastName" autoWidth/>
          <GridColumn path="date" autoWidth/>
          <GridColumn path="from" autoWidth/>
          <GridColumn path="to" autoWidth/>
          <GridColumn path="bookingStatus" autoWidth header="Status">
            {({item}) => item.bookingStatus === "CONFIRMED" ? "✅" : "❌"}
          </GridColumn>
          <GridColumn path="bookingClass" autoWidth/>
        </Grid>
      </div>
    </SplitLayout>

  );
}
