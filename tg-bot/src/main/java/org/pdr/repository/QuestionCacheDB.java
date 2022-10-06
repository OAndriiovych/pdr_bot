package org.pdr.repository;

import org.pdr.entity.Question;
import org.pdr.utils.db.DBManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionCacheDB implements QuestionRepository {
    private final List<Question> TOTAL_QUESTION_LIST = Collections.unmodifiableList(getQuestionMapByTheme().values()
            .stream().flatMap(Collection::stream).collect(Collectors.toList()));
    private static final Map<Double, Integer> QUESTION_THEME_MAPPING = Collections.unmodifiableMap(getQuestionsMapping());
    private static final Random RANDOM = new Random();


    // Вроді працює
    public static void main(String[] args) {
        Map<Integer, List<Question>> ss = new HashMap<>();
        ss = new QuestionCacheDB().getQuestionMapByTheme();
        System.out.println(new QuestionCacheDB().TOTAL_QUESTION_LIST.size());
    }

    private static Map<Double, Integer> getQuestionsMapping() {
        Map<Double, Integer> result = new HashMap<>();
        try {
            FileInputStream fis = new FileInputStream("tg-bot/src/main/resources/themes.txt");
            Scanner sc = new Scanner(fis);
            int i = 1;
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String substring = s.substring(0, s.indexOf(" "));
                double d = Double.parseDouble(substring.substring(0, substring.length() - 1));
                result.put(d, i);
                i++;
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Queue<Question> getQuestionsListWithSize(int size) {
        return getRandomizedQueue(size,TOTAL_QUESTION_LIST);
    }

    @Override
    public Queue<Question> getQuestionsListWithSizeAndSubject(int size, double theme) {
        return getRandomizedQueue(size,getQuestionMapByTheme().get(QUESTION_THEME_MAPPING.get(theme)));
    }

    @Override
    public Question getRandomQuestion() {
        return TOTAL_QUESTION_LIST.get(RANDOM.nextInt(TOTAL_QUESTION_LIST.size()));
    }

    private Map<Integer, List<Question>> getQuestionMapByTheme() {
        Map<Integer, List<Question>> questionByTheme = new HashMap<>();
        Connection connection = DBManager.getConnection();
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            for (int i = 1; i < 76; i++) {  // Хардкод цифри=( можна взяти як максимальну айдішку з ДБ, або к-сть строк з Themes, або к-сть файлів з джейсонів
                List<Question> questionList = new LinkedList<>();
                resultSet = statement.executeQuery("SELECT * FROM questions WHERE theme_id =" + i);
                while (resultSet.next()) {
                    questionList.add(new Question(resultSet.getString(4), resultSet.getInt(6),
                            resultSet.getString(3), resultSet.getInt(5)));
                }
                questionByTheme.put(i, questionList);

            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return questionByTheme;
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
