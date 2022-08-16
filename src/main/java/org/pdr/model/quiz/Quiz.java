package org.pdr.model.quiz;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.model.Question;
import org.pdr.model.repository.MessageRepository;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

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
        List<MessageI> outPutMessages = new ArrayList<>();
//        updateLastMessage(callbackQuery.getMessage());
        return outPutMessages;
    }

    private void updateLastMessage(Message message) {
//        EditMessageReplyMarkupMessage editMessageReplyMarkupMessage = new ButtonMessageUpdater(message)
//                .showAnswer();
//        sender.execute(editMessageReplyMarkupMessage);
    }

    protected abstract String getResult();

    public boolean isEnd() {
        return queueOfQuestion.isEmpty();
    }

    public MessageI createNextMessage() {
        return queueOfQuestion.poll().createMessage();
    }

    public List<MessageI> processCallbackQuery(InternalUpdate internalUpdate) {
        List<MessageI> outPutMessages = new ArrayList<>();
        if (messageRepository.checkMessageId(internalUpdate.getChatId(),
                internalUpdate.getCallBackMessageId())) {
            outPutMessages = processAnswer(internalUpdate);
            if (isEnd()) {
                outPutMessages.add(new TextMessage(getResult()));
            }
        }
        return outPutMessages;
    }
}
