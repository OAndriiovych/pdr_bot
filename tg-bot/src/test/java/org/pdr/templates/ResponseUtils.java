package org.pdr.templates;

import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.messages.MessageI;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseUtils {
    public static InternalExecuteMessage messageIToInternalExecuteMessage(MessageI i){
        Message message = new Message();
        return new InternalExecuteMessage(message);
    }

    public static List<InternalExecuteMessage> messageIToInternalExecuteMessage(List<MessageI> messageIList) {
        return messageIList.stream().map(ResponseUtils::messageIToInternalExecuteMessage).collect(Collectors.toList());
    }
}
