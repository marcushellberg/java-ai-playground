import {useEffect, useState} from "react";
import BookingDetails from "Frontend/generated/com/example/application/services/BookingDetails";
import {AssistantService, BookingService} from "Frontend/generated/endpoints";
import {GridColumn} from "@hilla/react-components/GridColumn";
import {Grid} from "@hilla/react-components/Grid";
import {MessageList, MessageListItem} from "@hilla/react-components/MessageList";
import {MessageInput} from "@hilla/react-components/MessageInput";
import {nanoid} from "nanoid";
import {SplitLayout} from "@hilla/react-components/SplitLayout";

const chatId = nanoid();
export default function App() {
    const [working, setWorking] = useState(false);
    const [messages, setMessages] = useState<MessageListItem[]>([{
        userName: 'Assistant',
        text: 'Welcome to Funnair! How can I help you?',
        userColorIndex: 1
    }]);
    const [bookings, setBookings] = useState<BookingDetails[]>([]);

    useEffect(() => {
        // Update bookings when we have received the full response
        if (!working) {
            BookingService.getBookings().then(setBookings);
        }
    }, [working]);


    function addMessage(message: MessageListItem) {
        setMessages(messages => [...messages, message]);
    }

    function appendToLastMessage(chunk: string) {
        setMessages(messages => {
            const lastMessage = messages[messages.length - 1];
            lastMessage.text += chunk;
            return [...messages.slice(0, -1), lastMessage];
        });
    }

    async function sendMessage(message: string) {
        setWorking(true);
        addMessage({
            text: message,
            userName: 'You',
            userColorIndex: 2
        });
        let first = true;
        AssistantService.chat(chatId, message)
            .onNext(chunk => {
                if (first && chunk) {
                    addMessage({
                        text: chunk,
                        userName: 'Assistant',
                        userColorIndex: 1
                    });

                    first = false;
                } else {
                    appendToLastMessage(chunk);
                }
            })
            .onError(() => setWorking(false))
            .onComplete(() => setWorking(false));
    }

    return (
        <SplitLayout className="h-full">
            <div className="flex flex-col gap-m p-m box-border h-full" style={{width: '30%'}}>
                <h3>Customer support</h3>
                <MessageList items={messages} className="flex-grow"/>
                <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
            </div>
            <div className="flex flex-col gap-m p-m box-border" style={{width: '70%'}}>
                <h3>Bookings database</h3>
                <Grid items={bookings} className="flex-shrink-0">
                    <GridColumn path="bookingNumber" autoWidth/>
                    <GridColumn path="firstName" autoWidth/>
                    <GridColumn path="lastName" autoWidth/>
                    <GridColumn path="date" autoWidth/>
                    <GridColumn path="from" autoWidth/>
                    <GridColumn path="to" autoWidth/>
                    <GridColumn path="bookingStatus" autoWidth/>
                    <GridColumn path="bookingClass" autoWidth/>
                </Grid>
            </div>
        </SplitLayout>

    );
}
