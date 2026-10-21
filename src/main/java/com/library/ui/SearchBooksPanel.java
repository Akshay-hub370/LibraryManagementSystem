package com.library.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SearchBooksPanel extends JPanel {

    private JTextField searchField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> availabilityBox;

    private JTable booksTable;
    private DefaultTableModel tableModel;

    private JLabel resultLabel;

    public SearchBooksPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // =================================================
        // HEADER
        // =================================================

        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headerPanel.setOpaque(false);

        JLabel titleLabel =
                new JLabel("Search Books");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Find books and check their availability"
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(subtitleLabel);

        add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =================================================
        // SEARCH AREA
        // =================================================

        JPanel searchArea =
                new JPanel();

        searchArea.setLayout(
                new BoxLayout(
                        searchArea,
                        BoxLayout.Y_AXIS
                )
        );

        searchArea.setBackground(Color.WHITE);

        searchArea.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        // Search row

        JPanel searchRow =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        searchRow.setOpaque(false);

        searchField =
                new JTextField();

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        searchField.setBorder(
                BorderFactory.createTitledBorder(
                        "Search by title, author or ISBN"
                )
        );

        JButton searchButton =
                new JButton("Search");
        UIUtils.styleButton(
        searchButton,
        UIUtils.PRIMARY
);


        searchButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        searchButton.setFocusPainted(false);

        JButton clearButton =
                new JButton("Clear");
        UIUtils.styleButton(
        searchButton,
        UIUtils.PRIMARY
);

UIUtils.styleButton(
        clearButton,
        UIUtils.WARNING
);
        

        clearButton.setFocusPainted(false);

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                5,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        searchRow.add(
                searchField,
                BorderLayout.CENTER
        );

        searchRow.add(
                buttonPanel,
                BorderLayout.EAST
        );

        searchArea.add(searchRow);

        searchArea.add(
                Box.createVerticalStrut(15)
        );

        // =================================================
        // FILTER ROW
        // =================================================

        JPanel filterRow =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        filterRow.setOpaque(false);

        JLabel categoryLabel =
                new JLabel("Category:");

        categoryBox =
                new JComboBox<>(
                        new String[]{
                                "All Categories",
                                "Programming",
                                "Database",
                                "Mathematics",
                                "Science",
                                "Literature",
                                "History",
                                "Management"
                        }
                );

        JLabel availabilityText =
                new JLabel("Availability:");

        availabilityBox =
                new JComboBox<>(
                        new String[]{
                                "All Books",
                                "Available",
                                "Not Available"
                        }
                );

        filterRow.add(categoryLabel);
        filterRow.add(categoryBox);

        filterRow.add(
                Box.createHorizontalStrut(20)
        );

        filterRow.add(availabilityText);
        filterRow.add(availabilityBox);

        searchArea.add(filterRow);

        add(
                searchArea,
                BorderLayout.NORTH
        );

        // =================================================
        // TABLE
        // =================================================

        String[] columns = {
                "ISBN",
                "Book Title",
                "Author",
                "Category",
                "Total",
                "Available",
                "Shelf",
                "Status"
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
                new JTable(tableModel);
        UIUtils.styleTable(booksTable);

        booksTable.setRowHeight(32);

        booksTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        booksTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        booksTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        booksTable
                );

        // =================================================
        // RESULT LABEL
        // =================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setOpaque(false);

        resultLabel =
                new JLabel(
                        "4 books found"
                );

        resultLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        resultLabel.setForeground(Color.GRAY);

        bottomPanel.add(
                resultLabel,
                BorderLayout.WEST
        );

        // =================================================
        // MAIN CONTENT
        // =================================================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(0, 10)
                );

        centerPanel.setOpaque(false);

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        centerPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // SAMPLE DATA
        // =================================================

        loadSampleBooks();

        // =================================================
        // EVENTS
        // =================================================

        searchButton.addActionListener(
                e -> performSearch()
        );

        searchField.addActionListener(
                e -> performSearch()
        );

        categoryBox.addActionListener(
                e -> performSearch()
        );

        availabilityBox.addActionListener(
                e -> performSearch()
        );

        clearButton.addActionListener(
                e -> clearSearch()
        );
    }

    // =================================================
    // SAMPLE BOOK DATA
    // =================================================

    private void loadSampleBooks() {

        addBook(
                "9780134685991",
                "Effective Java",
                "Joshua Bloch",
                "Programming",
                5,
                3,
                "A-01"
        );

        addBook(
                "9780132350884",
                "Clean Code",
                "Robert C. Martin",
                "Programming",
                4,
                4,
                "A-02"
        );

        addBook(
                "9781491950296",
                "Designing Data-Intensive Applications",
                "Martin Kleppmann",
                "Database",
                3,
                3,
                "B-01"
        );

        addBook(
                "9780262033848",
                "Introduction to Algorithms",
                "Thomas Cormen",
                "Programming",
                5,
                5,
                "A-03"
        );

        addBook(
                "9780073523323",
                "Database System Concepts",
                "Abraham Silberschatz",
                "Database",
                4,
                0,
                "B-02"
        );
    }

    private void addBook(
            String isbn,
            String title,
            String author,
            String category,
            int total,
            int available,
            String shelf
    ) {

        String status;

        if (available > 0) {
            status = "AVAILABLE";
        } else {
            status = "NOT AVAILABLE";
        }

        tableModel.addRow(
                new Object[]{
                        isbn,
                        title,
                        author,
                        category,
                        total,
                        available,
                        shelf,
                        status
                }
        );
    }

    // =================================================
    // SEARCH
    // =================================================

    private void performSearch() {

        String searchText =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        String category =
                categoryBox
                        .getSelectedItem()
                        .toString();

        String availability =
                availabilityBox
                        .getSelectedItem()
                        .toString();

        for (int i = 0;
             i < tableModel.getRowCount();
             i++) {

            String title =
                    tableModel
                            .getValueAt(i, 1)
                            .toString()
                            .toLowerCase();

            String author =
                    tableModel
                            .getValueAt(i, 2)
                            .toString()
                            .toLowerCase();

            String isbn =
                    tableModel
                            .getValueAt(i, 0)
                            .toString()
                            .toLowerCase();

            String bookCategory =
                    tableModel
                            .getValueAt(i, 3)
                            .toString();

            int available =
                    Integer.parseInt(
                            tableModel
                                    .getValueAt(i, 5)
                                    .toString()
                    );

            boolean textMatch =
                    searchText.isEmpty()
                    || title.contains(searchText)
                    || author.contains(searchText)
                    || isbn.contains(searchText);

            boolean categoryMatch =
                    category.equals(
                            "All Categories"
                    )
                    || category.equals(
                            bookCategory
                    );

            boolean availabilityMatch;

            if (availability.equals("Available")) {

                availabilityMatch =
                        available > 0;

            } else if (
                    availability.equals(
                            "Not Available"
                    )
            ) {

                availabilityMatch =
                        available == 0;

            } else {

                availabilityMatch = true;
            }

            boolean showRow =
                    textMatch
                    && categoryMatch
                    && availabilityMatch;

            booksTable
                    .getRowSorter();

            // Store matching state
            tableModel.fireTableRowsUpdated(
                    i,
                    i
            );

            booksTable.setRowSelectionAllowed(true);

            if (showRow) {

                booksTable
                        .setRowSelectionInterval(
                                i,
                                i
                        );
            }
        }

        // Show result count

        int count = 0;

        for (int i = 0;
             i < tableModel.getRowCount();
             i++) {

            String title =
                    tableModel
                            .getValueAt(i, 1)
                            .toString()
                            .toLowerCase();

            String author =
                    tableModel
                            .getValueAt(i, 2)
                            .toString()
                            .toLowerCase();

            String isbn =
                    tableModel
                            .getValueAt(i, 0)
                            .toString()
                            .toLowerCase();

            String bookCategory =
                    tableModel
                            .getValueAt(i, 3)
                            .toString();

            int available =
                    Integer.parseInt(
                            tableModel
                                    .getValueAt(i, 5)
                                    .toString()
                    );

            boolean textMatch =
                    searchText.isEmpty()
                    || title.contains(searchText)
                    || author.contains(searchText)
                    || isbn.contains(searchText);

            boolean categoryMatch =
                    category.equals(
                            "All Categories"
                    )
                    || category.equals(
                            bookCategory
                    );

            boolean availabilityMatch;

            if (availability.equals("Available")) {

                availabilityMatch =
                        available > 0;

            } else if (
                    availability.equals(
                            "Not Available"
                    )
            ) {

                availabilityMatch =
                        available == 0;

            } else {

                availabilityMatch = true;
            }

            if (textMatch
                    && categoryMatch
                    && availabilityMatch) {

                count++;
            }
        }

        resultLabel.setText(
                count + " book(s) found"
        );
    }

    // =================================================
    // CLEAR
    // =================================================

    private void clearSearch() {

        searchField.setText("");

        categoryBox.setSelectedIndex(0);

        availabilityBox.setSelectedIndex(0);

        booksTable.clearSelection();

        resultLabel.setText(
                tableModel.getRowCount()
                        + " books found"
        );
    }
}