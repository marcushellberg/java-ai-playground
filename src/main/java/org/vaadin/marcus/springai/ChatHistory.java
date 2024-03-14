package org.vaadin.marcus.springai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatHistory {

	private static final Logger logger = LoggerFactory.getLogger(ChatHistory.class);

	private final Map<String, List<Message>> chatHistoryLog;
	private final Map<String, List<Message>> messageAggregations;

	public ChatHistory() {
		this.chatHistoryLog = new ConcurrentHashMap<>();
		this.messageAggregations = new ConcurrentHashMap<>();
	}

	public void addMessage(String chatId, Message message) {
		String groupId = toGroupId(chatId, message);

		this.messageAggregations.computeIfAbsent(groupId, key -> new ArrayList<>()).add(message);

		if (this.messageAggregations.size() > 1) {
			logger.warn("Multiple active sessions: {}", this.messageAggregations.keySet());
		}

		String finishReason = getProperty(message, "finishReason");
		if ("STOP".equalsIgnoreCase(finishReason) || message.getMessageType() == MessageType.USER) {
			this.finalizeMessageGroup(chatId, groupId);
		}
	}

	private String toGroupId(String chatId, Message message) {
		String messageId = getProperty(message, "id");
		return chatId + ":" + messageId;
	}

	private String getProperty(Message message, String key) {
		return message.getProperties().getOrDefault(key, "").toString();
	}

	private void finalizeMessageGroup(String chatId, String groupId) {
		List<Message> sessionMessages = this.messageAggregations.remove(groupId);

		if (sessionMessages != null) {
			if (sessionMessages.size() == 1) {
				this.commitToHistoryLog(chatId, sessionMessages.get(0));
			} else {
				String aggregatedContent = sessionMessages.stream()
						.map(Message::getContent)
						.filter(Objects::nonNull)
						.collect(Collectors.joining());
				this.commitToHistoryLog(chatId, new AssistantMessage(aggregatedContent));
			}
		} else {
			logger.warn("No active session for groupId: {}", groupId);
		}
	}

	private void commitToHistoryLog(String chatId, Message message) {
		this.chatHistoryLog.computeIfAbsent(chatId, key -> new ArrayList<>()).add(message);
	}

	public List<Message> getAll(String chatId) {
		return this.chatHistoryLog.getOrDefault(chatId, List.of());
	}

	public List<Message> getLastN(String chatId, int lastN) {
		List<Message> response = getAll(chatId);

		if (response.size() <= lastN) {
			return response;
		}

		int from = response.size() - lastN;
		int to = response.size();
		logger.debug("Returning last {} messages from {} to {}", lastN, from, to);

		return new ArrayList<>(response.subList(from, to));
	}
}