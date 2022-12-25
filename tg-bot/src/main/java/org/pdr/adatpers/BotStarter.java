package org.pdr.adatpers;

import org.pdr.services.Sender;
import org.pdr.utils.MyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Component
public class BotStarter extends ChatSender {
    private static final Logger logger = LoggerFactory.getLogger(BotStarter.class);
    @Autowired
    Sender processUpdate;
    @Autowired
    MyProperties yProperties2;

    @PostConstruct
    public void startBot() {
        processUpdate.setCHAT_SENDER(this);
        try {
            logger.info("starting the bot");
            new TelegramBotsApi(DefaultBotSession.class).registerBot(this);
            logger.info("bot started");
        } catch (TelegramApiException e) {
            logger.error("ERROR in Main method", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        processUpdate.onUpdateReceived(new InternalUpdate(update));
    }

    @Override
    public String getBotUsername() {
        return yProperties2.getTelegramBotName();
    }

    @Override
    public String getBotToken() {
        return yProperties2.getTelegramBotToken();
    }
}
