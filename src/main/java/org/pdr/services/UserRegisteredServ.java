package org.pdr.services;

import org.pdr.entity.Payment;
import org.pdr.repository.PaymentRepository;
import org.pdr.repository.UserRepository;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.User;
import org.pdr.utils.LiqPayUtil;
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
    private static final String HISTORY = "Подивитись історію";
    private static final String PAY_FOR_SUBSCRIPTION = "Оплатити підписку";
    private static final String SUBSCRIPTION_DETAIL = "Переваги підписки";
    private static final String REQUEST_ACCESS_PHONE = "Запит на доступ до телефону";

    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final LiqPayUtil liqPayUtil;

    public UserRegisteredServ() {
        this.userRepository = new UserRepository();
        this.paymentRepository = new PaymentRepository();
        this.liqPayUtil = new LiqPayUtil();
    }

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(REG_USER);
        firstRow.add(HISTORY);
        List<String> secRow = new ArrayList<>();
        buttons.add(secRow);
        secRow.add(PAY_FOR_SUBSCRIPTION);
        secRow.add(SUBSCRIPTION_DETAIL);
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
                Contact userInfo = internalUpdate.getUserInfo();
                userRepository.save(new User(chatId, userInfo.getUserId(), userInfo.getPhoneNumber(), userInfo.getFirstName()));
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
            case HISTORY:
                User user = userRepository.getUserByChatId(chatId);
                if (user.isPrem()) {

                } else {
                    CHAT_SENDER.execute(new TextMessage("Тільки по платній підписці").setChatId(chatId));
                }
                break;
            case SUBSCRIPTION_DETAIL:
                //#TODO
                CHAT_SENDER.execute(new TextMessage("Нам треба гроші").setChatId(chatId));
                sendButtons(chatId);
                break;
            case PAY_FOR_SUBSCRIPTION:
                User userByChatId = userRepository.getUserByChatId(chatId);
                if (userByChatId == null) {
                    CHAT_SENDER.execute(new TextMessage("Будь ласка зареєструйтесь спочатку").setChatId(chatId));
                    sendRequestAccessForPhone(chatId);
                    break;
                }
                Payment payment = paymentRepository.getPaymentByChatId(chatId);
                if (payment == null) {
                    payment = new Payment();
                    payment.setLinkUser(userByChatId);
                    //#TODO add Logger
                    paymentRepository.save(payment);
                }else if (payment.isPaid()) {
                    CHAT_SENDER.execute(new TextMessage("Ваша підписка ще активна").setChatId(chatId));
                    break;
                }
                try {
                    String urlForPayment = liqPayUtil.createUrlForPayment(payment);
                    CHAT_SENDER.execute(new TextMessage("Сторінка для оплати готова").setChatId(chatId));
                    CHAT_SENDER.execute(new TextMessage(urlForPayment).setChatId(chatId));
                } catch (Exception e) {
                    //#TODO add Logger
                    CHAT_SENDER.execute(new TextMessage("Вибачне не можемо створити сторінку для оплати. Спробуйте пізніше будь ласка").setChatId(chatId));
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
