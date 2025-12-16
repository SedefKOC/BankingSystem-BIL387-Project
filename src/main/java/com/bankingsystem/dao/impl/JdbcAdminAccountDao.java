package com.bankingsystem.dao.impl;

import com.bankingsystem.dao.AdminAccountDao;
import com.bankingsystem.entity.AdminAccountView;
import com.bankingsystem.util.DBConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAdminAccountDao implements AdminAccountDao {
    private static final String BASE_SQL = """
            SELECT a.id,
                   a.iban,
                   a.name AS account_type,
                   a.currency,
                   a.balance,
                   a.status,
                   a.created_at,
                   u.first_name,
                   u.last_name
            FROM accounts a
            JOIN users u ON a.user_id = u.id
            """;

    @Override
    public List<AdminAccountView> findAll(Optional<Long> userId) throws SQLException {
        StringBuilder sql = new StringBuilder(BASE_SQL);
        if (userId.isPresent()) {
            sql.append(" WHERE a.user_id = ?");
        }
        sql.append(" ORDER BY a.created_at DESC NULLS LAST, a.id DESC");

        List<AdminAccountView> accounts = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            if (userId.isPresent()) {
                statement.setLong(1, userId.get());
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapRow(rs));
                }
            }
        }
        return accounts;
    }

    private AdminAccountView mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String iban = rs.getString("iban");
        String type = rs.getString("account_type");
        String currency = rs.getString("currency");
        BigDecimal balance = rs.getBigDecimal("balance");
        String status = rs.getString("status");
        LocalDate createdDate = toLocalDate(rs.getTimestamp("created_at"));
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        return new AdminAccountView(
                id,
                iban,
                type,
                currency,
                balance == null ? BigDecimal.ZERO : balance,
                status,
                createdDate,
                firstName,
                lastName
        );
    }

    private LocalDate toLocalDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime().toLocalDate();
    }
}
