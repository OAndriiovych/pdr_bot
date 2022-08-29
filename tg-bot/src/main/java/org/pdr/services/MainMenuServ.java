package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.TextMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MainMenuServ extends Service {
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

    public static void sendButtons(long chatId) {
        CHAT_SENDER.execute(new TextMessage("Виберіть щось з наведених пунктів").setButtons(listOfCommands).setChatId(chatId));
    }

    @Override
    EnumOfServices processUpdate(InternalUpdate internalUpdate) {
        EnumOfServices nextServ = EnumOfServices.MAIN_MANU;
        String userAnswer = internalUpdate.getMessageText();
        switch (userAnswer) {
            case ALL_QUESTION:
                nextServ = EnumOfServices.QUIZ_CREATOR;
                QuizCreatorServ.sendButtons(internalUpdate.getChatId());
                break;
            case USER_ROOM:
                nextServ = EnumOfServices.USER_REGISTERED;
                UserRegisteredServ.sendButtons(internalUpdate.getChatId());
                break;
            default:
                sendButtons(internalUpdate.getChatId());
        }
        return nextServ;
    }


}
