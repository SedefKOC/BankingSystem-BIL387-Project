package com.bankingsystem.dao.impl;

import com.bankingsystem.dao.UserDao;
import com.bankingsystem.entity.UserRole;
import com.bankingsystem.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
    private static final String LOGIN_SQL = """
            SELECT password_hash
            FROM users
            WHERE username = ? AND role = ?
            """;

    @Override
    public boolean validateCredentials(String username, String password, UserRole role) throws SQLException {

        try (Connection connection = DBConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(LOGIN_SQL)) {

            statement.setString(1, username);
            statement.setString(2, role.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return false;
                }
                String storedPassword = resultSet.getString("password_hash");
                return storedPassword != null && storedPassword.equals(password);

            } catch (Exception e) {
                return false;
            }
        }
    }
}
