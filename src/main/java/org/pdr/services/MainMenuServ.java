package org.pdr.services;

import org.pdr.Main;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.TextMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainMenuServ implements Service {
    private static final String ALL_QUESTION = "Тести";
    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(ALL_QUESTION);
        return buttons;
    }

    public static void sendButtons(long chatId) {
        CHAT_SENDER.execute(new TextMessage("Вітаю в головному меню").setButtons(listOfCommands).setChatId(chatId));
    }

    @Override
    public Service processUpdate(InternalUpdate internalUpdate) {
        Service nextServ = this;
        String userAnswer = internalUpdate.getMessageText();
        switch (userAnswer) {
            case ALL_QUESTION:
                nextServ = new TestCreatorServ();
                TestCreatorServ.sendButtons(internalUpdate.getChatId());
                break;
            default:
                CHAT_SENDER.execute(new TextMessage("Не зрозумів тебе").setChatId(internalUpdate.getChatId()));
                sendButtons(internalUpdate.getChatId());
        }
        return nextServ;
    }


}
