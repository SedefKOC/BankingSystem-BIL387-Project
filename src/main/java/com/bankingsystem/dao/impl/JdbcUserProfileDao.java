package com.bankingsystem.dao.impl;

import com.bankingsystem.dao.UserProfileDao;
import com.bankingsystem.entity.UserProfile;
import com.bankingsystem.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserProfileDao implements UserProfileDao {
    private static final String SELECT_SQL = """
            SELECT first_name, last_name, email, phone, national_id, birth_date, address
            FROM users
            WHERE id = ?
            """;
    private static final String UPDATE_INFO_SQL = """
            UPDATE users
            SET first_name = ?, last_name = ?, email = ?, phone = ?, national_id = ?, birth_date = ?, address = ?
            WHERE id = ?
            """;
    private static final String UPDATE_PASSWORD_SQL = """
            UPDATE users
            SET password_hash = ?
            WHERE id = ? AND password_hash = ?
            """;

    @Override
    public Optional<UserProfile> findByUserId(long userId) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(map(rs, userId));
            }
        }
    }

    @Override
    public boolean updatePersonalInfo(UserProfile profile) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_INFO_SQL)) {
            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getEmail());
            statement.setString(4, profile.getPhoneNumber());
            statement.setString(5, profile.getNationalId());
            statement.setObject(6, Date.valueOf(profile.getBirthDate()));
            statement.setString(7, profile.getAddress());
            statement.setLong(8, profile.getUserId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePassword(long userId, String currentPassword, String newPassword) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD_SQL)) {
            statement.setString(1, newPassword);
            statement.setLong(2, userId);
            statement.setString(3, currentPassword);
            return statement.executeUpdate() > 0;
        }
    }

    private UserProfile map(ResultSet rs, long userId) throws SQLException {
        return new UserProfile(
                userId,
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("national_id"),
                rs.getString("birth_date"),
                rs.getString("address")
        );
    }
}
