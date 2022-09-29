package org.pdr.model.quiz;

import org.junit.jupiter.api.Test;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.entity.Question;
import org.pdr.templates.messages.executed.SimpleExecuteMessage;
import org.pdr.templates.messages.updates.SimpleCallBackUpdate;
import org.pdr.templates.question.TestQuestion;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class QuizTest {

    @Test
    void successProcessCallbackQuery() {
        LinkedList<Question> list = new LinkedList<>();
        SimpleImpQuiz quiz = new SimpleImpQuiz(list);
        SimpleCallBackUpdate update = new SimpleCallBackUpdate();
        SimpleExecuteMessage executeMessage = new SimpleExecuteMessage();
        quiz.registrateNewMessageCallback(List.of(executeMessage));

        int count = 3;
        for (int i = 0; i < count; i++) {
            list.add(new TestQuestion());
            quiz.processCallbackQuery(new InternalUpdate(update));
            assertFalse(quiz.isEnd());
            list.poll();
            executeMessage.setMessageId(i);
            update.setMessageId(i);

            quiz.registrateNewMessageCallback(List.of(executeMessage));
        }

        assertTrue(quiz.isEnd());
        assertEquals(count, quiz.count);
    }

    @Test
    void processCallbackQueryInCorrectMessage() {
        LinkedList<Question> list = new LinkedList<>();
        SimpleImpQuiz quiz = new SimpleImpQuiz(list);
        SimpleCallBackUpdate update = new SimpleCallBackUpdate();
        SimpleExecuteMessage executeMessage = new SimpleExecuteMessage();
        quiz.registrateNewMessageCallback(List.of(executeMessage));

        int count = 3;
        for (int i = 0; i < count; i++) {
            list.add(new TestQuestion());
            if (i == 2) {
                update.setMessageId(-1);
            }
            quiz.processCallbackQuery(new InternalUpdate(update));
            assertFalse(quiz.isEnd());
            list.poll();
            executeMessage.setMessageId(i);
            update.setMessageId(i);

            quiz.registrateNewMessageCallback(List.of(executeMessage));
        }

        assertTrue(quiz.isEnd());
        assertEquals(count - 1, quiz.count);
    }

    class SimpleImpQuiz extends Quiz {
        int count = 0;

        SimpleImpQuiz(Queue<Question> queueOfQuestion) {
            super(queueOfQuestion);
        }

        @Override
        protected List<MessageI> processAnswer(InternalUpdate callbackQuery) {
            count++;
            return null;
        }

        @Override
        protected String getResult() {
            return null;
        }
    }
}