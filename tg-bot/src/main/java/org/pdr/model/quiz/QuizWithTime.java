package org.pdr.model.quiz;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class QuizWithTime extends QuizWithMarks {

    private final long totalTime;
    private final long startTime;
    private boolean isEnd = false;

    public QuizWithTime(Queue<Question> queueOfQuestion, int numberOfAttempts) {
        super(queueOfQuestion, numberOfAttempts);
        startTime = System.currentTimeMillis();
        totalTime = TimeUnit.MINUTES.toMillis(queueOfQuestion.size());
    }

    @Override
    public boolean isEnd() {
        return isEnd || super.isEnd();
    }

    @Override
    protected List<MessageI> processAnswer(InternalUpdate callbackQuery) {
        return processAnswerWithTime(callbackQuery, System.currentTimeMillis());
    }

    protected List<MessageI> processAnswerWithTime(InternalUpdate callbackQuery, long currentTime) {
        List<MessageI> messageIS;
        long currentTimeFromStartQuiz = currentTime - startTime;
        if (currentTimeFromStartQuiz > totalTime) {
            isEnd = true;
            messageIS = new ArrayList<>();
            messageIS.add(new TextMessage("Відповідь не враховано, час вийшов"));
        } else {
            messageIS = super.processAnswer(callbackQuery);
            if (!super.isEnd()) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime - currentTimeFromStartQuiz) + 1;
                messageIS.add(new TextMessage("Вам лишилось " + minutes + " хв."));
            }
        }
        return messageIS;
    }
}
