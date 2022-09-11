package org.pdr.model.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.entity.Question;
import org.pdr.templates.messages.updates.FalseAnswerCallBackUpdate;
import org.pdr.templates.messages.updates.TrueAnswerCallBackUpdate;
import org.pdr.templates.question.TestQuestion;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuizWithMarksTest {
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
        QuizWithMarks sut = new QuizWithMarks(list, 2);

        while (!sut.isEnd()) {
            sut.processAnswer(new InternalUpdate(new TrueAnswerCallBackUpdate()));
            sut.createNextMessage();
        }
        assertEquals(String.format("%.2f", 100.0) + "%", sut.getResult());
    }

    @Test
    void halfTrue() {
        QuizWithMarks sut = new QuizWithMarks(list, 20);

        for (int i = 0; i < COUNT_OF_QUESTION; i++) {
            Update update = i < 10 ? new TrueAnswerCallBackUpdate() : new FalseAnswerCallBackUpdate();
            sut.processAnswer(new InternalUpdate(update));
        }

        assertEquals(String.format("%.2f", 50.0) + "%", sut.getResult());
    }

    @Test
    void failAll() {
        QuizWithMarks sut = new QuizWithMarks(list, 20);

        while (!sut.isEnd()) {
            Update update = new FalseAnswerCallBackUpdate();
            sut.processAnswer(new InternalUpdate(update));
            sut.createNextMessage();
        }

        assertEquals(String.format("%.2f", 0.0) + "%", sut.getResult());
    }

    @Test
    void testNumberOfAttempts() {
        QuizWithMarks sut = new QuizWithMarks(list, 5);

        for (int i = 0; i < COUNT_OF_QUESTION; i++) {
            Update update = i % 2 == 0 ? new TrueAnswerCallBackUpdate() : new FalseAnswerCallBackUpdate();
            sut.processAnswer(new InternalUpdate(update));
        }

        assertEquals(String.format("%.2f", 25.0) + "%", sut.getResult());
    }
}