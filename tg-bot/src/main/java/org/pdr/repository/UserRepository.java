package org.pdr.repository;

import org.pdr.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static final Map<Long, User> repository = new HashMap<>();

    public void save(User newUser) {
        repository.put(newUser.getChatId(), newUser);
    }

    public User getUserByChatId(long chatId) {
        return repository.get(chatId);
    }

    public void deleteUser(User user) {
        repository.remove(user.getChatId());
    }
}


