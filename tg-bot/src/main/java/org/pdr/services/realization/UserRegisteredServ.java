package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.RequestAccessForPhoneMessage;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.User;
import org.pdr.model.payment.PaymentModel;
import org.pdr.model.payment.UserModel;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Service;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRegisteredServ extends Service {

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

    @Override
    protected MessageI getDefaultMessage() {
        return new TextMessage("Виберіть щось з наведених пунктів").setButtons(listOfCommands);
    }

    @Override
    protected Response processReply(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.USER_REGISTERED);
        String userAnswer = internalUpdate.getReply().getText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case RequestAccessForPhoneMessage.REQUEST_ACCESS_PHONE:
                Contact userInfo = internalUpdate.getUserInfo();
                User user = new User(chatId, userInfo.getUserId(), userInfo.getPhoneNumber(), userInfo.getFirstName());
                response.addMessage(userModel.registrarUser(user));
                break;
            default:
                break;
        }
        return response;
    }

    @Override
    protected Response processTextMessage(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.USER_REGISTERED);
        String userAnswer = internalUpdate.getMessageText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case REG_USER:
                if (userModel.isChatIdConnectToUser(chatId)) {
                    response.addMessage(new TextMessage("Ви вже зареєстровані в нас"));
                } else {
                    response.addMessage(new RequestAccessForPhoneMessage());
                    response.setSendDefaultMessage(false);
                }
                break;
            case HISTORY:
                response.addMessage(userModel.sendHistory(chatId));
                break;
            case SUBSCRIPTION_DETAIL:
                //#TODO
                response.addMessage(new TextMessage("Нам треба гроші"));
                break;
            case PAY_FOR_SUBSCRIPTION:
                if (userModel.isChatIdConnectToUser(chatId)) {
                    response.addMessage(paymentModel.createPayment(chatId));
                } else {
                    response.addMessage(new TextMessage("Будь ласка зареєструйтесь спочатку"));
                    response.addMessage(new RequestAccessForPhoneMessage());
                }
                break;
            default:
                break;
        }
        return response;
    }

}
