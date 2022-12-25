package org.pdr.services;

import org.pdr.adatpers.ChatSenderI;
import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.implementation.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Sender {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private Map<EnumOfServices, Service> serviceByEnum;
    private ChatSenderI CHAT_SENDER; // only service  Layer can send messages

    public void setCHAT_SENDER(ChatSenderI CHAT_SENDER) {
        this.CHAT_SENDER = CHAT_SENDER;
    }

    public void onUpdateReceived(InternalUpdate internalUpdate) {
        EnumOfServices currentServiceEnum = sessionManager.getSession(internalUpdate);
        Service service = serviceByEnum.get(currentServiceEnum);
        Response response = service.processUpdate(internalUpdate);
        EnumOfServices nextService = processResponse(response, internalUpdate.getChatId());
        sessionManager.saveSession(internalUpdate, nextService);
    }

    private EnumOfServices processResponse(Response response, long chatId) {
        List<InternalExecuteMessage> execute = CHAT_SENDER.execute(response.getMessages(), chatId);
        response.executeCallback(execute);
        EnumOfServices nextServ = response.getNextServ();
        if (response.isSendDefaultMessage()) {
            Service nextServices = serviceByEnum.get(nextServ);
            CHAT_SENDER.execute(nextServices.getDefaultMessage().setChatId(chatId));
        }
        return nextServ;
    }
}
