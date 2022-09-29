package org.pdr.templates.messages;

import org.pdr.templates.SimpleUser;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class SimpleCallBackQuery extends CallbackQuery {
    public SimpleCallBackQuery() {
        setId("123456789");
        setFrom(new SimpleUser());
        setChatInstance("-123456789");
        setMessage(new SimpleMessage());
    }
}
