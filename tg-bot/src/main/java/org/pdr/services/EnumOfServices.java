package org.pdr.services;

import org.pdr.services.realization.*;

import java.util.Arrays;

public enum EnumOfServices {
    MAIN_MANU(MainMenuServ.class),
    QUIZ_CREATOR(QuizCreatorServ.class),
    QUIZ_HANDLER(QuizHandlerServ.class),
    USER_REGISTERED(UserRegisteredServ.class),

    FIRST_FAIL(FirstFailServ.class);

    final Class<? extends Service> enumService;

    EnumOfServices(Class<? extends Service> service) {
        this.enumService = service;
    }

    public static EnumOfServices valueOf(Class<? extends Service> inputService) {
        return Arrays.stream(EnumOfServices.values())
                .filter(enumOfServices -> enumOfServices.enumService == inputService)
                .findFirst().orElse(null);
    }
}
