package org.pdr.model.repository;

import java.util.HashMap;
import java.util.Map;

public class MessageRepository {
    private static final Map<Long, Integer> lastMessageIdByChatId = new HashMap<>();

    public void registrateNewMessageId(long chatId, int messageId) {
        lastMessageIdByChatId.put(chatId, messageId);
    }

    public boolean checkMessageId(long chatId, int messageId) {
        return messageId == lastMessageIdByChatId.get(chatId);
    }

    public void removeMessage(long chatId) {
        lastMessageIdByChatId.remove(chatId);
    }
}
