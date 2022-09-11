package org.pdr.templates.messages.executed;

import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.templates.messages.SimpleMessage;

public class SimpleExecuteMessage extends InternalExecuteMessage {
    public SimpleExecuteMessage() {
        super(new SimpleMessage());
    }
    public void setMessageId(Integer messageId){
        response.setMessageId(messageId);
    }
}
