package org.pdr.model.quiz;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.Question;

import java.util.List;
import java.util.Queue;

public class QuizWithMarks extends Quiz {
    private float rightAnswer = 0;
    private final int countOfQuestion;
    private int numberOfAttempts;

    public QuizWithMarks(Queue<Question> queueOfQuestion, int numberOfAttempts) {
        super(queueOfQuestion);
        this.countOfQuestion = queueOfQuestion.size();
        this.numberOfAttempts = numberOfAttempts;
    }

    @Override
    public boolean isEnd() {
        return super.isEnd() || numberOfAttempts == 0;
    }

    @Override
    protected List<MessageI> processAnswer(InternalUpdate callbackQuery) {
        List<MessageI> messageIS = super.processAnswer(callbackQuery);
        if (!isEnd()) {
            if (Boolean.parseBoolean(callbackQuery.getCallbackData())) {
                messageIS.add(new TextMessage("✅ Відповідь правильна ✅"));
                float ra = 100f / countOfQuestion;
                rightAnswer += ra;
            } else {
                messageIS.add(new TextMessage("Відповідь хуйова ,правильна відповідь - доступна по преміум підписці"));
                numberOfAttempts--;
                if (numberOfAttempts == 0) {
                    messageIS.add(new TextMessage("Відповідь не правильна, спроби вичерпано"));
                }
            }
        }
        return messageIS;
    }

    @Override
    protected String getResult() {
        return String.format("%.2f", rightAnswer) + "%";
    }

}
