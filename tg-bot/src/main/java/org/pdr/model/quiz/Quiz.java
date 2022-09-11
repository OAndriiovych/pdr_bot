package org.pdr.model.quiz;

import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.Question;
import org.pdr.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class Quiz {

    MessageRepository messageRepository = new MessageRepository();
    private final Queue<Question> queueOfQuestion;

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
        List<MessageI> outPutMessages = new ArrayList<>();
        if (messageRepository.checkMessageId(internalUpdate)) {
            outPutMessages = processAnswer(internalUpdate);
            if (isEnd()) {
                outPutMessages.add(new TextMessage(getResult()));
            }
        }
        return outPutMessages;
    }

    public void registrateNewMessageCallback(List<InternalExecuteMessage> messages) {
        messages.forEach(messageRepository::registrateNewMessage);
    }
}
