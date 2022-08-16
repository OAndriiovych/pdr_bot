package org.pdr.model.repository;

import org.pdr.model.Question;

import java.util.Queue;

public interface QuestionRepository {

    Queue<Question> getQuestionsListWithSize(int size);

    Queue<Question> getQuestionsListWithSizeAndSubject(int size, double theme);
}
