package com.bankingsystem.dao;

import com.bankingsystem.entity.UserAccount;
import com.bankingsystem.entity.UserRole;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    Optional<UserAccount> findByCredentials(String username, String password, UserRole role) throws SQLException;
}
