package com.bankingsystem.dao.impl;

import com.bankingsystem.dao.UserDao;
import com.bankingsystem.entity.UserAccount;
import com.bankingsystem.entity.UserRole;
import com.bankingsystem.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserDao implements UserDao {
    private static final String LOGIN_SQL = """
            SELECT id, username, password_hash, first_name, last_name
            FROM users
            WHERE username = ? AND role = ?
            """;

    @Override
    public Optional<UserAccount> findByCredentials(String username, String password, UserRole role) throws SQLException {
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(LOGIN_SQL)) {
            statement.setString(1, username);
            statement.setString(2, role.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                String storedPassword = resultSet.getString("password_hash");
                if (storedPassword == null || !storedPassword.equals(password)) {
                    return Optional.empty();
                }
                UserAccount account = new UserAccount(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        role
                );
                return Optional.of(account);
            }
        }
    }
}
