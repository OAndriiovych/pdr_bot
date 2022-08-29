package org.pdr.adatpers;

import org.pdr.adatpers.messages.MessageI;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface ChatSenderI {

    Message execute(MessageI messageI);
    List<Message> execute(List<MessageI> messageI);
}
