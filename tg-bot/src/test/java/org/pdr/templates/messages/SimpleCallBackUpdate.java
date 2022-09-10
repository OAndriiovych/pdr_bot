package org.pdr.templates.messages;

import org.telegram.telegrambots.meta.api.objects.Update;

public class SimpleCallBackUpdate extends Update {
    public SimpleCallBackUpdate() {
        setUpdateId(123456);
        setCallbackQuery(new SimpleCallBack());
    }
}
