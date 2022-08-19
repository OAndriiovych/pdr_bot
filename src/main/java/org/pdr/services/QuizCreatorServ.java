package org.pdr.services;

import org.pdr.Main;
import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.model.quiz.QuizBuilder;
import org.pdr.model.quiz.Quiz;
import org.pdr.repository.MessageRepository;
import org.pdr.repository.QuizRepository;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class QuizCreatorServ extends Service {
    QuizRepository quizRepository = new QuizRepository();
    MessageRepository messageRepository = new MessageRepository();
    QuizBuilder quizBuilder = new QuizBuilder();
    private static final String REAL_TEST = "реальний тест";
    private static final String ALL_QUESTION2 = "Тести 2";
    private static final String ALL_QUESTION3 = "Тести 3";
    private static final List<List<String>> listOfCommands = Collections.unmodifiableList(createListOfCommands());

    private static List<List<String>> createListOfCommands() {
        List<List<String>> buttons = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        buttons.add(firstRow);
        firstRow.add(REAL_TEST);
        firstRow.add(ALL_QUESTION2);
        List<String> sectRow = new ArrayList<>();
        buttons.add(sectRow);
        sectRow.add(ALL_QUESTION3);
        return buttons;
    }

    public static void sendButtons(long chatId) {
        Main.CHAT_SENDER.execute(new TextMessage("Вибери щось").setButtons(listOfCommands).setChatId(chatId));
    }


    @Override
    EnumOfServices processUpdate(InternalUpdate internalUpdate) {
        EnumOfServices nextServ = EnumOfServices.QUIZ_CREATOR;
        String userAnswer = internalUpdate.getMessageText();
        long chatId = internalUpdate.getChatId();
        switch (userAnswer) {
            case REAL_TEST:
                Quiz ralTest = quizBuilder.createRalTest();
                quizRepository.saveQuiz(chatId, ralTest);
                MessageI messageI = ralTest.createNextMessage();
                messageI.setChatId(chatId);
                Message execute = CHAT_SENDER.execute(messageI);
                messageRepository.registrateNewMessageId(chatId, execute.getMessageId());
                nextServ = EnumOfServices.QUIZ_HANDLER;
                break;
            default:
                CHAT_SENDER.execute(new TextMessage("Не зрозумів тебе").setChatId(chatId));
                sendButtons(chatId);
        }
        return nextServ;
    }
}
