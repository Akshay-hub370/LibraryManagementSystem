package com.library.dao;

import com.library.model.Book;
import java.util.List;

public class BookDAOTest {

    public static void main(String[] args) {

        BookDAO bookDAO = new BookDAO();

        // Create a test book
        Book book = new Book();

        book.setIsbn("9780134685991");
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setCategory("Programming");
        book.setPublisher("Addison-Wesley");
        book.setPublicationYear(2018);
        book.setTotalCopies(5);
        book.setAvailableCopies(5);
        book.setShelf("A-01");

        // Add book
        boolean added = bookDAO.addBook(book);

        System.out.println("Book Added: " + added);

        // Retrieve all books
        List<Book> books = bookDAO.getAllBooks();

        System.out.println();
        System.out.println("===== BOOKS FROM DATABASE =====");

        for (Book b : books) {

            System.out.println(
                b.getId() + " | "
                + b.getTitle() + " | "
                + b.getAuthor() + " | "
                + b.getCategory()
            );
        }

        System.out.println("===============================");
        System.out.println("Total Books: " + books.size());
    }
}