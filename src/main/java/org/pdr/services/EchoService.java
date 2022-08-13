package org.pdr.services;

import org.pdr.Main;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;

public class EchoService extends Service {
    @Override
    public Service processUpdate(InternalUpdate internalUpdate) {
        String userAnswer = internalUpdate.getMessageText();
        MessageI textMessage = new TextMessage(userAnswer);
        textMessage.setChatId(internalUpdate.getChatId());

        Main.CHAT_SENDER.execute(textMessage);
        return this;
    }
}
