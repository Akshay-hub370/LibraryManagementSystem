package com.library.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.library.dao.DashboardDAO;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {

    private JPanel contentPanel;

    // Colors
    private final Color SIDEBAR_COLOR = new Color(35, 47, 62);
    private final Color SIDEBAR_TEXT = Color.WHITE;
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color PRIMARY = new Color(52, 152, 219);

    public MainFrame() {

        setTitle("Library Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Main content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        add(contentPanel, BorderLayout.CENTER);

        showDashboard();
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 700));
sidebar.setMinimumSize(new Dimension(240, 0));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BorderLayout());

        // Logo / title
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR_COLOR);
        logoPanel.setBorder(new EmptyBorder(25, 10, 25, 10));

        JLabel logoLabel = new JLabel(
                "<html><center>LIBRARY<br>MANAGEMENT</center></html>"
        );

        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));

        logoPanel.add(logoLabel);

        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(SIDEBAR_COLOR);
        menuPanel.setLayout(new GridLayout(9, 1, 0, 8));
        menuPanel.setBorder(new EmptyBorder(10, 12, 10, 12));

        JButton dashboardButton = createMenuButton("Dashboard");
        JButton booksButton = createMenuButton("Books");
        JButton membersButton = createMenuButton("Members");
       JButton issueButton = createMenuButton("Issue Book");
JButton returnButton = createMenuButton("Return Book");
JButton historyButton = createMenuButton("Issue History");
JButton searchButton = createMenuButton("Search Books");
        JButton reportsButton = createMenuButton("Reports");
        JButton logoutButton = createMenuButton("Logout");

        menuPanel.add(dashboardButton);
        menuPanel.add(booksButton);
        menuPanel.add(membersButton);
       menuPanel.add(issueButton);
menuPanel.add(returnButton);
menuPanel.add(historyButton);
menuPanel.add(searchButton);
        menuPanel.add(reportsButton);
        menuPanel.add(logoutButton);

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // Navigation
        dashboardButton.addActionListener(e -> showDashboard());

        booksButton.addActionListener(e -> showBooks());
        membersButton.addActionListener(e -> showMembers());

       issueButton.addActionListener(e -> showIssueBook());
returnButton.addActionListener(e -> showReturnBook());
historyButton.addActionListener(e -> showIssueHistory());

        searchButton.addActionListener(e -> showSearchBooks());

        reportsButton.addActionListener(e -> showReports());

        logoutButton.addActionListener(e -> {

            dispose();

            new LoginFrame().setVisible(true);
        });

        return sidebar;
    }

    // =====================================================
    // MENU BUTTON
    // =====================================================

    private JButton createMenuButton(String text) {

        JButton button = new JButton(text);

        button.setForeground(SIDEBAR_TEXT);
        button.setBackground(SIDEBAR_COLOR);

        button.setFont(new Font("Arial", Font.PLAIN, 15));

        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 15, 10, 10
                )
        );

        button.setFocusPainted(false);

        return button;
    }

    // =====================================================
    // DASHBOARD
    // =====================================================

    private void showDashboard() {

        contentPanel.removeAll();

        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(BACKGROUND);
        dashboard.setBorder(new EmptyBorder(25, 30, 25, 30));

        // -------------------------
        // Header
        // -------------------------

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Dashboard");

        title.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        JLabel welcome = new JLabel(
                "Welcome back, Admin"
        );

        welcome.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        welcome.setForeground(Color.GRAY);

        JPanel headerText = new JPanel();
        headerText.setLayout(new BoxLayout(
                headerText,
                BoxLayout.Y_AXIS
        ));

        headerText.setOpaque(false);

        headerText.add(title);
        headerText.add(Box.createVerticalStrut(5));
        headerText.add(welcome);

        header.add(headerText, BorderLayout.WEST);

        // -------------------------
        // Statistics
        // -------------------------

        DashboardDAO dashboardDAO = new DashboardDAO();

DashboardDAO.DashboardStats stats;

try {
    stats = dashboardDAO.getDashboardStats();
} catch (SQLException ex) {
    ex.printStackTrace();

    JOptionPane.showMessageDialog(
            this,
            "Could not load dashboard statistics.",
            "Database Error",
            JOptionPane.ERROR_MESSAGE
    );

    stats = null;
}

JPanel cardsPanel = new JPanel(
        new GridLayout(1, 5, 15, 15)
);
cardsPanel.setOpaque(false);

if (stats != null) {
    cardsPanel.add(createCard(
            "Total Book Titles",
            String.valueOf(stats.getTotalBookTitles())
    ));

    cardsPanel.add(createCard(
            "Available Copies",
            String.valueOf(stats.getAvailableCopies())
    ));

    cardsPanel.add(createCard(
            "Currently Issued",
            String.valueOf(stats.getCurrentlyIssued())
    ));

    cardsPanel.add(createCard(
            "Total Members",
            String.valueOf(stats.getTotalMembers())
    ));
    int overdueCount = 0;

try {
    overdueCount = dashboardDAO.getOverdueCount();
} catch (SQLException ex) {
    ex.printStackTrace();

    JOptionPane.showMessageDialog(
            this,
            "Could not load overdue book count.",
            "Database Error",
            JOptionPane.ERROR_MESSAGE
    );
}

cardsPanel.add(createCard(
        "Overdue Books",
        String.valueOf(overdueCount)
));
} else {
    cardsPanel.add(createCard("Total Book Titles", "—"));
    cardsPanel.add(createCard("Available Copies", "—"));
    cardsPanel.add(createCard("Currently Issued", "—"));
    cardsPanel.add(createCard("Total Members", "—"));
}

        // -------------------------
        // Transactions
        // -------------------------

        JPanel transactionPanel = new JPanel(
                new BorderLayout()
        );

        transactionPanel.setBackground(Color.WHITE);

        transactionPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)
                        ),
                        new EmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        JLabel transactionTitle =
                new JLabel("Recent Transactions");

        transactionTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        transactionPanel.add(
                transactionTitle,
                BorderLayout.NORTH
        );

        String[] columns = {
                "Issue ID",
                "Member",
                "Book",
                "Issue Date",
                "Status"
        };

       JTable table;
DefaultTableModel transactionModel =
        new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

try {
    List<DashboardDAO.RecentTransaction> transactions =
            dashboardDAO.getRecentTransactions(10);

    for (DashboardDAO.RecentTransaction transaction : transactions) {
        transactionModel.addRow(new Object[]{
            transaction.getIssueId(),
            transaction.getMemberName(),
            transaction.getBookTitle(),
            transaction.getIssueDate(),
            transaction.getStatus()
        });
    }

    table = new JTable(transactionModel);

} catch (SQLException ex) {
    ex.printStackTrace();

    JOptionPane.showMessageDialog(
            this,
            "Could not load recent transactions.",
            "Database Error",
            JOptionPane.ERROR_MESSAGE
    );

    table = new JTable(transactionModel);
}

table.setRowHeight(30);
table.setFont(new Font("Arial", Font.PLAIN, 13));
table.getTableHeader().setFont(
        new Font("Arial", Font.BOLD, 13)
);

JScrollPane scrollPane = new JScrollPane(table);

        table.setRowHeight(30);

        table.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

       

        transactionPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // -------------------------
        // Put everything together
        // -------------------------

        JPanel centerPanel = new JPanel(
                new BorderLayout(0, 20)
        );

        centerPanel.setOpaque(false);

        centerPanel.add(
                cardsPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                transactionPanel,
                BorderLayout.CENTER
        );

        dashboard.add(
                header,
                BorderLayout.NORTH
        );

        dashboard.add(
                centerPanel,
                BorderLayout.CENTER
        );

        contentPanel.add(
                dashboard,
                BorderLayout.CENTER
        );

        refreshContent();
    }

    // =====================================================
    // DASHBOARD CARD
    // =====================================================

    private JPanel createCard(
            String title,
            String value
    ) {

        JPanel card = new JPanel();

        card.setBackground(CARD_COLOR);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 225, 225)
                        ),
                        new EmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(Color.GRAY);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        valueLabel.setForeground(PRIMARY);

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(10)
        );

        card.add(valueLabel);

        return card;
    }

    // =====================================================
    // TEMPORARY PAGES
    // =====================================================
private void showBooks() {

    contentPanel.removeAll();

    contentPanel.add(
            new BooksPanel(),
            BorderLayout.CENTER
    );

    contentPanel.revalidate();
    contentPanel.repaint();
}
    private void showPage(String pageName) {

        contentPanel.removeAll();

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(BACKGROUND);

        panel.setBorder(
                new EmptyBorder(
                        30, 30, 30, 30
                )
        );

        JLabel title =
                new JLabel(pageName);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        contentPanel.add(
                panel,
                BorderLayout.CENTER
        );

        refreshContent();
    }

    // =====================================================
    // REFRESH
    // =====================================================

    private void refreshContent() {

        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void showMembers() {

    contentPanel.removeAll();

    contentPanel.add(
            new MembersPanel(),
            BorderLayout.CENTER
    );

    contentPanel.revalidate();
    contentPanel.repaint();
}
    private void showIssueBook() {

    contentPanel.removeAll();

    contentPanel.add(
            new IssueBookPanel(),
            BorderLayout.CENTER
    );

    contentPanel.revalidate();
    contentPanel.repaint();
}
   private void showIssueHistory() {
    contentPanel.removeAll();

    contentPanel.add(
            new IssueHistoryPanel(),
            BorderLayout.CENTER
    );

    contentPanel.revalidate();
    contentPanel.repaint();
} 
  private void showReturnBook() {

    contentPanel.removeAll();

    contentPanel.add(
            new ReturnBookPanel(),
            BorderLayout.CENTER
    );

    contentPanel.revalidate();
    contentPanel.repaint();
}
  private void showSearchBooks() {

    contentPanel.removeAll();

    contentPanel.add(
            new SearchBooksPanel(),
            BorderLayout.CENTER
    );

    contentPanel.revalidate();
    contentPanel.repaint();
}
  private void showReports() {

    contentPanel.removeAll();

    contentPanel.add(
            new ReportsPanel(),
            BorderLayout.CENTER
    );

    contentPanel.revalidate();
    contentPanel.repaint();
}
  
}