package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private final static String USERNAME_KEY = "root";
    private final static String PASSWORD_KEY = "root";
    private final static String URL_KEY = "jdbc:mysql://localhost:3306/my_db";

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    URL_KEY,
                    USERNAME_KEY,
                    PASSWORD_KEY
                    );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}