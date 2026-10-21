
package com.library.dao;

import com.library.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {
    
public int getOverdueCount() throws SQLException {
    String sql =
            "SELECT COUNT(*) FROM book_issues "
            + "WHERE status = 'ISSUED' "
            + "AND return_date IS NULL "
            + "AND due_date < CURDATE()";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
    }

    return 0;
}
    // Holds the four dashboard statistics.
    public static class DashboardStats {
        private int totalBookTitles;
        private int availableCopies;
        private int currentlyIssued;
        private int totalMembers;

        public int getTotalBookTitles() {
            return totalBookTitles;
        }

        public int getAvailableCopies() {
            return availableCopies;
        }

        public int getCurrentlyIssued() {
            return currentlyIssued;
        }

        public int getTotalMembers() {
            return totalMembers;
        }
    }

    // Holds one recent transaction.
    public static class RecentTransaction {
        private int issueId;
        private String memberName;
        private String bookTitle;
        private String issueDate;
        private String status;

        public int getIssueId() {
            return issueId;
        }

        public String getMemberName() {
            return memberName;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public String getStatus() {
            return status;
        }
    }

    // Fetch live dashboard counts.
    public DashboardStats getDashboardStats() throws SQLException {
        DashboardStats stats = new DashboardStats();

        String sql =
                "SELECT "
                + "(SELECT COUNT(*) FROM books) AS total_titles, "
                + "(SELECT COALESCE(SUM(available_copies), 0) "
                + " FROM books) AS available_copies, "
                + "(SELECT COUNT(*) FROM book_issues "
                + " WHERE status = 'ISSUED' "
                + " AND return_date IS NULL) AS currently_issued, "
                + "(SELECT COUNT(*) FROM members) AS total_members";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                stats.totalBookTitles =
                        resultSet.getInt("total_titles");

                stats.availableCopies =
                        resultSet.getInt("available_copies");

                stats.currentlyIssued =
                        resultSet.getInt("currently_issued");

                stats.totalMembers =
                        resultSet.getInt("total_members");
            }
        }

        return stats;
    }

    // Fetch the latest transactions.
    public List<RecentTransaction> getRecentTransactions(int limit)
            throws SQLException {

        List<RecentTransaction> transactions = new ArrayList<>();

        if (limit < 1) {
            return transactions;
        }

        String sql =
                "SELECT bi.id, m.name AS member_name, "
                + "b.title AS book_title, bi.issue_date, bi.status "
                + "FROM book_issues bi "
                + "JOIN members m ON bi.member_id = m.id "
                + "JOIN books b ON bi.book_id = b.id "
                + "ORDER BY bi.id DESC LIMIT ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RecentTransaction transaction =
                            new RecentTransaction();

                    transaction.issueId =
                            resultSet.getInt("id");

                    transaction.memberName =
                            resultSet.getString("member_name");

                    transaction.bookTitle =
                            resultSet.getString("book_title");

                    transaction.issueDate =
                            resultSet.getDate("issue_date").toString();

                    transaction.status =
                            resultSet.getString("status");

                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }
}
