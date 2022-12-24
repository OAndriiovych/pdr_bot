package org.pdr.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.pdr.adatpers.ChatSenderI;
import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);
    @Autowired
    private ApplicationContext appContext;
    private ChatSenderI CHAT_SENDER; // only service  Layer can send messages
    private final Map<EnumOfServices, Service> serviceByEnum = new HashMap<>();
    private final LoadingCache<Long, EnumOfServices> SERVICE_BY_CHAT_ID_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(7, TimeUnit.DAYS).build(
                    new CacheLoader<>() {
                        @Override
                        public EnumOfServices load(Long chatId) {
                            logger.info("new User {}", chatId);
                            CHAT_SENDER.execute(new TextMessage("Вітаю в нашому чат боті").setChatId((long) chatId));
                            return EnumOfServices.MAIN_MANU;
                        }
                    });

    @PostConstruct
    private void postConstruct() {
        for (EnumOfServices i : EnumOfServices.values()) {
            serviceByEnum.put(i, appContext.getBean(i.enumService));
        }
    }

    public void setCHAT_SENDER(ChatSenderI CHAT_SENDER) {
        this.CHAT_SENDER = CHAT_SENDER;
    }

    private EnumOfServices processUpdate(InternalUpdate internalUpdate, EnumOfServices currentService) {
        Service service = serviceByEnum.get(currentService);
        Response response = service.processUpdate(internalUpdate);
        long chatId = internalUpdate.getChatId();
        List<InternalExecuteMessage> execute = CHAT_SENDER.execute(response.getMessageIList(), chatId);
        response.getCallback().accept(execute);
        EnumOfServices nextServ = response.getNextServ();
        if (response.isSendDefaultMessage()) {
            Service nextServices = serviceByEnum.get(nextServ);
            CHAT_SENDER.execute(nextServices.getDefaultMessage().setChatId(chatId));
        }
        return nextServ;
    }

    public void onUpdateReceived(Update update) {
        InternalUpdate internalUpdate = new InternalUpdate(update);
        long chatId = internalUpdate.getChatId();
        EnumOfServices currentService = SERVICE_BY_CHAT_ID_CACHE.getUnchecked(chatId);
        EnumOfServices nextService = processUpdate(internalUpdate, currentService);
        if (currentService != nextService) {
            SERVICE_BY_CHAT_ID_CACHE.put(chatId, nextService);
        }
    }
}
