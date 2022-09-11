package org.pdr.adatpers;

import org.telegram.telegrambots.meta.api.objects.Message;

public class InternalExecuteMessage {
    private final Message response;

    public InternalExecuteMessage(Message response) {
        this.response = response;
    }

    public long getChatId() {
        return response.getChatId();
    }

    public int getMessageId() {
        return response.getMessageId();
    }
}
