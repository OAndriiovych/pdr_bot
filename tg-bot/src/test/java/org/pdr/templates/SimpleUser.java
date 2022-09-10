package org.pdr.templates;

import org.telegram.telegrambots.meta.api.objects.User;

public class SimpleUser extends User {
    public SimpleUser() {
        setId(123456L);
        setFirstName("Test User FirstName");
        setIsBot(false);
        setUserName("Test User setUserName");
        setLanguageCode("uk");
    }
}
