package com.bankingsystem.service;

import com.bankingsystem.dao.AccountDao;
import com.bankingsystem.dao.TransactionDao;
import com.bankingsystem.dao.impl.JdbcAccountDao;
import com.bankingsystem.dao.impl.JdbcTransactionDao;
import com.bankingsystem.entity.AccountSummary;
import com.bankingsystem.entity.TransactionRecord;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CustomerDashboardService {
    private final TransactionDao transactionDao;
    private final AccountDao accountDao;

    public CustomerDashboardService() {
        this(new JdbcTransactionDao(), new JdbcAccountDao());
    }

    public CustomerDashboardService(TransactionDao transactionDao, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
    }

    public List<TransactionRecord> getRecentTransactions(long userId, int limit) {
        try {
            return transactionDao.findLatestByUserId(userId, limit);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    public BigDecimal getTotalBalance(long userId) {
        try {
            return transactionDao.sumAmountByUserId(userId);
        } catch (SQLException e) {
            return BigDecimal.ZERO;
        }
    }

    public List<AccountSummary> getAccounts(long userId) {
        try {
            return accountDao.findByUserId(userId);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    public Optional<AccountSummary> getAccount(long userId, long accountId) {
        try {
            return accountDao.findByIdAndUser(accountId, userId);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public List<TransactionRecord> getAccountTransactions(long accountId, int limit) {
        try {
            return transactionDao.findByAccountId(accountId, limit);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
