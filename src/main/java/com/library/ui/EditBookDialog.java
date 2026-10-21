package com.library.ui;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditBookDialog extends JDialog {

    private JTextField isbnField;
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<String> categoryBox;
    private JTextField publisherField;
    private JTextField yearField;
    private JTextField copiesField;
    private JTextField shelfField;

    private Book book;

    private BookDAO bookDAO;

    private boolean bookUpdated = false;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EditBookDialog(
            JFrame parent,
            Book book
    ) {

        super(
                parent,
                "Edit Book",
                true
        );

        this.book = book;

        bookDAO = new BookDAO();

        setSize(500, 550);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        // =================================================
        // TITLE
        // =================================================

        JLabel title =
                new JLabel("Edit Book");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // =================================================
        // FORM
        // =================================================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                8,
                                2,
                                10,
                                15
                        )
                );

        formPanel.setBorder(
                new EmptyBorder(
                        25,
                        0,
                        20,
                        0
                )
        );

        // ISBN

        formPanel.add(
                new JLabel("ISBN:")
        );

        isbnField =
                new JTextField();

        formPanel.add(
                isbnField
        );

        // TITLE

        formPanel.add(
                new JLabel("Book Title:")
        );

        titleField =
                new JTextField();

        formPanel.add(
                titleField
        );

        // AUTHOR

        formPanel.add(
                new JLabel("Author:")
        );

        authorField =
                new JTextField();

        formPanel.add(
                authorField
        );

        // CATEGORY

        formPanel.add(
                new JLabel("Category:")
        );

        categoryBox =
                new JComboBox<>(
                        new String[]{
                                "Programming",
                                "Database",
                                "Mathematics",
                                "Science",
                                "Literature",
                                "History",
                                "Management"
                        }
                );

        formPanel.add(
                categoryBox
        );

        // PUBLISHER

        formPanel.add(
                new JLabel("Publisher:")
        );

        publisherField =
                new JTextField();

        formPanel.add(
                publisherField
        );

        // YEAR

        formPanel.add(
                new JLabel("Publication Year:")
        );

        yearField =
                new JTextField();

        formPanel.add(
                yearField
        );

        // COPIES

        formPanel.add(
                new JLabel("Total Copies:")
        );

        copiesField =
                new JTextField();

        formPanel.add(
                copiesField
        );

        // SHELF

        formPanel.add(
                new JLabel("Shelf Number:")
        );

        shelfField =
                new JTextField();

        formPanel.add(
                shelfField
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTONS
        // =================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton cancelButton =
                new JButton("Cancel");

        JButton updateButton =
                new JButton("Update Book");

        buttonPanel.add(
                cancelButton
        );

        buttonPanel.add(
                updateButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =================================================
        // LOAD EXISTING DATA
        // =================================================

        loadBookData();

        // =================================================
        // BUTTON ACTIONS
        // =================================================

        cancelButton.addActionListener(
                e -> dispose()
        );

        updateButton.addActionListener(
                e -> updateBook()
        );

        add(mainPanel);
    }

    // =====================================================
    // LOAD BOOK DATA
    // =====================================================

    private void loadBookData() {

        isbnField.setText(
                book.getIsbn()
        );

        titleField.setText(
                book.getTitle()
        );

        authorField.setText(
                book.getAuthor()
        );

        categoryBox.setSelectedItem(
                book.getCategory()
        );

        publisherField.setText(
                book.getPublisher()
        );

        yearField.setText(
                String.valueOf(
                        book.getPublicationYear()
                )
        );

        copiesField.setText(
                String.valueOf(
                        book.getTotalCopies()
                )
        );

        shelfField.setText(
                book.getShelf()
        );
    }

    // =====================================================
    // UPDATE BOOK
    // =====================================================

    private void updateBook() {

        String isbn =
                isbnField.getText().trim();

        String title =
                titleField.getText().trim();

        String author =
                authorField.getText().trim();

        String publisher =
                publisherField.getText().trim();

        String year =
                yearField.getText().trim();

        String copies =
                copiesField.getText().trim();

        String shelf =
                shelfField.getText().trim();

        // =================================================
        // VALIDATION
        // =================================================

        if (isbn.isEmpty()
                || title.isEmpty()
                || author.isEmpty()
                || publisher.isEmpty()
                || year.isEmpty()
                || copies.isEmpty()
                || shelf.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            int publicationYear =
                    Integer.parseInt(year);

            int totalCopies =
                    Integer.parseInt(copies);

            if (publicationYear <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Publication year must be valid.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (totalCopies <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Total copies must be greater than 0.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // =================================================
            // IMPORTANT
            // =================================================
            // Preserve the current available copies.
            //
            // This is important because later Issue/Return
            // functionality will change availableCopies.
            // =================================================

            int currentAvailableCopies =
                    book.getAvailableCopies();

            int copiesDifference =
                    totalCopies - book.getTotalCopies();

            int newAvailableCopies =
                    currentAvailableCopies
                            + copiesDifference;

            if (newAvailableCopies < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Total copies cannot be less than the copies currently issued.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // =================================================
            // UPDATE BOOK OBJECT
            // =================================================

            book.setIsbn(isbn);

            book.setTitle(title);

            book.setAuthor(author);

            book.setCategory(
                    categoryBox
                            .getSelectedItem()
                            .toString()
            );

            book.setPublisher(publisher);

            book.setPublicationYear(
                    publicationYear
            );

            book.setTotalCopies(
                    totalCopies
            );

            book.setAvailableCopies(
                    newAvailableCopies
            );

            book.setShelf(shelf);

            // =================================================
            // UPDATE DATABASE
            // =================================================

            boolean updated =
                    bookDAO.updateBook(book);

            if (updated) {

                bookUpdated = true;

                JOptionPane.showMessageDialog(
                        this,
                        "Book updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update book in database.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Publication year and total copies must be valid numbers.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // CHECK UPDATE
    // =====================================================

    public boolean isBookUpdated() {

        return bookUpdated;
    }
}