package org.pdr.services.realization;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.User;
import org.pdr.model.quiz.Quiz;
import org.pdr.model.quiz.QuizBuilder;
import org.pdr.repository.QuestionCacheDB;
import org.pdr.repository.QuestionRepository;
import org.pdr.repository.QuizRepository;
import org.pdr.services.EnumOfServices;
import org.pdr.services.Response;
import org.pdr.services.Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class QuizCreatorServ extends Service {

    public static final String READY = "Готовий";
    private static final QuestionRepository questionRepository = new QuestionCacheDB();
    private static final QuizRepository quizRepository = new QuizRepository();
    private static final QuizBuilder quizBuilder = new QuizBuilder();
    private static final String REAL_TEST = "реальний тест";
    private static final String FIRST_FAIL = "First fail";
    private static final String BY_THEMES = "По темам";
    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());
    private static final List<List<String>> readyCommand = Collections.unmodifiableList(createReadyCommand());

    private static List<List<String>> createReadyCommand() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(READY);
        return buttons;
    }

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(REAL_TEST);
        firstRow.add(FIRST_FAIL);
        List<String> sectRow = new ArrayList<>();
        buttons.add(sectRow);
        sectRow.add(BY_THEMES);
        return buttons;
    }

    @Override
    protected Response processTextMessage(InternalUpdate internalUpdate) {
        Response response;
        try {
            response = tryToProcessUserAnswerAsTheme(internalUpdate);
        } catch (NumberFormatException ignore) {
            response = processCommandAnswer(internalUpdate);
        }
        return response;
    }

    private Response tryToProcessUserAnswerAsTheme(InternalUpdate internalUpdate) throws NumberFormatException {
        Response response = new Response(EnumOfServices.QUIZ_CREATOR);
        String userAnswer = internalUpdate.getMessageText();
        long chatId = internalUpdate.getChatId();
        Double aDouble = validateThemeNumber(userAnswer.replace(",", "."));
        if (aDouble == null) {
            response.addMessage(new TextMessage("Не можу знайти номер"));
            response.addMessage(new TextMessage("спробуйте у форматі 1.23"));
            response.setSendDefaultMessage(false);
        } else {
            Quiz realTest = quizBuilder.createTestByTheme(aDouble);
            quizRepository.saveQuiz(chatId, realTest);
            addReadyButton(response);
        }
        return response;
    }

    private Response processCommandAnswer(InternalUpdate internalUpdate) {
        Response response = new Response(EnumOfServices.QUIZ_CREATOR);
        String userAnswer = internalUpdate.getMessageText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case REAL_TEST:
                Quiz realTest = quizBuilder.createRalTest();
                quizRepository.saveQuiz(chatId, realTest);
                addReadyButton(response);
                break;
            case FIRST_FAIL:
                User user = internalUpdate.getUser();
                if (user.isPrem()) {
                    response = FirstFailServ.sendFirstQuestion(chatId);
                } else {
                    response.addMessage(new TextMessage("сорі тільки по підписці)"));
                }
                break;
            case BY_THEMES:
                response.addMessage(new TextMessage("Надішліть номер теми"));
                response.addMessage(new TextMessage(questionRepository.getAllTheme()));
                break;
            default:
                response.addMessage(new TextMessage("Не зрозумів тебе"));
                break;
        }
        return response;
    }

    private Double validateThemeNumber(String userAnswer) {
        double theme = Double.parseDouble(userAnswer);
        return questionRepository.isThemeValid(theme) ? theme : null;
    }

    @Override
    protected MessageI getDefaultMessage() {
        return new TextMessage("Вибери щось").setButtons(listOfCommands);
    }

    public void addReadyButton(Response response) {
        response.setNextServ(EnumOfServices.QUIZ_HANDLER);
        response.addMessage(new TextMessage("Готовий?").setButtons(readyCommand));
        response.setSendDefaultMessage(false);
    }
}
