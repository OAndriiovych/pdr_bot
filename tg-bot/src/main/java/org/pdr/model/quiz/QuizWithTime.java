package org.pdr.model.quiz;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.Question;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class QuizWithTime extends QuizWithMarks {

    private final long totalTime;
    private final long startTime;
    private long currentTime;

    public QuizWithTime(Queue<Question> queueOfQuestion, int numberOfAttempts) {
        super(queueOfQuestion, numberOfAttempts);
        startTime = System.currentTimeMillis();
        totalTime = TimeUnit.MINUTES.toMillis(queueOfQuestion.size());
    }

    @Override
    public boolean isEnd() {
        return super.isEnd() || isTimeOut();
    }

    @Override
    protected List<MessageI> processAnswer(InternalUpdate callbackQuery) {
        currentTime = System.currentTimeMillis();
        List<MessageI> messageIS = super.processAnswer(callbackQuery);
        if (isTimeOut()) {
            messageIS.add(new TextMessage("Відповідь не враховано, час вийшов"));
        } else if (!super.isEnd()) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime + (startTime - currentTime)) + 1;
            messageIS.add(new TextMessage("Вам лишилось " + minutes + " хв."));
        }
        return messageIS;
    }

    public boolean isTimeOut() {
        return (currentTime - startTime) > totalTime;
    }
}
