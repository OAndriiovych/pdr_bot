package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;

public enum EnumOfServices {
    MAIN_MANU(new MainMenuServ()),
    QUIZ_CREATOR(new QuizCreatorServ()),
    QUIZ_HANDLER(new QuizHandlerServ()),
    USER_REGISTERED(new UserRegisteredServ()),
    ;
    private final Service service;

    EnumOfServices(Service service) {
        this.service = service;
    }

    public EnumOfServices processUpdate(InternalUpdate internalUpdate){
        return service.processUpdate(internalUpdate);
    }
}
