package org.pdr.model.quiz;

import org.junit.jupiter.api.Test;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.entity.Question;
import org.pdr.templates.messages.updates.TrueAnswerCallBackUpdate;
import org.pdr.templates.question.TestQuestion;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuizWithTimeIntegrationTest extends QuizWithTimeTest{
    @Test
    void successProcessAllAnswer() {
        LinkedList<Question> list = createTestQuestionList();
        QuizWithTime sut = new QuizWithTime(list, 2);
        InternalUpdate trueAnswer = new InternalUpdate(new TrueAnswerCallBackUpdate());

        while (!sut.isEnd()) {
            sut.processAnswer(trueAnswer);
            sut.createNextMessage();
        }

        assertEquals(String.format("%.2f", 100.0) + "%", sut.getResult());
    }

    @Test
    void spendAllTimeOnHalfQuestionsWithTrueAnswers() {
        LinkedList<Question> list = new LinkedList<>();
        int i1 = 4;
        for (int i = 0; i < i1; i++) {
            list.add(new TestQuestion());
        }

        QuizWithTime sut = new QuizWithTime(list, 2);

        for (int i = 0; i < i1; i++) {
            long currentTime = System.currentTimeMillis();
            if (i == 2) {
                currentTime += TimeUnit.MINUTES.toMillis(i1) + 100;
            }
            sut.processAnswerWithTime(new InternalUpdate(new TrueAnswerCallBackUpdate()), currentTime);
            if (i == 2) {
                assertTrue(sut.isEnd());
            }
            sut.createNextMessage();
        }

        assertEquals(String.format("%.2f", 50.0) + "%", sut.getResult());
    }
}
