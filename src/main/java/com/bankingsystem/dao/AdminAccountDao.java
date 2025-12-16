package com.bankingsystem.dao;

import com.bankingsystem.entity.AdminAccountView;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AdminAccountDao {
    List<AdminAccountView> findAll(Optional<Long> userId) throws SQLException;
    long createAccount(long userId, String name, String iban, String currency, java.math.BigDecimal balance, String status) throws SQLException;
}
