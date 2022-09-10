package org.pdr.model.quiz;

import org.junit.jupiter.api.Test;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.templates.messages.SimpleCallBackUpdate;
import org.pdr.templates.messages.SimpleMessage;
import org.pdr.templates.messages.SimpleTextUpdate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FirstFailQuizTest {


    @Test
    void successProcessAnswer() {
        FirstFailQuiz quiz = new FirstFailQuiz();
        SimpleCallBackUpdate simpleCallBackUpdate = new SimpleCallBackUpdate();
        simpleCallBackUpdate.getCallbackQuery().setData("true");
        SimpleMessage simpleMessage = new SimpleMessage();
        simpleCallBackUpdate.getCallbackQuery().setMessage(simpleMessage);
        InternalUpdate internalUpdate = new InternalUpdate(simpleCallBackUpdate);
        quiz.setLastMessageId(simpleMessage);

        List<MessageI> messageIList = quiz.processAnswer(internalUpdate);

        assertEquals(2, messageIList.size());
        assertFalse(quiz.isEnd());
    }

    @Test
    void processAnswerWrongAnswer() {
        FirstFailQuiz quiz = new FirstFailQuiz();
        SimpleCallBackUpdate simpleCallBackUpdate = new SimpleCallBackUpdate();
        simpleCallBackUpdate.getCallbackQuery().setData("");
        SimpleMessage simpleMessage = new SimpleMessage();
        simpleCallBackUpdate.getCallbackQuery().setMessage(simpleMessage);
        InternalUpdate internalUpdate = new InternalUpdate(simpleCallBackUpdate);
        quiz.setLastMessageId(simpleMessage);

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
        SimpleCallBackUpdate simpleCallBackUpdate = new SimpleCallBackUpdate();
        simpleCallBackUpdate.getCallbackQuery().setData("");
        SimpleMessage simpleMessage = new SimpleMessage();
        simpleCallBackUpdate.getCallbackQuery().setMessage(simpleMessage);
        InternalUpdate internalUpdate = new InternalUpdate(simpleCallBackUpdate);
        quiz.setLastMessageId(simpleMessage);
        simpleMessage.setMessageId(-1);

        List<MessageI> messageIList = quiz.processAnswer(internalUpdate);

        assertEquals(0, messageIList.size());
        assertFalse(quiz.isEnd());
    }
}