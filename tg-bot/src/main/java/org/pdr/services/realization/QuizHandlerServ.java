package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.model.quiz.Quiz;
import org.pdr.repository.MessageRepository;
import org.pdr.repository.QuizRepository;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Service;

public class QuizHandlerServ extends Service {
    private static final MessageRepository messageRepository = new MessageRepository();
    private static final QuizRepository quizRepository = new QuizRepository();

    @Override
    protected Response processUpdate(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.QUIZ_HANDLER);
        long chatId = internalUpdate.getChatId();
        Quiz quiz = quizRepository.getByChatId(chatId);
        response.addMessage(quiz.processCallbackQuery(internalUpdate));
        if (!quiz.isEnd()) {
            response.processQuizForQuizHandlerServ(quiz);
        } else {
            quizRepository.removeQuiz(chatId);
            messageRepository.removeMessage(chatId);
            response.setNextServ(EnumOfServices.MAIN_MANU);
        }
        return response;
    }

    @Override
    protected MessageI getDefaultMessage() {
        throw new UnsupportedOperationException();
    }
}