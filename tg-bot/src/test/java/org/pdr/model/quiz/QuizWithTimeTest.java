package org.pdr.model.quiz;

import org.junit.jupiter.api.Test;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.entity.Question;
import org.pdr.templates.messages.updates.TrueAnswerCallBackUpdate;
import org.pdr.templates.question.TestQuestion;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class QuizWithTimeTest {
    public static final int COUNT_OF_QUESTION = 20;

    LinkedList<Question> createTestQuestionList() {
        LinkedList<Question> list = new LinkedList<>();
        for (int i = 0; i < COUNT_OF_QUESTION; i++) {
            list.add(new TestQuestion());
        }
        return list;
    }

    @Test
    void successProcessAllAnswer() {
        LinkedList<Question> list = createTestQuestionList();
        QuizWithTime sut = new QuizWithTime(list, 2);
        long currentTime = System.currentTimeMillis() + 100;
        InternalUpdate trueAnswer = new InternalUpdate(new TrueAnswerCallBackUpdate());

        sut.processAnswerWithTime(trueAnswer, currentTime);

        assertFalse(sut.isEnd());
    }

    @Test
    void spendAllTimeNoAnswer() {
        LinkedList<Question> list = createTestQuestionList();
        QuizWithTime sut = new QuizWithTime(list, 2);
        long currentTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(list.size()) + 100;
        InternalUpdate trueAnswer = new InternalUpdate(new TrueAnswerCallBackUpdate());

        sut.processAnswerWithTime(trueAnswer, currentTime);

        assertTrue(sut.isEnd());
    }
}