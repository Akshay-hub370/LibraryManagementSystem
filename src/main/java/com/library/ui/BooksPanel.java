
package com.library.ui;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BooksPanel extends JPanel {

    private JTable booksTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;

    private JButton searchButton;
    private JButton refreshButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    private BookDAO bookDAO;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BooksPanel() {

        bookDAO = new BookDAO();

        setLayout(new BorderLayout(10, 10));

        // =================================================
        // TOP PANEL
        // =================================================

        JPanel topPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        // =================================================
        // HEADING
        // =================================================

        JLabel heading =
                new JLabel("Books");

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        topPanel.add(
                heading,
                BorderLayout.WEST
        );

        // =================================================
        // SEARCH + BUTTON PANEL
        // =================================================

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        searchField =
                new JTextField(20);

        searchButton =
                new JButton("Search");

        refreshButton =
                new JButton("Refresh");

        addButton =
                new JButton("Add Book");

        editButton =
                new JButton("Edit");

        deleteButton =
                new JButton("Delete");

        searchPanel.add(
                new JLabel("Search:")
        );

        searchPanel.add(
                searchField
        );

        searchPanel.add(
                searchButton
        );

        searchPanel.add(
                refreshButton
        );

        searchPanel.add(
                addButton
        );

        searchPanel.add(
                editButton
        );

        searchPanel.add(
                deleteButton
        );

        topPanel.add(
                searchPanel,
                BorderLayout.EAST
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // =================================================
        // TABLE
        // =================================================

        String[] columns = {

                "Book ID",
                "ISBN",
                "Title",
                "Author",
                "Category",
                "Total Copies",
                "Available",
                "Shelf"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        booksTable =
                new JTable(
                        tableModel
                );

        booksTable.setRowHeight(30);

        booksTable
                .getTableHeader()
                .setReorderingAllowed(false);

        booksTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        booksTable
                );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTON ACTIONS
        // =================================================

        addButton.addActionListener(
                e -> openAddBookDialog()
        );

        editButton.addActionListener(
                e -> editSelectedBook()
        );

        deleteButton.addActionListener(
                e -> deleteSelectedBook()
        );

        refreshButton.addActionListener(
                e -> loadBooksFromDatabase()
        );

        searchButton.addActionListener(
                e -> searchBooks()
        );

        searchField.addActionListener(
                e -> searchBooks()
        );

        // =================================================
        // LOAD BOOKS WHEN PANEL OPENS
        // =================================================

        loadBooksFromDatabase();
    }

    // =====================================================
    // LOAD BOOKS FROM DATABASE
    // =====================================================

    private void loadBooksFromDatabase() {

        tableModel.setRowCount(0);

        List<Book> books =
                bookDAO.getAllBooks();

        for (Book book : books) {

            addBookToTable(book);
        }
    }

    // =====================================================
    // ADD BOOK TO TABLE
    // =====================================================

    private void addBookToTable(Book book) {

        tableModel.addRow(
                new Object[]{

                        book.getId(),

                        book.getIsbn(),

                        book.getTitle(),

                        book.getAuthor(),

                        book.getCategory(),

                        book.getTotalCopies(),

                        book.getAvailableCopies(),

                        book.getShelf()
                }
        );
    }

    // =====================================================
    // OPEN ADD BOOK DIALOG
    // =====================================================

    private void openAddBookDialog() {

        JFrame parent =
                (JFrame) SwingUtilities
                        .getWindowAncestor(this);

        AddBookDialog dialog =
                new AddBookDialog(parent);

        dialog.setVisible(true);

        // If book was successfully saved
        if (dialog.isBookSaved()) {

            loadBooksFromDatabase();
        }
    }

    // =====================================================
    // EDIT SELECTED BOOK
    // =====================================================

    private void editSelectedBook() {

        int selectedRow =
                booksTable.getSelectedRow();

        // No book selected
        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =================================================
        // GET BOOK ID FROM TABLE
        // =================================================

        int bookId =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        // =================================================
        // GET COMPLETE BOOK FROM DATABASE
        // =================================================

        List<Book> books =
                bookDAO.getAllBooks();

        Book selectedBook = null;

        for (Book book : books) {

            if (book.getId() == bookId) {

                selectedBook = book;

                break;
            }
        }

        // =================================================
        // BOOK NOT FOUND
        // =================================================

        if (selectedBook == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Book could not be found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =================================================
        // OPEN EDIT DIALOG
        // =================================================

        JFrame parent =
                (JFrame) SwingUtilities
                        .getWindowAncestor(this);

        EditBookDialog dialog =
                new EditBookDialog(
                        parent,
                        selectedBook
                );

        dialog.setVisible(true);

        // =================================================
        // REFRESH AFTER UPDATE
        // =================================================

        if (dialog.isBookUpdated()) {

            loadBooksFromDatabase();
        }
    }

    // =====================================================
    // DELETE SELECTED BOOK
    // =====================================================

    private void deleteSelectedBook() {

        int selectedRow =
                booksTable.getSelectedRow();

        // =================================================
        // NO SELECTION
        // =================================================

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =================================================
        // GET BOOK ID
        // =================================================

        int bookId =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        // =================================================
        // GET BOOK TITLE
        // =================================================

        String bookTitle =
                tableModel.getValueAt(
                        selectedRow,
                        2
                ).toString();

        // =================================================
        // CONFIRM DELETE
        // =================================================

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete \""
                                + bookTitle
                                + "\"?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        // =================================================
        // DELETE FROM DATABASE
        // =================================================

        boolean deleted =
                bookDAO.deleteBook(bookId);

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "Book deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Refresh table
            loadBooksFromDatabase();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete book.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // SEARCH BOOKS
    // =====================================================

    private void searchBooks() {

        String searchText =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        // =================================================
        // IF SEARCH IS EMPTY
        // =================================================

        if (searchText.isEmpty()) {

            loadBooksFromDatabase();

            return;
        }

        // =================================================
        // CLEAR CURRENT TABLE
        // =================================================

        tableModel.setRowCount(0);

        // =================================================
        // GET BOOKS FROM DATABASE
        // =================================================

        List<Book> books =
                bookDAO.getAllBooks();

        // =================================================
        // SEARCH
        // =================================================

        for (Book book : books) {

            boolean matches =

                    book.getTitle()
                            .toLowerCase()
                            .contains(searchText)

                    ||

                    book.getAuthor()
                            .toLowerCase()
                            .contains(searchText)

                    ||

                    book.getIsbn()
                            .toLowerCase()
                            .contains(searchText)

                    ||

                    book.getCategory()
                            .toLowerCase()
                            .contains(searchText);

            if (matches) {

                addBookToTable(book);
            }
        }

        // =================================================
        // NO RESULTS
        // =================================================

        if (tableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No books found.",
                    "Search",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}