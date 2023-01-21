package org.pdr.repository;

import org.pdr.entity.Question;
import org.pdr.utils.MyProperties;
import org.pdr.utils.db.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class QuestionCacheDB implements QuestionRepository {

    private static final String SELECT_FROM_QUESTIONS = "SELECT * FROM questions q ORDER BY theme_id";
    private static final String SELECT_FROM_THEME = "SELECT * FROM theme ORDER BY theme_id";
    private static final Random RANDOM = new Random();
    private static String partOfUrl;
    @Autowired
    private DBManager dbManager;
    @Autowired
    private MyProperties myProperties;
    private Map<Double, List<Question>> questionByThemeId;
    private List<Question> questionList;
    private String textVersionOfListTheme;

    @PostConstruct
    private void load() {
        partOfUrl = myProperties.getBucketURL();
        try (Connection connection = dbManager.getConnection()) {
            Map<Integer, List<Question>> questions = loadQuestion(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_THEME);
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Double, List<Question>> questionByThemeId = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                int theme_id = resultSet.getInt("theme_id");
                String theme = resultSet.getString("caption");
                stringBuilder.append(theme).append("\n");
                String substring = theme.substring(0, theme.indexOf(" "));
                double d = Double.parseDouble(substring.substring(0, substring.length() - 1));
                questionByThemeId.put(d, questions.get(theme_id));
            }
            this.questionByThemeId = questionByThemeId;
            textVersionOfListTheme = stringBuilder.toString();
            questionList = questionByThemeId.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer, List<Question>> loadQuestion(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_QUESTIONS);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<Integer, List<Question>> result = new HashMap<>();
        while (resultSet.next()) {
            int theme_id = resultSet.getInt("theme_id");
            List<Question> orDefault = result.computeIfAbsent(theme_id, ArrayList::new);
            Question question = new Question(resultSet);
            question.setUrl(partOfUrl.concat(question.getUrl()).concat(".jpg"));
            orDefault.add(question);
        }
        return result;
    }

    @Override
    public Queue<Question> getQuestionsListWithSize(int size) {
        return getRandomizedQueue(size, questionList);
    }

    @Override
    public Queue<Question> getQuestionsListWithSizeAndSubject(int size, double theme) {
        return getRandomizedQueue(size, questionByThemeId.get(theme));
    }

    @Override
    public Question getRandomQuestion() {
        return questionList.get(RANDOM.nextInt(questionList.size()));

    }

    @Override
    public String getAllTheme() {
        return textVersionOfListTheme;
    }

    @Override
    public boolean isThemeValid(double theme) {
        return questionByThemeId.containsKey(theme);
    }

    private static Queue<Question> getRandomizedQueue(int size, List<Question> list) {
        Set<Integer> ticketIndicator = new HashSet<>();
        Queue<Question> questions = new LinkedList<>();
        int totalSize = list.size();
        while (questions.size() < size) {
            int a = RANDOM.nextInt(totalSize);
            if (!ticketIndicator.contains(a)) {
                questions.add(list.get(a));
                ticketIndicator.add(a);
            }
        }
        return questions;
    }
}
