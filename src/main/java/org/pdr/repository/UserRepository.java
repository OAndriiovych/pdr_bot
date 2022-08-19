package org.pdr.repository;

import org.pdr.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static final Map<Long, User> repository = new HashMap<>();

    public void save(User newUser) {
        if (newUser.chatId == null) {
            throw new RuntimeException("chat id can not be null");
        }
        repository.put(newUser.chatId, newUser);
    }

    public User getUserByChatId(long chatId) {
        return repository.get(chatId);
    }
}


