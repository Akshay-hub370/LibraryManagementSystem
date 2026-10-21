
package com.library.ui;

import com.library.config.DBConnection;
import com.library.dao.BookReturnDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReturnBookPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable issueTable;
    private final BookReturnDAO returnDAO = new BookReturnDAO();

    public ReturnBookPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(
                new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Return Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel(
                "View issued books and process returns"
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {
            "Issue ID",
            "Member Code",
            "Member Name",
            "Book Title",
            "Issue Date",
            "Due Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        issueTable = new JTable(tableModel);
        issueTable.setRowHeight(30);
        issueTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        issueTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(issueTable);
        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        );

        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5)
        );
        buttonPanel.setOpaque(false);

        JButton refreshButton = new JButton("Refresh");
        JButton returnButton = new JButton("RETURN SELECTED BOOK");

        refreshButton.addActionListener(e -> loadIssuedBooks());
        returnButton.addActionListener(e -> returnSelectedBook());

        buttonPanel.add(refreshButton);
        buttonPanel.add(returnButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load actual database records.
        loadIssuedBooks();
    }

    private void loadIssuedBooks() {
        tableModel.setRowCount(0);

        String sql =
                "SELECT bi.id, m.member_code, m.name, b.title, "
                + "bi.issue_date, bi.due_date "
                + "FROM book_issues bi "
                + "JOIN members m ON bi.member_id = m.id "
                + "JOIN books b ON bi.book_id = b.id "
                + "WHERE bi.status = 'ISSUED' "
                + "AND bi.return_date IS NULL "
                + "ORDER BY bi.due_date ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getInt("id"),
                    resultSet.getString("member_code"),
                    resultSet.getString("name"),
                    resultSet.getString("title"),
                    resultSet.getDate("issue_date"),
                    resultSet.getDate("due_date")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load issued books. Check the NetBeans "
                            + "Output window for details.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void returnSelectedBook() {
        int selectedRow = issueTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to return.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow = issueTable.convertRowIndexToModel(selectedRow);
        int issueId = (int) tableModel.getValueAt(modelRow, 0);
        String memberName =
                String.valueOf(tableModel.getValueAt(modelRow, 2));
        String bookTitle =
                String.valueOf(tableModel.getValueAt(modelRow, 3));

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Confirm return of \"" + bookTitle
                        + "\" from " + memberName + "?",
                "Confirm Book Return",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = returnDAO.returnBook(issueId);

        if (success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Book returned successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadIssuedBooks();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Return failed. The issue may already be returned, "
                            + "or a database error occurred. Check "
                            + "the NetBeans Output window.",
                    "Return Failed",
                    JOptionPane.ERROR_MESSAGE
            );
            loadIssuedBooks();
        }
    }
}
