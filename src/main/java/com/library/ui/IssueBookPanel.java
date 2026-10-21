
package com.library.ui;

import com.library.dao.BookDAO;
import com.library.dao.BookIssueDAO;
import com.library.dao.MemberDAO;
import com.library.model.Book;
import com.library.model.Member;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class IssueBookPanel extends JPanel {

    private final MemberDAO memberDAO = new MemberDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final BookIssueDAO bookIssueDAO = new BookIssueDAO();

    private final List<Member> members = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();

    private JComboBox<String> memberBox;
    private JComboBox<String> bookBox;

    private JLabel memberDetailsLabel;
    private JLabel bookDetailsLabel;
    private JLabel availabilityLabel;

    private JTextField issueDateField;
    private JTextField dueDateField;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public IssueBookPanel() {
        setLayout(new BorderLayout(0, 25));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // =========================
        // HEADER
        // =========================

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(
                new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Issue Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel(
                "Issue a book to a library member"
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // =========================
        // MAIN FORM
        // =========================

        JPanel formPanel = new JPanel();
        formPanel.setLayout(
                new BoxLayout(formPanel, BoxLayout.Y_AXIS)
        );
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                new EmptyBorder(25, 30, 25, 30)
        );

        // =========================
        // MEMBER SECTION
        // =========================

        JLabel memberTitle = new JLabel("1. Select Member");
        memberTitle.setFont(new Font("Arial", Font.BOLD, 18));

        formPanel.add(memberTitle);
        formPanel.add(Box.createVerticalStrut(15));

        JPanel memberRow = new JPanel(
                new BorderLayout(10, 0)
        );
        memberRow.setOpaque(false);

        // Create the dropdown first.
        // Do not load members until all labels have been initialized.
        memberBox = new JComboBox<>();
        memberBox.setFont(new Font("Arial", Font.PLAIN, 14));

        memberDetailsLabel = new JLabel("Select a member");
        memberDetailsLabel.setForeground(Color.GRAY);

        memberRow.add(memberBox, BorderLayout.CENTER);
        memberRow.add(memberDetailsLabel, BorderLayout.EAST);

        formPanel.add(memberRow);
        formPanel.add(Box.createVerticalStrut(30));

        // =========================
        // BOOK SECTION
        // =========================

        JLabel bookTitle = new JLabel("2. Select Book");
        bookTitle.setFont(new Font("Arial", Font.BOLD, 18));

        formPanel.add(bookTitle);
        formPanel.add(Box.createVerticalStrut(15));

        JPanel bookRow = new JPanel(
                new BorderLayout(10, 0)
        );
        bookRow.setOpaque(false);

        bookBox = new JComboBox<>();
        bookBox.setFont(new Font("Arial", Font.PLAIN, 14));

        bookDetailsLabel = new JLabel("Select a book");
        bookDetailsLabel.setForeground(Color.GRAY);

        bookRow.add(bookBox, BorderLayout.CENTER);
        bookRow.add(bookDetailsLabel, BorderLayout.EAST);

        formPanel.add(bookRow);
        formPanel.add(Box.createVerticalStrut(12));

        // =========================
        // AVAILABILITY
        // =========================

        availabilityLabel = new JLabel("Available Copies: 0");
        availabilityLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );
        availabilityLabel.setForeground(Color.RED);

        formPanel.add(availabilityLabel);
        formPanel.add(Box.createVerticalStrut(30));

        // =========================
        // ISSUE DETAILS
        // =========================

        JLabel transactionTitle = new JLabel("3. Issue Details");
        transactionTitle.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        formPanel.add(transactionTitle);
        formPanel.add(Box.createVerticalStrut(15));

        JPanel datePanel = new JPanel(
                new GridLayout(2, 2, 15, 10)
        );
        datePanel.setOpaque(false);

        datePanel.add(new JLabel("Issue Date:"));

        issueDateField = new JTextField();
        issueDateField.setText(
                LocalDate.now().format(dateFormatter)
        );
        datePanel.add(issueDateField);

        datePanel.add(new JLabel("Due Date:"));

        dueDateField = new JTextField();
        dueDateField.setText(
                LocalDate.now().plusDays(7).format(dateFormatter)
        );
        datePanel.add(dueDateField);

        formPanel.add(datePanel);
        formPanel.add(Box.createVerticalStrut(35));

        // =========================
        // ISSUE BUTTON
        // =========================

        JButton issueButton = new JButton("ISSUE BOOK");

        UIUtils.styleButton(issueButton, UIUtils.SUCCESS);

        issueButton.setFont(
                new Font("Arial", Font.BOLD, 15)
        );
        issueButton.setForeground(Color.WHITE);
        issueButton.setBackground(new Color(52, 152, 219));
        issueButton.setFocusPainted(false);
        issueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        issueButton.setPreferredSize(new Dimension(180, 45));
        issueButton.setMaximumSize(new Dimension(220, 45));

        issueButton.addActionListener(e -> issueBook());

        formPanel.add(issueButton);

        add(formPanel, BorderLayout.CENTER);

        // =========================
        // COMBOBOX EVENTS
        // =========================

        memberBox.addActionListener(e -> updateMemberDetails());
        bookBox.addActionListener(e -> updateBookDetails());

        // IMPORTANT:
        // Load database records only after every label and
        // component used by these methods has been initialized.
        loadActiveMembers();
        loadAvailableBooks();
    }

    // =====================================================
    // LOAD ACTIVE MEMBERS FROM DATABASE
    // =====================================================

    private void loadActiveMembers() {
        memberBox.removeAllItems();
        members.clear();

        try {
            List<Member> allMembers = memberDAO.getAllMembers();

            for (Member member : allMembers) {
                if ("ACTIVE".equalsIgnoreCase(member.getStatus())) {
                    members.add(member);

                    memberBox.addItem(
                            member.getMemberCode()
                                    + " - "
                                    + member.getName()
                    );
                }
            }

            if (members.isEmpty()) {
                memberDetailsLabel.setText(
                        "No active members found"
                );
            } else {
                updateMemberDetails();
            }

        } catch (Exception ex) {
            memberDetailsLabel.setText(
                    "Could not load members"
            );
            System.err.println("Error loading active members:");
            ex.printStackTrace();
        }
    }

    // =====================================================
    // LOAD BOOKS WITH AVAILABLE COPIES
    // =====================================================

    private void loadAvailableBooks() {
        bookBox.removeAllItems();
        books.clear();

        try {
            List<Book> allBooks = bookDAO.getAllBooks();

            for (Book book : allBooks) {
                if (book.getAvailableCopies() > 0) {
                    books.add(book);

                    bookBox.addItem(
                            book.getIsbn()
                                    + " - "
                                    + book.getTitle()
                    );
                }
            }

            if (books.isEmpty()) {
                bookDetailsLabel.setText(
                        "No books available"
                );
                availabilityLabel.setText(
                        "Available Copies: 0"
                );
                availabilityLabel.setForeground(Color.RED);
            } else {
                updateBookDetails();
            }

        } catch (Exception ex) {
            bookDetailsLabel.setText(
                    "Could not load books"
            );
            availabilityLabel.setText(
                    "Availability unavailable"
            );
            availabilityLabel.setForeground(Color.RED);

            System.err.println("Error loading available books:");
            ex.printStackTrace();
        }
    }

    // =====================================================
    // UPDATE SELECTED MEMBER DETAILS
    // =====================================================

    private void updateMemberDetails() {
        int selected = memberBox.getSelectedIndex();

        if (selected < 0 || selected >= members.size()) {
            memberDetailsLabel.setText("No member selected");
            return;
        }

        Member member = members.get(selected);

        memberDetailsLabel.setText(
                member.getDepartment()
                        + " | "
                        + member.getRollNumber()
        );
    }

    // =====================================================
    // UPDATE SELECTED BOOK DETAILS
    // =====================================================

    private void updateBookDetails() {
        int selected = bookBox.getSelectedIndex();

        if (selected < 0 || selected >= books.size()) {
            bookDetailsLabel.setText("No book selected");
            availabilityLabel.setText("Available Copies: 0");
            availabilityLabel.setForeground(Color.RED);
            return;
        }

        Book book = books.get(selected);

        bookDetailsLabel.setText(
                book.getAuthor()
                        + " | "
                        + book.getCategory()
        );

        availabilityLabel.setText(
                "Available Copies: " + book.getAvailableCopies()
        );

        if (book.getAvailableCopies() > 0) {
            availabilityLabel.setForeground(
                    new Color(39, 174, 96)
            );
        } else {
            availabilityLabel.setForeground(Color.RED);
        }
    }

    // =====================================================
    // ISSUE BOOK
    // =====================================================

    private void issueBook() {
        int selectedMemberIndex = memberBox.getSelectedIndex();
        int selectedBookIndex = bookBox.getSelectedIndex();

        // Validate member selection.
        if (selectedMemberIndex < 0
                || selectedMemberIndex >= members.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an active member.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Validate book selection.
        if (selectedBookIndex < 0
                || selectedBookIndex >= books.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an available book.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Parse and validate dates.
        LocalDate issueDate;
        LocalDate dueDate;

        try {
            issueDate = LocalDate.parse(
                    issueDateField.getText().trim(),
                    dateFormatter
            );

            dueDate = LocalDate.parse(
                    dueDateField.getText().trim(),
                    dateFormatter
            );

            if (dueDate.isBefore(issueDate)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Due date cannot be before issue date.",
                        "Invalid Date",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter dates in DD-MM-YYYY format.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Member member = members.get(selectedMemberIndex);
        Book book = books.get(selectedBookIndex);

        if (book.getAvailableCopies() <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No copies of this book are currently available.",
                    "Book Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );

            loadAvailableBooks();
            return;
        }

        // Ask for confirmation before saving.
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Issue \"" + book.getTitle() + "\" to "
                        + member.getName() + "?\n"
                        + "Issue date: "
                        + issueDate.format(dateFormatter)
                        + "\nDue date: "
                        + dueDate.format(dateFormatter),
                "Confirm Book Issue",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        // Save the issue and update available copies in MySQL.
        boolean success = bookIssueDAO.issueBook(
                member.getId(),
                book.getId(),
                issueDate,
                dueDate
        );

        if (success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Book issued successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Reload the actual database values.
            loadAvailableBooks();
            resetForm();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "The book could not be issued. Check the "
                            + "NetBeans Output window for the "
                            + "database error.",
                    "Issue Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            loadAvailableBooks();
        }
    }

    // =====================================================
    // RESET FORM
    // =====================================================

    private void resetForm() {
        // Do not select index 0 when a dropdown is empty.
        if (memberBox.getItemCount() > 0) {
            memberBox.setSelectedIndex(0);
        }

        if (bookBox.getItemCount() > 0) {
            bookBox.setSelectedIndex(0);
        }

        issueDateField.setText(
                LocalDate.now().format(dateFormatter)
        );

        dueDateField.setText(
                LocalDate.now().plusDays(7).format(dateFormatter)
        );

        updateMemberDetails();
        updateBookDetails();
    }
}
