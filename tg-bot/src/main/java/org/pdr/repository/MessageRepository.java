package org.pdr.repository;

import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageRepository {
    private static final Map<Long, Integer> lastMessageIdByChatId = new HashMap<>();

    public void registrateNewMessageId(long chatId, int messageId) {
        lastMessageIdByChatId.put(chatId, messageId);
    }

    public void registrateNewMessage(InternalExecuteMessage message) {
        registrateNewMessageId(message.getChatId(), message.getMessageId());
    }

    public boolean checkMessageId(InternalUpdate message) {
        int messageId = Optional.ofNullable(message.getCallBackMessageId()).orElse(message.getMessageId());
        return checkMessageId(message.getChatId(), messageId);
    }

    public boolean checkMessageId(long chatId, int messageId) {
        return messageId == lastMessageIdByChatId.get(chatId);
    }

    public void removeMessage(long chatId) {
        lastMessageIdByChatId.remove(chatId);
    }
}
