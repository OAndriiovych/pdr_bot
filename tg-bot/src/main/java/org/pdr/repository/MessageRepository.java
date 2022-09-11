package org.pdr.repository;

import org.pdr.adatpers.InternalExecuteMessage;

import java.util.HashMap;
import java.util.Map;

public class MessageRepository {
    private static final Map<Long, Integer> lastMessageIdByChatId = new HashMap<>();

    public void registrateNewMessageId(long chatId, int messageId) {
        lastMessageIdByChatId.put(chatId, messageId);
    }

    public void registrateNewMessage(InternalExecuteMessage message) {
        registrateNewMessageId(message.getChatId(), message.getMessageId());
    }

    public boolean checkMessageId(long chatId, int messageId) {
        return messageId == lastMessageIdByChatId.get(chatId);
    }

    public void removeMessage(long chatId) {
        lastMessageIdByChatId.remove(chatId);
    }
}
