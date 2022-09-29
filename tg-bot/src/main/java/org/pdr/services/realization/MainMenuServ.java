package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainMenuServ extends Service {
    private static final String ALL_QUESTION = "Тести";
    private static final String USER_ROOM = "кабінет";
    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(ALL_QUESTION);
        firstRow.add(USER_ROOM);
        return buttons;
    }

    @Override
    protected Response processTextMessage(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.MAIN_MANU);
        String userAnswer = internalUpdate.getMessageText();
        switch (userAnswer) {
            case ALL_QUESTION:
                response.setNextServ(EnumOfServices.QUIZ_CREATOR);
                break;
            case USER_ROOM:
                response.setNextServ(EnumOfServices.USER_REGISTERED);
                break;
            default:
                break;
        }
        return response;
    }

    @Override
    protected MessageI getDefaultMessage() {
        return new TextMessage("Вітаю в головному меню").setButtons(listOfCommands);
    }
}
