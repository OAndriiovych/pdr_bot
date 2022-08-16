package org.pdr.model.repository;

import org.pdr.model.quiz.Quiz;

import java.util.HashMap;
import java.util.Map;

public class QuizRepository {

    private static final Map<Long, Quiz> quizByChatId = new HashMap<>();

    public void saveQuiz(long chatId, Quiz ralTest) {
        quizByChatId.put(chatId, ralTest);
    }

    public Quiz getByChatId(long chatId) {
        return quizByChatId.get(chatId);
    }

    public void removeQuiz(long chatId) {
        quizByChatId.remove(chatId);
    }
}
