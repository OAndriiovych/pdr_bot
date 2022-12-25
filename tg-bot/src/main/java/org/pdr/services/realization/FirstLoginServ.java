package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FirstLoginServ extends MainMenuServ {
    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    @Override
    protected Response processTextMessage(InternalUpdate internalUpdate) {
        logger.info("new User {}", internalUpdate.getChatId());
        return Response.builder()
                .message(new TextMessage("Вітаю в нашому чат боті"))
                .nextServ(EnumOfServices.MAIN_MANU).build();
    }
}
