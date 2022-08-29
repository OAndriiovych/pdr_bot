package org.pdr.adatpers;

import org.pdr.adatpers.messages.MessageI;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ChatSender extends TelegramLongPollingBot implements ChatSenderI {


    @Override
    public List<Message> execute(List<MessageI> messageI) {
        return messageI.stream().map(this::execute).collect(Collectors.toList());
    }

    @Override
    public Message execute(MessageI message) {
        try {
            return tryToSend(message);
        } catch (TelegramApiException e) {
            //#TODO  add logger
            e.printStackTrace();
            return new Message();
        }
    }

    private Message tryToSend(MessageI message) throws TelegramApiException {
        if (message instanceof SendMessage) {
            return execute((SendMessage) message);
        } else if (message instanceof SendPhoto) {
            return execute((SendPhoto) message);
        } else if (message instanceof EditMessageReplyMarkup) {
            return (Message) execute((EditMessageReplyMarkup) message);
        } else {
            //#TODO  add logger
            throw new UnsupportedOperationException("not implemented yet");
        }
    }
}
