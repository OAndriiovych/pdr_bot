package org.pdr.adatpers.implementation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.services.EnumOfServices;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SessionManager {
    private final LoadingCache<Long, EnumOfServices> SERVICE_BY_CHAT_ID_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(7, TimeUnit.DAYS).build(
                    new CacheLoader<>() {
                        @Override
                        public EnumOfServices load(Long chatId) {
                            return EnumOfServices.FIRST_LOGIN;
                        }
                    });

    public EnumOfServices getSession(InternalUpdate internalUpdate) {
        return SERVICE_BY_CHAT_ID_CACHE.getUnchecked(internalUpdate.getChatId());
    }

    public void saveSession(InternalUpdate internalUpdate, EnumOfServices nextService) {
        SERVICE_BY_CHAT_ID_CACHE.put(internalUpdate.getChatId(), nextService);
    }
}
