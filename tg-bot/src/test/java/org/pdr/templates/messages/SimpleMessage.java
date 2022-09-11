package org.pdr.templates.messages;

import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.templates.SimpleChat;
import org.pdr.templates.SimpleUser;
import org.telegram.telegrambots.meta.api.objects.Message;

public class SimpleMessage extends Message {
    public SimpleMessage() {
        setMessageId(123);
        setDate(1662813596);
        setText("Text");
        SimpleUser from = new SimpleUser();
        setFrom(from);
        setChat(new SimpleChat(from));
    }

    public InternalExecuteMessage toInternalExecuteMessage(){
        return  new InternalExecuteMessage(this);
    }
}
