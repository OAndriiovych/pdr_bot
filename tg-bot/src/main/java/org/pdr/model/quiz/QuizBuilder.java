package org.pdr.model.quiz;

import org.pdr.entity.Question;
import org.pdr.repository.QuestionCacheDB;
import org.pdr.repository.QuestionRepository;

import java.util.Queue;

public class QuizBuilder {

    private  final QuestionRepository repository = new QuestionCacheDB();
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

    public Quiz createTestByTheme(double theme) {
        Queue<Question> questionsListWithSizeAndSubject = repository.getQuestionsListWithSizeAndSubject(20, theme);
        return new QuizWithMarks(questionsListWithSizeAndSubject, questionsListWithSizeAndSubject.size());
    }
}
