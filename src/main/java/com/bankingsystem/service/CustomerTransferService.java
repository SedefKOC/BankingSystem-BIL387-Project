package com.bankingsystem.service;

import com.bankingsystem.dao.AccountDao;
import com.bankingsystem.dao.TransactionDao;
import com.bankingsystem.dao.impl.JdbcAccountDao;
import com.bankingsystem.dao.impl.JdbcTransactionDao;
import com.bankingsystem.dao.impl.JdbcUserDao;
import com.bankingsystem.entity.AccountSummary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

public class CustomerTransferService {
    private final TransactionDao transactionDao;
    private final AccountDao accountDao;

    public CustomerTransferService() {
        this(new JdbcTransactionDao(), new JdbcAccountDao());
    }

    public CustomerTransferService(TransactionDao transactionDao, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
    }

    public TransferResult transferBetweenMyAccounts(long userId, long fromAccountId, long toAccountId,
                                                    BigDecimal amount, LocalDate date, String description) {
        if (fromAccountId == toAccountId) {
            return TransferResult.error("Kaynak ve hedef hesap farklı olmalı.");
        }
        Optional<AccountSummary> fromAccount = accountDaoFind(userId, fromAccountId);
        Optional<AccountSummary> toAccount = accountDaoFind(userId, toAccountId);
        if (fromAccount.isEmpty() || toAccount.isEmpty()) {
            return TransferResult.error("Hesap bilgileri doğrulanamadı.");
        }
        if (amount == null || amount.signum() <= 0) {
            return TransferResult.error("Tutar 0'dan büyük olmalı.");
        }
        try {
            transactionDao.insertTransaction(userId, fromAccountId, null, description, amount.negate(), date);
            transactionDao.insertTransaction(userId, toAccountId, null, description, amount, date);
            return TransferResult.success("Transfer tamamlandı.");
        } catch (SQLException e) {
            Logger log = Logger.getLogger(JdbcUserDao.class.getName());
 log.info("HATA!!!!!!!!!!!!!!!!: " + e);
            return TransferResult.error("Transfer kaydedilirken hata oluştu.");
        }
    }

    public TransferResult transferToIban(long userId, long fromAccountId, String toIban,
                                         BigDecimal amount, LocalDate date, String description) {
        if (toIban == null || toIban.isBlank()) {
            return TransferResult.error("IBAN boş olamaz.");
        }
        Optional<AccountSummary> fromAccount = accountDaoFind(userId, fromAccountId);
        if (fromAccount.isEmpty()) {
            return TransferResult.error("Kaynak hesap bulunamadı.");
        }
        if (amount == null || amount.signum() <= 0) {
            return TransferResult.error("Tutar 0'dan büyük olmalı.");
        }
        try {
            transactionDao.insertTransaction(userId, fromAccountId, toIban.trim(), description, amount.negate(), date);
            return TransferResult.success("IBAN transferi kaydedildi.");
        } catch (SQLException e) {
            return TransferResult.error("Transfer kaydedilirken hata oluştu.");
        }
    }

    private Optional<AccountSummary> accountDaoFind(long userId, long accountId) {
        try {
            return accountDao.findByIdAndUser(accountId, userId);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public record TransferResult(boolean success, String message) {
        static TransferResult success(String message) {
            return new TransferResult(true, message);
        }

        public static TransferResult error(String message) {
            return new TransferResult(false, message);
        }
    }
}
