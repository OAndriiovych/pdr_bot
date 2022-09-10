package org.pdr.model.payment;

import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.User;
import org.pdr.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private final UserRepository userRepository;

    public UserModel() {
        this.userRepository = new UserRepository();
    }

    public boolean isChatIdConnectToUser(long chatId) {
        return false;
    }

    public List<MessageI> sendHistory(long chatId) {
        List<MessageI> mess = new ArrayList<>();
        User user = userRepository.getUserByChatId(chatId);
        if (user.isPrem()) {
            mess.add(new TextMessage("History").setChatId(chatId));
        } else {
            mess.add(new TextMessage("Тільки по платній підписці").setChatId(chatId));
        }
        return mess;
    }

    public MessageI registrarUser(User user) {
        userRepository.save(user);
        return new TextMessage("Ми вас зареєстрували");
    }
}
