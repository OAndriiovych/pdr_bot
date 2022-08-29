package org.pdr.adatpers.messages;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class RequestAccessForPhoneMessage extends TextMessage implements MessageI {

    public static final String REQUEST_ACCESS_PHONE = "Запит на доступ до телефону";

    public RequestAccessForPhoneMessage() {
        super(REQUEST_ACCESS_PHONE);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText(REQUEST_ACCESS_PHONE);
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        this.setReplyKeyboard_(new ReplyKeyboardMarkup(keyboard, true, true, true, null));
    }
}
