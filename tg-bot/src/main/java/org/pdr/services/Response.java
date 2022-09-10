package org.pdr.services;

import org.pdr.adatpers.messages.MessageI;
import org.pdr.utils.DataStructure;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@DataStructure
public class Response {
    private EnumOfServices nextServ;

    private boolean sendDefaultMessage = true;
    private Consumer<List<Message>> callback = a -> {
    };
    private final List<MessageI> messageIList = new LinkedList<>();

    public Response(EnumOfServices defaultService) {
        this.nextServ = defaultService;
    }

    public void addMessage(MessageI messageI) {
        messageIList.add(messageI);
    }

    public void addMessage(List<MessageI> messageI) {
        messageIList.addAll(messageI);
    }

    EnumOfServices getNextServ() {
        return nextServ;
    }

    public void setNextServ(EnumOfServices nextServ) {
        this.nextServ = nextServ;
    }

    boolean isSendDefaultMessage() {
        return sendDefaultMessage;
    }

    public void setSendDefaultMessage(boolean sendDefaultMessage) {
        this.sendDefaultMessage = sendDefaultMessage;
    }

    Consumer<List<Message>> getCallback() {
        return callback;
    }

    public void setCallback(Consumer<List<Message>> callback) {
        this.callback = callback;
    }

    List<MessageI> getMessageIList() {
        return messageIList;
    }
}
