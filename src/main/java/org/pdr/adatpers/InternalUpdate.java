package org.pdr.adatpers;

import org.telegram.telegrambots.meta.api.objects.Update;

public class InternalUpdate {
    private final Update update;

    public InternalUpdate(Update update) {
        this.update = update;
    }

    public long getChatId() {
        long chatId;
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
        return chatId;
    }

    public String getMessageText() {
        return update.getMessage().getText();
    }

    public String getCallBackText() {
        return update.getCallbackQuery().getMessage().getText();
    }

    public int getMessageId() {
        return update.getMessage().getMessageId();
    }

    public int getCallBackMessageId() {
        return update.getCallbackQuery().getMessage().getMessageId();
    }

    public String getCallbackData() {
        return update.getCallbackQuery().getData();
    }
}
