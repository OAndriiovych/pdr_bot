package org.pdr.repository;

import org.pdr.entity.Question;

import java.util.Queue;

public interface QuestionRepository {

    Queue<Question> getQuestionsListWithSize(int size);

    Queue<Question> getQuestionsListWithSizeAndSubject(int size, double theme);
}
