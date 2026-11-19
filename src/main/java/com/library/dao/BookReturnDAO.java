
package com.library.dao;

import com.library.config.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookReturnDAO {

    public boolean returnBook(int issueId) {

        String findIssueSql =
                "SELECT book_id FROM book_issues "
                + "WHERE id = ? AND status = 'ISSUED' "
                + "AND return_date IS NULL FOR UPDATE";

        String updateIssueSql =
                "UPDATE book_issues "
                + "SET return_date = ?, status = 'RETURNED' "
                + "WHERE id = ? AND status = 'ISSUED' "
                + "AND return_date IS NULL";

        String updateBookSql =
                "UPDATE books "
                + "SET available_copies = available_copies + 1 "
                + "WHERE id = ? "
                + "AND available_copies < total_copies";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int bookId;

               
                try (PreparedStatement statement =
                             connection.prepareStatement(findIssueSql)) {

                    statement.setInt(1, issueId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (!resultSet.next()) {
                            connection.rollback();
                            return false;
                        }

                        bookId = resultSet.getInt("book_id");
                    }
                }

               
                try (PreparedStatement statement =
                             connection.prepareStatement(updateBookSql)) {

                    statement.setInt(1, bookId);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                
                try (PreparedStatement statement =
                             connection.prepareStatement(updateIssueSql)) {

                    statement.setDate(
                            1, Date.valueOf(LocalDate.now())
                    );
                    statement.setInt(2, issueId);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException ex) {
                connection.rollback();
                throw ex;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            System.err.println("BOOK RETURN DATABASE ERROR:");
            ex.printStackTrace();
            return false;
        }
    }
}
