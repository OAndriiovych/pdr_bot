package org.pdr.repository;

import org.pdr.model.quiz.FirstFailQuiz;

import java.util.HashMap;
import java.util.Map;

public class FirstFailQuizRepository {
    private static Map<Long, FirstFailQuiz> repository = new HashMap<>();


    public FirstFailQuiz getModelByChatId(long chatId) {
        return repository.get(chatId);
    }

    public void save(FirstFailQuiz quiz, long chatId) {
        repository.put(chatId,quiz);
    }

    public void delete(long chatId) {
        repository.remove(chatId);
    }
}
