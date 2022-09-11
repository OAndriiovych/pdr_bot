package org.pdr.model.quiz;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
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
    private LinkedList<Question> list = null;

    @BeforeEach
    void preConf() {
        list = new LinkedList<>();
        for (int i = 0; i < COUNT_OF_QUESTION; i++) {
            list.add(new TestQuestion());
        }
    }

    @Test
    void successProcessAnswer() {
        QuizWithTime sut = new QuizWithTime(list, 2);

        while (!sut.isEnd()) {
            sut.processAnswer(new InternalUpdate(new TrueAnswerCallBackUpdate()));
            sut.createNextMessage();
        }

        assertEquals(String.format("%.2f", 100.0) + "%", sut.getResult());
    }

    @Test
    @Ignore
        //to long to work
    void spendAllTimeNoAnswer() {
        list.clear();
        list.add(new TestQuestion());
        QuizWithTime sut = new QuizWithTime(list, 2);

        try {
            Thread.sleep(TimeUnit.MINUTES.toMillis(list.size()) + 100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertTrue(sut.isTimeOut());

        assertEquals(String.format("%.2f", 0.0) + "%", sut.getResult());
    }

    @Test
    @Ignore
        //to long to work
    void spendAllTimeOnHalfQuestionsWithTrueAnswers() {
        list = new LinkedList<>();
        int i1 = 4;
        for (int i = 0; i < i1; i++) {
            list.add(new TestQuestion());
        }

        QuizWithTime sut = new QuizWithTime(list, 2);

        for (int i = 0; i < i1; i++) {
            if (i == 2) {
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(list.size()) + 100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            sut.processAnswer(new InternalUpdate(new TrueAnswerCallBackUpdate()));
            sut.createNextMessage();
        }
        assertEquals(String.format("%.2f", 50.0) + "%", sut.getResult());
    }
}