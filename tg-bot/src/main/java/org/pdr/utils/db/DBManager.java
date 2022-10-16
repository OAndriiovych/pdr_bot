package org.pdr.utils.db;

import org.pdr.utils.MyProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final MyProperties MY_PROPERTIES = MyProperties.getInstance();

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(MY_PROPERTIES.getDBUrl(), MY_PROPERTIES.getDBUserName(),
                MY_PROPERTIES.getDBPass());
    }
}
