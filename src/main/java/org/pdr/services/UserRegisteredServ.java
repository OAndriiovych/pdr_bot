package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.TextMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class UserRegisteredServ extends Service {

    private static final String REG_USER = "";

    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(REG_USER);
        return buttons;
    }
    public static void sendButtons(long chatId) {
        CHAT_SENDER.execute(new TextMessage("Виберіть щось з наведених пунктів").setButtons(listOfCommands).setChatId(chatId));
    }

    @Override
    EnumOfServices processUpdate(InternalUpdate internalUpdate) {
        EnumOfServices nextServ = EnumOfServices.USER_REGISTERED;
        String userAnswer = internalUpdate.getMessageText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case REG_USER:
                sendRequestAccessForPhone(chatId);
                break;
            default:
                sendButtons(chatId);
        }
        return nextServ;
    }

    private static void sendRequestAccessForPhone(long chatId) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Запит на доступ до телефону");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        CHAT_SENDER.execute(new TextMessage("Запит на доступ до телефону").setChatId(chatId).setReplyKeyboard_(
                new ReplyKeyboardMarkup(keyboard, true, true, true, null)));
    }
}
