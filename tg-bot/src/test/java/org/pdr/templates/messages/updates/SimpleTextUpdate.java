package org.pdr.templates.messages.updates;

import org.pdr.templates.messages.SimpleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SimpleTextUpdate extends Update {
    public SimpleTextUpdate() {
        setUpdateId(123456);
        setMessage(new SimpleMessage());
    }

    public void setMessageId(Integer messageId){
        getMessage().setMessageId(messageId);
    }
}
