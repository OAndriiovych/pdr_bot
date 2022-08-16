package org.pdr.model;

import org.pdr.model.quiz.Quiz;
import org.pdr.model.quiz.QuizWithMarks;
import org.pdr.model.quiz.QuizWithTime;
import org.pdr.model.repository.QuestionCache;
import org.pdr.model.repository.QuestionRepository;

import java.util.Queue;

public class QuizBuilder {

    QuestionRepository repository = new QuestionCache();
    private Integer countOfQuestion = null;
    private Double theme = null;
    private boolean isRealTest = false;
    private boolean isWithTime = false;

    public void setCountOfQuestion(Integer countOfQuestion) {
        this.countOfQuestion = countOfQuestion;
    }

    public void setTheme(Double theme) {
        this.theme = theme;
    }

    public void setRealTest(boolean realTest) {
        isRealTest = realTest;
    }

    public void setWithTime(boolean withTime) {
        isWithTime = withTime;
    }

    public Quiz build() {
        if (countOfQuestion == null) {
            countOfQuestion = 20;
        }
        Queue<Question> questionsList = theme == null ?
                repository.getQuestionsListWithSize(countOfQuestion)
                : repository.getQuestionsListWithSizeAndSubject(countOfQuestion, theme);
        int numberOfAttempts = isRealTest ? 2 : questionsList.size();
        return isWithTime ?
                new QuizWithTime(questionsList, numberOfAttempts)
                : new QuizWithMarks(questionsList, numberOfAttempts);
    }

    public Quiz createRalTest() {
        return new QuizWithTime(repository.getQuestionsListWithSize(20), 2);
    }
}
