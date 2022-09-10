package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.model.quiz.FirstFailQuiz;
import org.pdr.repository.FirstFailQuizRepository;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Service;

public class FirstFailServ extends Service {

    private static final FirstFailQuizRepository firstFailQuizRepository = new FirstFailQuizRepository();

    @Override
    protected Response processUpdate(InternalUpdate internalUpdate) {
        long chatId = internalUpdate.getChatId();
        FirstFailQuiz quiz = firstFailQuizRepository.getModelByChatId(chatId);
        Response response = new Response(EnumOfServices.FIRST_FAIL);
        response.addMessage(quiz.processAnswer(internalUpdate));
        if (quiz.isEnd()) {
            response.setNextServ(EnumOfServices.MAIN_MANU);
            firstFailQuizRepository.delete(chatId);
        } else {
            response.setSendDefaultMessage(false);
        }
        return response;
    }

    @Override
    protected MessageI getDefaultMessage() {
        throw new UnsupportedOperationException();
    }

    public static Response sendFirstQuestion(long chatId) {
        Response response = new Response(EnumOfServices.FIRST_FAIL);
        FirstFailQuiz quiz = new FirstFailQuiz();
        firstFailQuizRepository.save(quiz, chatId);
        response.addMessage(quiz.sendQuestion());
        response.setSendDefaultMessage(false);
        response.setCallback(messages -> messages.forEach(quiz::setLastMessageId));
        return response;
    }
}
