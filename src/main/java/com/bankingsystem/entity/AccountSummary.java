package com.bankingsystem.entity;

import java.math.BigDecimal;

public class AccountSummary {
    private final long id;
    private final String name;
    private final String iban;
    private final String currencyLabel;
    private final BigDecimal balance;
    private final String statusLabel;

    public AccountSummary(long id, String name, String iban, String currencyLabel, BigDecimal balance, String statusLabel) {
        this.id = id;
        this.name = name;
        this.iban = iban;
        this.currencyLabel = currencyLabel;
        this.balance = balance;
        this.statusLabel = statusLabel;
    }

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public long getId() {
        return id;
    }
}
