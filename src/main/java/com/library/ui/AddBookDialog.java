package com.library.ui;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddBookDialog extends JDialog {

    private JTextField isbnField;
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<String> categoryBox;
    private JTextField publisherField;
    private JTextField yearField;
    private JTextField copiesField;
    private JTextField shelfField;

    private boolean bookSaved = false;

    private BookDAO bookDAO;

   
    public AddBookDialog(JFrame parent) {

        super(
                parent,
                "Add New Book",
                true
        );

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

       
        JLabel title =
                new JLabel("Add New Book");

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


        formPanel.add(
                new JLabel("ISBN:")
        );

        isbnField =
                new JTextField();

        formPanel.add(
                isbnField
        );

        formPanel.add(
                new JLabel("Book Title:")
        );

        titleField =
                new JTextField();

        formPanel.add(
                titleField
        );

      

        formPanel.add(
                new JLabel("Author:")
        );

        authorField =
                new JTextField();

        formPanel.add(
                authorField
        );

        

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

        

        formPanel.add(
                new JLabel("Publisher:")
        );

        publisherField =
                new JTextField();

        formPanel.add(
                publisherField
        );

        

        formPanel.add(
                new JLabel("Publication Year:")
        );

        yearField =
                new JTextField();

        formPanel.add(
                yearField
        );

        

        formPanel.add(
                new JLabel("Total Copies:")
        );

        copiesField =
                new JTextField();

        formPanel.add(
                copiesField
        );

        

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

        
        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton cancelButton =
                new JButton("Cancel");

        JButton saveButton =
                new JButton("Save Book");

        buttonPanel.add(
                cancelButton
        );

        buttonPanel.add(
                saveButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

      
        cancelButton.addActionListener(
                e -> dispose()
        );

      
        saveButton.addActionListener(
                e -> saveBook()
        );

        add(mainPanel);
    }

   
    private void saveBook() {

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

           
            Book book =
                    new Book();

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

            // New book means all copies are available

            book.setAvailableCopies(
                    totalCopies
            );

            book.setShelf(shelf);

           
            boolean saved =
                    bookDAO.addBook(book);

            if (saved) {

                bookSaved = true;

                JOptionPane.showMessageDialog(
                        this,
                        "Book added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to save book to database.",
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

  
    public boolean isBookSaved() {

        return bookSaved;
    }

   
    public Object[] getBookData() {

        String copies =
                copiesField
                        .getText()
                        .trim();

        return new Object[]{

                "B" + System.currentTimeMillis()
                        % 10000,

                isbnField
                        .getText()
                        .trim(),

                titleField
                        .getText()
                        .trim(),

                authorField
                        .getText()
                        .trim(),

                categoryBox
                        .getSelectedItem()
                        .toString(),

                copies,

                copies,

                shelfField
                        .getText()
                        .trim()
        };
    }
}
