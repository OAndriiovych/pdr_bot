package org.pdr.utils.db;

import java.sql.*;

//Manager

public class DBManager {

    private static final String PASS = "1488";
    private static final String USER_NAME = "postgres";
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/PDR";

    private Connection connection;
    private Statement statement;

    public DBManager() throws SQLException {
        initConnection();
    }

    public Statement getStatement() {
        return statement;
    }

    public PreparedStatement getPreparedStatement(String sqlRequest){
        try {
            return connection.prepareStatement(sqlRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeStatement() {
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        connection = DriverManager.getConnection(DB_URL, USER_NAME, PASS);
        statement = connection.createStatement();
    }
}
