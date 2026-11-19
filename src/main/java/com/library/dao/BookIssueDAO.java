
package com.library.dao;

import com.library.config.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookIssueDAO {

 
    public boolean issueBook(
            int memberId,
            int bookId,
            LocalDate issueDate,
            LocalDate dueDate) {

        if (dueDate.isBefore(issueDate)) {
            System.err.println("Due date cannot precede issue date.");
            return false;
        }

        String checkMemberSql =
                "SELECT status FROM members WHERE id = ?";

        String reduceCopiesSql =
                "UPDATE books SET available_copies = available_copies - 1 "
                + "WHERE id = ? AND available_copies > 0";

        String insertIssueSql =
                "INSERT INTO book_issues "
                + "(member_id, book_id, issue_date, due_date, status) "
                + "VALUES (?, ?, ?, ?, 'ISSUED')";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
              
                try (PreparedStatement statement =
                             connection.prepareStatement(checkMemberSql)) {

                    statement.setInt(1, memberId);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next()
                                || !"ACTIVE".equalsIgnoreCase(
                                        resultSet.getString("status"))) {
                            connection.rollback();
                            System.err.println(
                                    "Member does not exist or is inactive.");
                            return false;
                        }
                    }
                }

                
                try (PreparedStatement statement =
                             connection.prepareStatement(reduceCopiesSql)) {

                    statement.setInt(1, bookId);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        System.err.println(
                                "Book not found or no copies available.");
                        return false;
                    }
                }

               
                try (PreparedStatement statement =
                             connection.prepareStatement(insertIssueSql)) {

                    statement.setInt(1, memberId);
                    statement.setInt(2, bookId);
                    statement.setDate(3, Date.valueOf(issueDate));
                    statement.setDate(4, Date.valueOf(dueDate));

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("ISSUE BOOK DATABASE ERROR:");
            e.printStackTrace();
            return false;
        }
    }
}
