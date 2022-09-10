package org.pdr.templates;

import org.telegram.telegrambots.meta.api.objects.Chat;

public class SimpleChat extends Chat {
    public SimpleChat(SimpleUser user) {
        setId(123456L);
        setType("private");
        setFirstName(user.getFirstName());
        setUserName(user.getUserName());
    }
}
