import { useState } from 'react';
import {MessageInput} from "@hilla/react-components/MessageInput";
import {MessageList, MessageListItem} from "@hilla/react-components/MessageList";
import {AssistantService} from "Frontend/generated/endpoints";

export default function CustomerServiceView() {
    const [messages, setMessages] = useState<MessageListItem[]>([]);

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
        addMessage({
            text: message,
            userName: 'You'
        });

        let first = true;
        AssistantService.chat(message).onNext(chunk => {
            if (first && chunk) {
                addMessage({
                    text: chunk,
                    userName: 'Assistant'
                });

                first = false;
            } else {
                appendToLastMessage(chunk);
            }
        });
    }

    return (
        <div className="p-m flex flex-col h-full box-border">
            <MessageList items={messages} className="flex-grow"/>
            <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
        </div>
    );
}
