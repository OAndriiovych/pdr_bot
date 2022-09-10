package org.pdr.services;

import org.pdr.Main;
import org.pdr.adatpers.ChatSenderI;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.services.realization.MainMenuServ;
import org.pdr.services.realization.QuizCreatorServ;
import org.pdr.services.realization.QuizHandlerServ;
import org.pdr.services.realization.UserRegisteredServ;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public enum EnumOfServices {
    MAIN_MANU(new MainMenuServ()),
    QUIZ_CREATOR(new QuizCreatorServ()),
    QUIZ_HANDLER(new QuizHandlerServ()),
    USER_REGISTERED(new UserRegisteredServ()),
    ;
    private final Service service;
    private static final ChatSenderI CHAT_SENDER = Main.CHAT_SENDER; // only service  Layer can send messages

    EnumOfServices(Service service) {
        this.service = service;
    }

    public EnumOfServices processUpdate(InternalUpdate internalUpdate) {
        Response response = service.processUpdate(internalUpdate);
        long chatId = internalUpdate.getChatId();
        List<Message> execute = CHAT_SENDER.execute(response.getMessageIList(), chatId);
        response.getCallback().accept(execute);
        EnumOfServices nextServ = response.getNextServ();
        if (response.isSendDefaultMessage()) {
            CHAT_SENDER.execute(nextServ.service.getDefaultMessage().setChatId(chatId));
        }
        return nextServ;
    }
}
