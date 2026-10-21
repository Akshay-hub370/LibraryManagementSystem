
package com.library.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/library_management"
            + "?useSSL=false&serverTimezone=UTC";

    private static final String USER =
            System.getenv("DB_USERNAME");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (USER == null || USER.isBlank()
                || PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "Database credentials are missing. "
                    + "Set DB_USERNAME and DB_PASSWORD "
                    + "environment variables."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println(
                    "Database connected successfully!"
            );
        } catch (SQLException e) {
            System.err.println(
                    "Database connection failed: "
                    + e.getMessage()
            );
        }
    }
}
