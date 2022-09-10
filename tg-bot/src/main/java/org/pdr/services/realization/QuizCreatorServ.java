package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.User;
import org.pdr.model.quiz.Quiz;
import org.pdr.model.quiz.QuizBuilder;
import org.pdr.repository.MessageRepository;
import org.pdr.repository.QuizRepository;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizCreatorServ extends Service {
    private static final QuizRepository quizRepository = new QuizRepository();
    private static final MessageRepository messageRepository = new MessageRepository();
    private static final QuizBuilder quizBuilder = new QuizBuilder();
    private static final String REAL_TEST = "реальний тест";
    private static final String FIRST_FAIL = "First fail";
    private static final String ALL_QUESTION3 = "Тести 3";
    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(REAL_TEST);
        firstRow.add(FIRST_FAIL);
        List<String> sectRow = new ArrayList<>();
        buttons.add(sectRow);
        sectRow.add(ALL_QUESTION3);
        return buttons;
    }

    @Override
    protected Response processUpdate(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.QUIZ_CREATOR);
        String userAnswer = internalUpdate.getMessageText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case REAL_TEST:
                Quiz ralTest = quizBuilder.createRalTest();
                quizRepository.saveQuiz(chatId, ralTest);
                response.addMessage(ralTest.createNextMessage());
                response.setCallback(messages -> messages.forEach(execute -> messageRepository.registrateNewMessageId(chatId, execute.getMessageId())));
                response.setNextServ(EnumOfServices.QUIZ_HANDLER);
                response.setSendDefaultMessage(false);
                break;
            case FIRST_FAIL:
                User user = internalUpdate.getUser();
                if (user.isPrem()) {
                    response = FirstFailServ.sendFirstQuestion(chatId);
                } else {
                    response.addMessage(new TextMessage("сорі тільки по підписці)"));
                }
                break;
            default:
                response.addMessage(new TextMessage("Не зрозумів тебе"));
                break;
        }
        return response;
    }

    @Override
    protected MessageI getDefaultMessage() {
        return new TextMessage("Вибери щось").setButtons(listOfCommands);
    }
}
