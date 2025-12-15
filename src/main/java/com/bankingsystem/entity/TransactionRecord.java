package com.bankingsystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionRecord {
    private final String description;
    private final LocalDateTime transactionDate;
    private final BigDecimal amount;

    public TransactionRecord(String description, LocalDateTime transactionDate, BigDecimal amount) {
        this.description = description;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
