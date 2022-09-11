package org.pdr.adatpers;

import org.pdr.adatpers.messages.MessageI;

import java.util.List;

public interface ChatSenderI {

    InternalExecuteMessage execute(MessageI messageI);
    List<InternalExecuteMessage> execute(List<MessageI> messageI);
    List<InternalExecuteMessage> execute(List<MessageI> messageI,long chatId);
}
