package com.bankingsystem.dao;

import com.bankingsystem.entity.AccountSummary;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao {
    List<AccountSummary> findByUserId(long userId) throws SQLException;
    java.util.Optional<AccountSummary> findByIdAndUser(long accountId, long userId) throws SQLException;
}
