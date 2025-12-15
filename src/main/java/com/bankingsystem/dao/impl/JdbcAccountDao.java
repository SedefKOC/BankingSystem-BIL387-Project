package com.bankingsystem.dao.impl;

import com.bankingsystem.dao.AccountDao;
import com.bankingsystem.entity.AccountSummary;
import com.bankingsystem.util.DBConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAccountDao implements AccountDao {
    private static final String BY_USER_SQL = """
            SELECT id, name, iban, currency, balance, status
            FROM accounts
            WHERE user_id = ?
            ORDER BY name
            """;
    private static final String BY_ID_AND_USER_SQL = """
            SELECT id, name, iban, currency, balance, status
            FROM accounts
            WHERE id = ? AND user_id = ?
            """;

    @Override
    public List<AccountSummary> findByUserId(long userId) throws SQLException {
        List<AccountSummary> accounts = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(BY_USER_SQL)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapAccount(rs));
                }
            }
        }
        return accounts;
    }

    @Override
    public Optional<AccountSummary> findByIdAndUser(long accountId, long userId) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(BY_ID_AND_USER_SQL)) {
            statement.setLong(1, accountId);
            statement.setLong(2, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapAccount(rs));
            }
        }
    }

    private AccountSummary mapAccount(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String iban = rs.getString("iban");
        String currency = rs.getString("currency");
        BigDecimal balance = rs.getBigDecimal("balance");
        String status = rs.getString("status");
        return new AccountSummary(
                id,
                name == null ? "" : name,
                iban == null ? "" : iban,
                currency == null ? "" : currency,
                balance == null ? BigDecimal.ZERO : balance,
                status == null ? "" : status
        );
    }
}
