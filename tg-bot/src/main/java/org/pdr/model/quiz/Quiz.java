package org.pdr.model.quiz;

import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class Quiz {
    private final Queue<Question> queueOfQuestion;
    private int lastMessageId = -1;

    Quiz(Queue<Question> queueOfQuestion) {
        this.queueOfQuestion = queueOfQuestion;
    }

    protected List<MessageI> processAnswer(InternalUpdate callbackQuery) {
        return new ArrayList<>();
    }

    protected abstract String getResult();

    public boolean isEnd() {
        return queueOfQuestion.isEmpty();
    }

    public MessageI createNextMessage() {
        return queueOfQuestion.poll().createMessage();
    }

    public final List<MessageI> processCallbackQuery(InternalUpdate internalUpdate) {
        List<MessageI> outPutMessages = processAnswer(internalUpdate);
        if (isEnd()) {
            outPutMessages.add(new TextMessage(getResult()));
        } else {
            outPutMessages.add(createNextMessage());
        }
        return outPutMessages;
    }

    public void registrateNewMessageCallback(List<InternalExecuteMessage> messages) {
        messages.forEach(this::setLastMessageId);
    }

    private void setLastMessageId(InternalExecuteMessage internalUpdate) {
        this.lastMessageId = internalUpdate.getMessageId();
    }

    public boolean isValidMassage(InternalUpdate internalUpdate) {
        return lastMessageId == internalUpdate.getCallBackMessageId();
    }
}
