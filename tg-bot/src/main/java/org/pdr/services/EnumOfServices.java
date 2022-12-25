package org.pdr.services;

import org.pdr.services.realization.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum EnumOfServices {
    MAIN_MANU(MainMenuServ.class),
    QUIZ_CREATOR(QuizCreatorServ.class),
    QUIZ_HANDLER(QuizHandlerServ.class),
    USER_REGISTERED(UserRegisteredServ.class),
    FIRST_FAIL(FirstFailServ.class),
    FIRST_LOGIN(FirstLoginServ.class);

    private final Class<? extends Service> enumService;

    EnumOfServices(Class<? extends Service> service) {
        this.enumService = service;
    }

    public static EnumOfServices valueOf(Class<? extends Service> inputService) {
        return Arrays.stream(EnumOfServices.values())
                .filter(enumOfServices -> enumOfServices.enumService == inputService)
                .findFirst().orElse(null);
    }

    @Configuration
    public static class Conf {
        public Conf() {
        }

        @Bean
        public Map<EnumOfServices, Service> postConstruct(@Autowired ApplicationContext appContext) {
            Map<EnumOfServices, Service> serviceByEnum = new HashMap<>();
            for (EnumOfServices i : EnumOfServices.values()) {
                serviceByEnum.put(i, appContext.getBean(i.enumService));
            }
            return serviceByEnum;
        }
    }
}
