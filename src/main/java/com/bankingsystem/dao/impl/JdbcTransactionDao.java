package com.bankingsystem.dao.impl;

import com.bankingsystem.dao.TransactionDao;
import com.bankingsystem.entity.TransactionRecord;
import com.bankingsystem.util.DBConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionDao implements TransactionDao {
    private static final String RECENT_SQL = """
            SELECT transactions, transaction_date, amount
            FROM transactions
            WHERE user_id = ?
            ORDER BY transaction_date DESC
            LIMIT ?
            """;
    private static final String SUM_SQL = """
            SELECT COALESCE(SUM(amount), 0) AS total
            FROM transactions
            WHERE user_id = ?
            """;
    private static final String BY_ACCOUNT_SQL = """
            SELECT transactions, transaction_date, amount
            FROM transactions
            WHERE account_id = ?
            ORDER BY transaction_date DESC
            LIMIT ?
            """;
    @Override
    public List<TransactionRecord> findLatestByUserId(long userId, int limit) throws SQLException {
        List<TransactionRecord> records = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(RECENT_SQL)) {
            statement.setLong(1, userId);
            statement.setInt(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String description = resultSet.getString("transactions");
                    LocalDateTime dateTime = null;
                    if (resultSet.getTimestamp("transaction_date") != null) {
                        dateTime = resultSet.getTimestamp("transaction_date").toLocalDateTime();
                    }
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    records.add(new TransactionRecord(description, dateTime, amount));
                }
            }
        }
        return records;
    }

    @Override
    public BigDecimal sumAmountByUserId(long userId) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SUM_SQL)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    BigDecimal total = resultSet.getBigDecimal("total");
                    return total == null ? BigDecimal.ZERO : total;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<TransactionRecord> findByAccountId(long accountId, int limit) throws SQLException {
        List<TransactionRecord> records = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(BY_ACCOUNT_SQL)) {
            statement.setLong(1, accountId);
            statement.setInt(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String description = resultSet.getString("transactions");
                    LocalDateTime dateTime = null;
                    if (resultSet.getTimestamp("transaction_date") != null) {
                        dateTime = resultSet.getTimestamp("transaction_date").toLocalDateTime();
                    }
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    records.add(new TransactionRecord(description, dateTime, amount));
                }
            }
        }
        return records;
    }

}
