package org.pdr;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.pdr.adatpers.ChatSender;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.services.EchoService;
import org.pdr.services.Service;
import org.pdr.utils.MyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.TimeUnit;

public class Main extends ChatSender {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static final ChatSender CHAT_SENDER = new Main();

    private static final LoadingCache<Long, Service> SERVICE_BY_CHAT_ID_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(7, TimeUnit.DAYS).build(
                    new CacheLoader<>() {
                        @Override
                        public Service load(Long chatId) {
                            return new EchoService();
                        }
                    });


    public static void main(String[] args) {
        logger.info("Bot started");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(CHAT_SENDER);
        } catch (TelegramApiException e) {
            logger.error("ERROR in Main method", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        InternalUpdate internalUpdate = new InternalUpdate(update);
        long chatId = internalUpdate.getChatId();
        Service currentService = SERVICE_BY_CHAT_ID_CACHE.getUnchecked(chatId);
        Service nextService = currentService.processUpdate(internalUpdate);
        if (currentService != nextService) {
            SERVICE_BY_CHAT_ID_CACHE.put(chatId, nextService);
        }
    }

    @Override
    public String getBotUsername() {
        return MyProperties.getTelegramBotName();
    }

    @Override
    public String getBotToken() {
        return MyProperties.getTelegramBotToken();
    }
}
