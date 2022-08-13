package org.pdr.services;

import org.pdr.Main;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;

public class EchoService implements Service {
    @Override
    public Service processUpdate(InternalUpdate internalUpdate) {
        String userAnswer = internalUpdate.getMessageText();
        MessageI textMessage = new TextMessage(userAnswer);
        textMessage.setChatId(internalUpdate.getChatId());

        Main.CHAT_SENDER.execute(textMessage);
        return this;
    }

    public static void sendHiMessage(long chatId) {
        Main.CHAT_SENDER.execute(new TextMessage("/help - опис команд, /start - почати тестування, вибір кількості питань, /stop - зупинка тесту").setChatId(chatId));
    }
}
