
package com.library.ui;

import com.library.config.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IssueHistoryPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable historyTable;
    private final JTextField searchField;
    private final JComboBox<String> statusBox;

    public IssueHistoryPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(
                new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Issue History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel(
                "View and search issued and returned books"
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Search and filter controls
        JPanel filterPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );
        filterPanel.setOpaque(false);

        searchField = new JTextField(22);
        searchField.setToolTipText(
                "Search by member code, member name, or book title"
        );

        statusBox = new JComboBox<>(new String[]{
            "ALL", "ISSUED", "RETURNED"
        });

        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusBox);
        filterPanel.add(searchButton);
        filterPanel.add(refreshButton);

        // Table
        String[] columns = {
            "Issue ID",
            "Member Code",
            "Member Name",
            "Book Title",
            "Issue Date",
            "Due Date",
            "Return Date",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(28);
        historyTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        historyTable.setAutoCreateRowSorter(true);
        historyTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        JScrollPane scrollPane = new JScrollPane(historyTable);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Events
        searchButton.addActionListener(e -> loadHistory());
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            statusBox.setSelectedItem("ALL");
            loadHistory();
        });

        searchField.addActionListener(e -> loadHistory());
        statusBox.addActionListener(e -> loadHistory());

        // Initial load
        loadHistory();
    }

    private void loadHistory() {
        tableModel.setRowCount(0);

        String search = searchField.getText().trim();
        String status = (String) statusBox.getSelectedItem();

        StringBuilder sql = new StringBuilder(
                "SELECT bi.id, m.member_code, m.name AS member_name, "
                + "b.title AS book_title, bi.issue_date, bi.due_date, "
                + "bi.return_date, bi.status "
                + "FROM book_issues bi "
                + "JOIN members m ON bi.member_id = m.id "
                + "JOIN books b ON bi.book_id = b.id "
                + "WHERE 1 = 1 "
        );

        if (!search.isEmpty()) {
            sql.append(
                    "AND (m.member_code LIKE ? "
                    + "OR m.name LIKE ? "
                    + "OR b.title LIKE ?) "
            );
        }

        if (status != null && !"ALL".equals(status)) {
            sql.append("AND bi.status = ? ");
        }

        sql.append("ORDER BY bi.id DESC");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            int parameterIndex = 1;

            if (!search.isEmpty()) {
                String pattern = "%" + search + "%";

                statement.setString(parameterIndex++, pattern);
                statement.setString(parameterIndex++, pattern);
                statement.setString(parameterIndex++, pattern);
            }

            if (status != null && !"ALL".equals(status)) {
                statement.setString(parameterIndex, status);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("member_code"),
                        resultSet.getString("member_name"),
                        resultSet.getString("book_title"),
                        resultSet.getDate("issue_date"),
                        resultSet.getDate("due_date"),
                        resultSet.getDate("return_date"),
                        resultSet.getString("status")
                    });
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load issue history. Check the "
                            + "NetBeans Output window for details.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
