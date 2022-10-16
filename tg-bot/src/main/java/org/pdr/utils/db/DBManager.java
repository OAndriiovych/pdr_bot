package org.pdr.utils.db;

import org.pdr.utils.MyProperties;

import java.io.IOException;
import java.sql.*;

//Manager

public class DBManager {
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(MyProperties.getDBUrl(), MyProperties.getDBUserName(),
                    MyProperties.getDBPass());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
