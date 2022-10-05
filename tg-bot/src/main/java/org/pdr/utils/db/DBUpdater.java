package org.pdr.utils.db;

import org.pdr.entity.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class DBUpdater {

    private final List<Question> allQuestions = new LinkedList<>();

    public DBManager dbManager;

    public DBUpdater() {
        try {
            dbManager = new DBManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        DBUpdater dbUpdater = new DBUpdater();
        try {
            List<Question> allQuestions1 = dbUpdater.getAllQuestions();
            allQuestions1.stream().forEach(System.out::println);  ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Question> getAllQuestions() throws SQLException {
        Question currentQuestion;
        Statement statement = dbManager.getStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM questions;");
        while (resultSet.next()) {
            currentQuestion = new Question(resultSet.getString(4), resultSet.getInt(6),
                    resultSet.getString(3),resultSet.getInt(5));
            allQuestions.add(currentQuestion);
        }
        return allQuestions;
    }


}
