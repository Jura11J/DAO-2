package util;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:students.db";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
