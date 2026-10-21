
package com.library.ui;

import com.library.config.DBConnection;
import com.library.dao.DashboardDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsPanel extends JPanel {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    private JLabel totalBooksValue;
    private JLabel issuedBooksValue;
    private JLabel overdueBooksValue;
    private JLabel totalMembersValue;

    private DefaultTableModel tableModel;
    private JTable reportTable;

    public ReportsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ================= HEADER =================

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel subtitleLabel = new JLabel(
                "Library statistics and overdue book report"
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );
        titlePanel.setOpaque(false);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ================= STATISTIC CARDS =================

        JPanel cardsPanel = new JPanel(
                new GridLayout(1, 4, 15, 15)
        );
        cardsPanel.setOpaque(false);

        cardsPanel.add(createCard(
                "Total Book Titles",
                "—",
                new Color(52, 152, 219),
                value -> totalBooksValue = value
        ));

        cardsPanel.add(createCard(
                "Currently Issued",
                "—",
                new Color(46, 204, 113),
                value -> issuedBooksValue = value
        ));

        cardsPanel.add(createCard(
                "Overdue Books",
                "—",
                new Color(231, 76, 60),
                value -> overdueBooksValue = value
        ));

        cardsPanel.add(createCard(
                "Total Members",
                "—",
                new Color(155, 89, 182),
                value -> totalMembersValue = value
        ));

        // ================= OVERDUE TABLE =================

        String[] columns = {
            "Issue ID",
            "Member Code",
            "Member Name",
            "Book Title",
            "Issue Date",
            "Due Date",
            "Days Overdue"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);
        UIUtils.styleTable(reportTable);

        reportTable.setRowHeight(32);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 13));
        reportTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        reportTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        reportTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(reportTable);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 225, 225)
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel tableTitle = new JLabel("Overdue Books");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ================= BUTTONS =================

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        bottomPanel.setOpaque(false);

        JButton refreshButton = new JButton("Refresh");
        UIUtils.styleButton(refreshButton, UIUtils.PRIMARY);

        refreshButton.addActionListener(e -> loadReportData());

        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadReportData();
    }

    // ================= CREATE STATISTIC CARD =================

    private JPanel createCard(
            String title,
            String initialValue,
            Color color,
            java.util.function.Consumer<JLabel> labelConsumer
    ) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 225, 225)
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(initialValue);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);

        labelConsumer.accept(valueLabel);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= LOAD ALL REPORT DATA =================

    private void loadReportData() {
        loadStatistics();
        loadOverdueBooks();
    }

    private void loadStatistics() {
        try {
            DashboardDAO.DashboardStats stats =
                    dashboardDAO.getDashboardStats();

            totalBooksValue.setText(
                    String.valueOf(stats.getTotalBookTitles())
            );

            issuedBooksValue.setText(
                    String.valueOf(stats.getCurrentlyIssued())
            );

            overdueBooksValue.setText(
                    String.valueOf(dashboardDAO.getOverdueCount())
            );

            totalMembersValue.setText(
                    String.valueOf(stats.getTotalMembers())
            );

        } catch (SQLException ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load report statistics. "
                            + "Check the NetBeans Output window.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= LOAD OVERDUE BOOKS =================

    private void loadOverdueBooks() {
        tableModel.setRowCount(0);

        String sql =
                "SELECT bi.id, m.member_code, m.name AS member_name, "
                + "b.title AS book_title, bi.issue_date, bi.due_date, "
                + "DATEDIFF(CURDATE(), bi.due_date) AS days_overdue "
                + "FROM book_issues bi "
                + "JOIN members m ON bi.member_id = m.id "
                + "JOIN books b ON bi.book_id = b.id "
                + "WHERE bi.status = 'ISSUED' "
                + "AND bi.return_date IS NULL "
                + "AND bi.due_date < CURDATE() "
                + "ORDER BY bi.due_date ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getInt("id"),
                    resultSet.getString("member_code"),
                    resultSet.getString("member_name"),
                    resultSet.getString("book_title"),
                    resultSet.getDate("issue_date"),
                    resultSet.getDate("due_date"),
                    resultSet.getInt("days_overdue")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load overdue books. "
                            + "Check the NetBeans Output window.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
