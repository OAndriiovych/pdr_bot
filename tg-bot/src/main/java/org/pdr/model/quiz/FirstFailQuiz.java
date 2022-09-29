package org.pdr.model.quiz;

import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.Question;
import org.pdr.repository.QuestionCache;
import org.pdr.repository.QuestionRepository;

import java.util.LinkedList;
import java.util.List;

public class FirstFailQuiz {
    private static final QuestionRepository repository = new QuestionCache();
    private int countCorrectAnswer = 0;
    private boolean isEnd = false;

    private int lastMessageId = 0;

    public List<MessageI> processAnswer(InternalUpdate callbackQuery) {
        List<MessageI> messageIList = new LinkedList<>();
        if (!callbackQuery.isCallBack() || callbackQuery.getCallBackMessageId() != lastMessageId) {
            return messageIList;
        }
        if (Boolean.parseBoolean(callbackQuery.getCallbackData())) {
            messageIList.add(new TextMessage("✅ Відповідь правильна ✅"));
            countCorrectAnswer++;
            messageIList.add(sendQuestion());
        } else {
            isEnd = true;
            messageIList.add(new TextMessage("Відповідь хуйова ,правильна відповідь - доступна по преміум підписці"));
            messageIList.add(new TextMessage("Тест закінчено"));
            messageIList.add(new TextMessage("ваш результат + " + countCorrectAnswer));
            //#TODO рейтинг
            messageIList.add(new TextMessage("подивитись на рейтинг"));
        }
        return messageIList;
    }

    public MessageI sendQuestion() {
        Question randomQuestion = repository.getRandomQuestion();
        return randomQuestion.createMessage();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setLastMessageId(InternalExecuteMessage message) {
        this.lastMessageId = message.getMessageId();
    }
}
