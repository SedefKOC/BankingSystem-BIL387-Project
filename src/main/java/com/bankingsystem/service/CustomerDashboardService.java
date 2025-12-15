package com.bankingsystem.service;

import com.bankingsystem.dao.TransactionDao;
import com.bankingsystem.dao.impl.JdbcTransactionDao;
import com.bankingsystem.entity.TransactionRecord;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CustomerDashboardService {
    private final TransactionDao transactionDao;

    public CustomerDashboardService() {
        this(new JdbcTransactionDao());
    }

    public CustomerDashboardService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public List<TransactionRecord> getRecentTransactions(long userId, int limit) {
        try {
            return transactionDao.findLatestByUserId(userId, limit);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
