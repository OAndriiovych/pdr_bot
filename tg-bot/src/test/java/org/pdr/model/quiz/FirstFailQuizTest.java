package org.pdr.model.quiz;

import org.junit.jupiter.api.Test;
import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.templates.messages.SimpleMessage;
import org.pdr.templates.messages.updates.FalseAnswerCallBackUpdate;
import org.pdr.templates.messages.updates.SimpleTextUpdate;
import org.pdr.templates.messages.updates.TrueAnswerCallBackUpdate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FirstFailQuizTest {


    @Test
    void successProcessAnswer() {
        FirstFailQuiz quiz = new FirstFailQuiz();
        TrueAnswerCallBackUpdate update = new TrueAnswerCallBackUpdate();
        InternalUpdate internalUpdate = new InternalUpdate(update);
        SimpleMessage message = (SimpleMessage) update.getCallbackQuery().getMessage();
        quiz.setLastMessageId(message.toInternalExecuteMessage());

        List<MessageI> messageIList = quiz.processAnswer(internalUpdate);

        assertEquals(2, messageIList.size());
        assertFalse(quiz.isEnd());
    }

    @Test
    void processAnswerWrongAnswer() {
        FirstFailQuiz quiz = new FirstFailQuiz();
        FalseAnswerCallBackUpdate update = new FalseAnswerCallBackUpdate();
        InternalUpdate internalUpdate = new InternalUpdate(update);
        SimpleMessage message = (SimpleMessage) update.getCallbackQuery().getMessage();
        quiz.setLastMessageId(message.toInternalExecuteMessage());

        List<MessageI> messageIList = quiz.processAnswer(internalUpdate);

        assertNotEquals(2, messageIList.size());
        assertTrue(quiz.isEnd());
    }

    @Test
    void processTextMessage() {
        FirstFailQuiz quiz = new FirstFailQuiz();

        List<MessageI> messageIList = quiz.processAnswer(new InternalUpdate(new SimpleTextUpdate()));

        assertEquals(0, messageIList.size());
        assertFalse(quiz.isEnd());
    }

    @Test
    void processAnswerWithWrongMessageID() {
        FirstFailQuiz quiz = new FirstFailQuiz();
        TrueAnswerCallBackUpdate update = new TrueAnswerCallBackUpdate();
        InternalUpdate internalUpdate = new InternalUpdate(update);
        SimpleMessage message = (SimpleMessage) update.getCallbackQuery().getMessage();
        quiz.setLastMessageId(message.toInternalExecuteMessage());
        message.setMessageId(-1);

        List<MessageI> messageIList = quiz.processAnswer(internalUpdate);

        assertEquals(0, messageIList.size());
        assertFalse(quiz.isEnd());
    }

    @Test
    void checkCountOfCorrectAnswer() {
        FirstFailQuiz quiz = new FirstFailQuiz();
        TrueAnswerCallBackUpdate update = new TrueAnswerCallBackUpdate();
        InternalUpdate internalUpdate = new InternalUpdate(update);
        SimpleMessage message = (SimpleMessage) update.getCallbackQuery().getMessage();
        quiz.setLastMessageId(message.toInternalExecuteMessage());

        int count = 3;
        for (int i = 1; i <= count; i++) {
            quiz.processAnswer(internalUpdate);

            message.setMessageId(i);

            quiz.setLastMessageId(new InternalExecuteMessage(message));
        }
        FalseAnswerCallBackUpdate updateF = new FalseAnswerCallBackUpdate();
        message = (SimpleMessage) updateF.getCallbackQuery().getMessage();
        message.setMessageId(count);

        List<MessageI> messageIList = quiz.processAnswer(new InternalUpdate(updateF));


        assertNotEquals(2, messageIList.size());
        assertEquals(new TextMessage("ваш результат + " + count), messageIList.get(2));
        assertTrue(quiz.isEnd());
    }
}