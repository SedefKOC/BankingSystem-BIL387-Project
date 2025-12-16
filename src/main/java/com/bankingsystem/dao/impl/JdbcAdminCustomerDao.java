package com.bankingsystem.dao.impl;

import com.bankingsystem.dao.AdminCustomerDao;
import com.bankingsystem.entity.AdminCustomerSummary;
import com.bankingsystem.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcAdminCustomerDao implements AdminCustomerDao {
    private static final String LIST_SQL = """
            SELECT id, first_name, last_name, email, phone, national_id, created_at
            FROM users
            WHERE role = 'CUSTOMER'
            ORDER BY created_at DESC NULLS LAST, id DESC
            """;
    private static final String INSERT_SQL = """
            INSERT INTO users (username, password_hash, first_name, last_name, email, phone, national_id, role, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, 'CUSTOMER', NOW())
            RETURNING id
            """;

    @Override
    public List<AdminCustomerSummary> findAllCustomers() throws SQLException {
        List<AdminCustomerSummary> customers = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(LIST_SQL);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                customers.add(mapRow(rs));
            }
        }
        return customers;
    }

    @Override
    public long createCustomer(String username, String password, String firstName, String lastName,
                               String email, String phone, String nationalId) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, email);
            statement.setString(6, phone);
            statement.setString(7, nationalId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Customer insert failed.");
    }

    private AdminCustomerSummary mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String identifier = rs.getString("national_id");
        LocalDate createdDate = toLocalDate(rs.getTimestamp("created_at"));
        return new AdminCustomerSummary(
                id,
                firstName,
                lastName,
                email,
                phone,
                identifier == null || identifier.isBlank() ? "ID: " + id : identifier,
                createdDate
        );
    }

    private LocalDate toLocalDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime().toLocalDate();
    }
}
