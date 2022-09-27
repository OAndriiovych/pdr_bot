package org.pdr.adatpers.messages;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class RemoveReplyKeyboardMessage extends TextMessage{
    public RemoveReplyKeyboardMessage(String text) {
        super(text);
        ReplyKeyboardRemove replyMarkup = new ReplyKeyboardRemove();
        replyMarkup.setRemoveKeyboard(true);
        setReplyMarkup(replyMarkup);
    }
}
