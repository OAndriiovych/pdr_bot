package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.RequestAccessForPhoneMessage;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.User;
import org.pdr.model.payment.PaymentModel;
import org.pdr.model.payment.UserModel;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class UserRegisteredServ extends Service {

    private static final String REG_USER = "Створити аккаунт";
    private static final String HISTORY = "Подивитись історію";
    private static final String PAY_FOR_SUBSCRIPTION = "Оплатити підписку";
    private static final String SUBSCRIPTION_DETAIL = "Переваги підписки";

    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());

    private final PaymentModel paymentModel;

    private final UserModel userModel;

    public UserRegisteredServ() {
        this.paymentModel = new PaymentModel();
        this.userModel = new UserModel();
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
        String userAnswer = internalUpdate.getReply().getText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case RequestAccessForPhoneMessage.REQUEST_ACCESS_PHONE:
                Contact userInfo = internalUpdate.getUserInfo();
                userModel.registrarUser(new User(chatId, userInfo.getUserId(), userInfo.getPhoneNumber(), userInfo.getFirstName()));
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
                if (userModel.isChatIdConnectToUser(chatId)) {
                    CHAT_SENDER.execute(new TextMessage("Ви вже зареєстровані в нас").setChatId(chatId));
                    sendButtons(chatId);
                } else {
                    CHAT_SENDER.execute(new RequestAccessForPhoneMessage());
                }
                break;
            case HISTORY:
                List<MessageI> historyMessages = userModel.sendHistory(chatId);
                CHAT_SENDER.execute(historyMessages, chatId);
                sendButtons(chatId);
                break;
            case SUBSCRIPTION_DETAIL:
                //#TODO
                CHAT_SENDER.execute(new TextMessage("Нам треба гроші").setChatId(chatId));
                sendButtons(chatId);
                break;
            case PAY_FOR_SUBSCRIPTION:
                if (userModel.isChatIdConnectToUser(chatId)) {
                    List<MessageI> paymentMessages = paymentModel.createPayment(chatId);
                    CHAT_SENDER.execute(paymentMessages, chatId);
                    sendButtons(chatId);
                } else {
                    CHAT_SENDER.execute(new TextMessage("Будь ласка зареєструйтесь спочатку"));
                    CHAT_SENDER.execute(new RequestAccessForPhoneMessage());
                }
                break;
            default:
                sendButtons(chatId);
        }
        return nextServ;
    }

}
