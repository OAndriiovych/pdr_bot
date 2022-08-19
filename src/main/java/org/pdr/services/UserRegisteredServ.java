package org.pdr.services;

import org.pdr.repository.UserRepository;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.User;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class UserRegisteredServ extends Service {

    private static final String REG_USER = "Створити аккаунт";
    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());
    private static final String REQUEST_ACCESS_PHONE = "Запит на доступ до телефону";

    private final UserRepository userRepository = new UserRepository();

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
        EnumOfServices nextServ;
        if (internalUpdate.isReply()) {
            nextServ = processReplyUpdate(internalUpdate);
        } else {
            nextServ = processDefaultUpdate(internalUpdate);
        }
        return nextServ;
    }

    private EnumOfServices processReplyUpdate(InternalUpdate internalUpdate) {
        EnumOfServices nextServ = EnumOfServices.USER_REGISTERED;
        Message reply = internalUpdate.getReply();
        String userAnswer = reply.getText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case REQUEST_ACCESS_PHONE:
                User newUser = new User();
                newUser.chatId = chatId;
                Contact userInfo = internalUpdate.getUserInfo();
                newUser.phone = userInfo.getPhoneNumber();
                newUser.userName = userInfo.getFirstName();
                userRepository.save(newUser);
                CHAT_SENDER.execute(new TextMessage("Ми вас зареєстрували").setChatId(chatId));
                sendButtons(chatId);
                break;
            default:
                sendButtons(chatId);
        }
        return nextServ;
    }

    private EnumOfServices processDefaultUpdate(InternalUpdate internalUpdate) {
        EnumOfServices nextServ = EnumOfServices.USER_REGISTERED;
        String userAnswer = internalUpdate.getMessageText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case REG_USER:
                if (userRepository.getUserByChatId(chatId) != null) {
                    CHAT_SENDER.execute(new TextMessage("Ви вже зареєстровані в нас").setChatId(chatId));
                    sendButtons(chatId);
                } else {
                    sendRequestAccessForPhone(chatId);
                }
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
        keyboardButton.setText(REQUEST_ACCESS_PHONE);
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        CHAT_SENDER.execute(new TextMessage(REQUEST_ACCESS_PHONE).setChatId(chatId).setReplyKeyboard_(
                new ReplyKeyboardMarkup(keyboard, true, true, true, null)));
    }
}
