package com.bankingsystem.dao;

import com.bankingsystem.entity.TransactionRecord;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDao {
    List<TransactionRecord> findLatestByUserId(long userId, int limit) throws SQLException;
    java.math.BigDecimal sumAmountByUserId(long userId) throws SQLException;
    List<TransactionRecord> findByAccountId(long accountId, int limit) throws SQLException;
    void insertTransaction(long userId, long accountId, String iban, String description,
                           java.math.BigDecimal amount, java.time.LocalDate date) throws SQLException;
}
