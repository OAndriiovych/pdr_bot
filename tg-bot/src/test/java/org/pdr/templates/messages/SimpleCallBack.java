package org.pdr.templates.messages;

import org.pdr.templates.SimpleUser;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class SimpleCallBack extends CallbackQuery {
    public SimpleCallBack() {
        setId("123456789");
        setFrom(new SimpleUser());
        setChatInstance("-123456789");
    }
}
