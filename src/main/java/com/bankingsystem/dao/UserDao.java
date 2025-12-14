package com.bankingsystem.dao;

import com.bankingsystem.entity.UserRole;

import java.sql.SQLException;

public interface UserDao {
    boolean validateCredentials(String username, String password, UserRole role) throws SQLException;
}
