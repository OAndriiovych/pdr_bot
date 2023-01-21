package org.pdr.services;

import lombok.*;
import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.utils.DataStructure;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
@Builder
@DataStructure
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private EnumOfServices nextServ;

    private boolean sendDefaultMessage = true;
    private Consumer<List<InternalExecuteMessage>> callback;

    @Singular
    private List<MessageI> messages;

    public Response(EnumOfServices defaultService) {
        this.nextServ = defaultService;
        this.messages = new LinkedList<>();
        this.callback = a -> {
        };
    }

    public void addMessage(MessageI messageI) {
        messages.add(messageI);
    }

    public void addMessage(List<MessageI> messageI) {
        messages.addAll(messageI);
    }

    List<MessageI> getMessages() {
        return messages;
    }

    public void executeCallback(List<InternalExecuteMessage> execute) {
        if (callback!=null){
            callback.accept(execute);
        }
    }
}
