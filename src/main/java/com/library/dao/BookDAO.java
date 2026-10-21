package com.library.dao;

import com.library.config.DBConnection;
import com.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // =====================================================
    // GET ALL BOOKS
    // =====================================================

    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books ORDER BY id DESC";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Book book = new Book();

                book.setId(resultSet.getInt("id"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setCategory(resultSet.getString("category"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setPublicationYear(
                        resultSet.getInt("publication_year")
                );
                book.setTotalCopies(
                        resultSet.getInt("total_copies")
                );
                book.setAvailableCopies(
                        resultSet.getInt("available_copies")
                );
                book.setShelf(
                        resultSet.getString("shelf")
                );

                books.add(book);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return books;
    }

    // =====================================================
    // ADD BOOK
    // =====================================================

    public boolean addBook(Book book) {

        String sql = """
                INSERT INTO books
                (
                    isbn,
                    title,
                    author,
                    category,
                    publisher,
                    publication_year,
                    total_copies,
                    available_copies,
                    shelf
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getCategory());
            statement.setString(5, book.getPublisher());
            statement.setInt(6, book.getPublicationYear());
            statement.setInt(7, book.getTotalCopies());
            statement.setInt(8, book.getAvailableCopies());
            statement.setString(9, book.getShelf());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE BOOK
    // =====================================================

    public boolean updateBook(Book book) {

        String sql = """
                UPDATE books
                SET
                    isbn = ?,
                    title = ?,
                    author = ?,
                    category = ?,
                    publisher = ?,
                    publication_year = ?,
                    total_copies = ?,
                    available_copies = ?,
                    shelf = ?
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getCategory());
            statement.setString(5, book.getPublisher());
            statement.setInt(6, book.getPublicationYear());
            statement.setInt(7, book.getTotalCopies());
            statement.setInt(8, book.getAvailableCopies());
            statement.setString(9, book.getShelf());
            statement.setInt(10, book.getId());

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.out.println("UPDATE BOOK ERROR");
            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE BOOK
    // =====================================================

    public boolean deleteBook(int id) {

        String sql =
                "DELETE FROM books WHERE id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}