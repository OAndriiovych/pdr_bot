package org.pdr.model.quiz;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pdr.adatpers.InternalExecuteMessage;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.repository.QuestionRepository;
import org.pdr.templates.messages.SimpleMessage;
import org.pdr.templates.messages.updates.FalseAnswerCallBackUpdate;
import org.pdr.templates.messages.updates.SimpleTextUpdate;
import org.pdr.templates.messages.updates.TrueAnswerCallBackUpdate;
import org.pdr.templates.question.TestQuestion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FirstFailQuizTest {

    @Test
    void successProcessAnswer() {
        QuestionRepository mockQuestionRepository = Mockito.spy(QuestionRepository.class);
        Mockito.when(mockQuestionRepository.getRandomQuestion()).thenReturn(new TestQuestion());
        FirstFailQuiz quiz = new FirstFailQuiz(mockQuestionRepository);
        TrueAnswerCallBackUpdate update = new TrueAnswerCallBackUpdate();
        InternalUpdate internalUpdate = new InternalUpdate(update);
        SimpleMessage message = (SimpleMessage) update.getCallbackQuery().getMessage();
        quiz.setLastMessageId(message.toInternalExecuteMessage());

        quiz.processAnswer(internalUpdate);

        assertFalse(quiz.isEnd());
    }

    @Test
    void processAnswerWrongAnswer() {
        QuestionRepository mockQuestionRepository = Mockito.spy(QuestionRepository.class);
        Mockito.when(mockQuestionRepository.getRandomQuestion()).thenReturn(new TestQuestion());
        FirstFailQuiz quiz = new FirstFailQuiz(mockQuestionRepository);
        FalseAnswerCallBackUpdate update = new FalseAnswerCallBackUpdate();
        InternalUpdate internalUpdate = new InternalUpdate(update);
        SimpleMessage message = (SimpleMessage) update.getCallbackQuery().getMessage();
        quiz.setLastMessageId(message.toInternalExecuteMessage());

        quiz.processAnswer(internalUpdate);

        assertTrue(quiz.isEnd());
    }

    @Test
    void processTextMessage() {
        QuestionRepository mockQuestionRepository = Mockito.spy(QuestionRepository.class);
        Mockito.when(mockQuestionRepository.getRandomQuestion()).thenReturn(new TestQuestion());
        FirstFailQuiz quiz = new FirstFailQuiz(mockQuestionRepository);

        List<MessageI> messageIList = quiz.processAnswer(new InternalUpdate(new SimpleTextUpdate()));

        assertEquals(0, messageIList.size());
        assertFalse(quiz.isEnd());
    }

    @Test
    void processAnswerWithWrongMessageID() {
        QuestionRepository mockQuestionRepository = Mockito.spy(QuestionRepository.class);
        Mockito.when(mockQuestionRepository.getRandomQuestion()).thenReturn(new TestQuestion());
        FirstFailQuiz quiz = new FirstFailQuiz(mockQuestionRepository);
        TrueAnswerCallBackUpdate update = new TrueAnswerCallBackUpdate();
        InternalUpdate internalUpdate = new InternalUpdate(update);
        SimpleMessage message = (SimpleMessage) update.getCallbackQuery().getMessage();
        quiz.setLastMessageId(message.toInternalExecuteMessage());
        message.setMessageId(-1);

        quiz.processAnswer(internalUpdate);

        assertFalse(quiz.isEnd());
    }

    @Test
    void checkCountOfCorrectAnswer() {
        QuestionRepository mockQuestionRepository = Mockito.spy(QuestionRepository.class);
        Mockito.when(mockQuestionRepository.getRandomQuestion()).thenReturn(new TestQuestion());
        FirstFailQuiz quiz = new FirstFailQuiz(mockQuestionRepository);
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


        assertEquals(new TextMessage("ваш результат + " + count), messageIList.get(2));
    }
}