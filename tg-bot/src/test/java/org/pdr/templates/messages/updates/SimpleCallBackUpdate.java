package org.pdr.templates.messages.updates;

import org.pdr.templates.messages.SimpleCallBackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SimpleCallBackUpdate extends Update {

    public SimpleCallBackUpdate() {
        setUpdateId(123456);
        setCallbackQuery(new SimpleCallBackQuery());
    }

    public void setMessageId(Integer messageId) {
        getCallbackQuery().getMessage().setMessageId(messageId);
    }
}
