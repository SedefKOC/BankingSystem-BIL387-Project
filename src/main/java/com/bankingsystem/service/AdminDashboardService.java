package com.bankingsystem.service;

import com.bankingsystem.util.DBConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardService {
    private static final String ACTIVE_CUSTOMERS_SQL = """
            SELECT COUNT(*) AS total
            FROM users
            WHERE role = 'CUSTOMER'
            """;
    private static final String ACTIVE_ACCOUNTS_SQL = """
            SELECT COUNT(*) AS total
            FROM accounts
            WHERE UPPER(COALESCE(status, 'ACTIVE')) <> 'CLOSED'
            """;
    private static final String TODAY_VOLUME_SQL = """
            SELECT COALESCE(SUM(amount), 0) AS total
            FROM transactions
            WHERE DATE(transaction_date) = CURRENT_DATE
            """;
    private static final String RECENT_TRANSACTIONS_SQL = """
            SELECT t.id,
                   u.first_name,
                   u.last_name,
                   t.transactions,
                   t.amount,
                   t.transaction_date
            FROM transactions t
            JOIN accounts a ON t.account_id = a.id
            JOIN users u ON a.user_id = u.id
            ORDER BY t.transaction_date DESC NULLS LAST, t.id DESC
            LIMIT ?
            """;

    public AdminDashboardMetrics loadMetrics() throws SQLException {
        long customers = queryCount(ACTIVE_CUSTOMERS_SQL);
        long accounts = queryCount(ACTIVE_ACCOUNTS_SQL);
        BigDecimal todayVolume = queryDecimal(TODAY_VOLUME_SQL);
        return new AdminDashboardMetrics(customers, accounts, todayVolume);
    }

    public List<AdminTransactionView> loadRecentTransactions(int limit) throws SQLException {
        List<AdminTransactionView> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(RECENT_TRANSACTIONS_SQL)) {
            statement.setInt(1, limit);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String description = rs.getString("transactions");
                    BigDecimal amount = rs.getBigDecimal("amount");
                    LocalDateTime dateTime = rs.getTimestamp("transaction_date") == null
                            ? null
                            : rs.getTimestamp("transaction_date").toLocalDateTime();
                    transactions.add(new AdminTransactionView(
                            id,
                            combineName(firstName, lastName),
                            description == null ? "" : description,
                            amount == null ? BigDecimal.ZERO : amount,
                            dateTime
                    ));
                }
            }
        }
        return transactions;
    }

    private long queryCount(String sql) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("total");
            }
        }
        return 0;
    }

    private BigDecimal queryDecimal(String sql) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                BigDecimal value = rs.getBigDecimal("total");
                return value == null ? BigDecimal.ZERO : value;
            }
        }
        return BigDecimal.ZERO;
    }

    private String combineName(String firstName, String lastName) {
        String fn = firstName == null ? "" : firstName.trim();
        String ln = lastName == null ? "" : lastName.trim();
        String full = (fn + " " + ln).trim();
        return full.isEmpty() ? "Unnamed" : full;
    }

    public record AdminDashboardMetrics(long activeCustomers,
                                        long activeAccounts,
                                        BigDecimal todaysVolume) {
    }

    public record AdminTransactionView(long id,
                                       String customerName,
                                       String description,
                                       BigDecimal amount,
                                       LocalDateTime transactionDate) {
    }
}
