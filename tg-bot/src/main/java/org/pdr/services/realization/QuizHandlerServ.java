package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.model.quiz.Quiz;
import org.pdr.repository.QuizRepository;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizHandlerServ extends Service {
    private static final String SAVE_QUESTION = "Зберегти питання";
    private static final String END_OF_TEST = "закінчити тест";

    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());
    private static final QuizRepository quizRepository = new QuizRepository();

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(SAVE_QUESTION);
        firstRow.add(END_OF_TEST);
        return buttons;
    }

    @Override
    protected Response processTextMessage(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.QUIZ_HANDLER);
        String userAnswer = internalUpdate.getMessageText();
        switch (userAnswer) {
            case QuizCreatorServ.READY:
                long chatId = internalUpdate.getChatId();
                Quiz quiz = quizRepository.getByChatId(chatId);
                response.addMessage(new TextMessage("Починаємо тест").setButtons(listOfCommands));
                response.addMessage(quiz.createNextMessage());
                response.setCallback(quiz::registrateNewMessageCallback);
                response.setSendDefaultMessage(false);
                break;
            case SAVE_QUESTION:
                //#TODO
                break;
            case END_OF_TEST:
                response.setNextServ(EnumOfServices.MAIN_MANU);
                break;
            default:
                break;
        }
        return response;
    }

    @Override
    protected Response processCallbackQuery(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.QUIZ_HANDLER);
        long chatId = internalUpdate.getChatId();
        Quiz quiz = quizRepository.getByChatId(chatId);
        if (quiz == null) {
            return response;
        }
        if (quiz.isEnd()) {
            quizRepository.removeQuiz(chatId);
            response.setNextServ(EnumOfServices.MAIN_MANU);
        } else if (quiz.isValidMassage(internalUpdate)) {
            response.addMessage(quiz.processCallbackQuery(internalUpdate));
            response.setCallback(quiz::registrateNewMessageCallback);
            response.setSendDefaultMessage(false);
        } else {
            response.setSendDefaultMessage(false);
        }
        return response;
    }

    @Override
    protected MessageI getDefaultMessage() {
        throw new UnsupportedOperationException();
    }
}
