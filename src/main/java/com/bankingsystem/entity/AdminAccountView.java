package com.bankingsystem.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AdminAccountView {
    private final long id;
    private final String iban;
    private final String accountType;
    private final String currency;
    private final BigDecimal balance;
    private final String status;
    private final LocalDate createdDate;
    private final String customerFirstName;
    private final String customerLastName;

    public AdminAccountView(long id,
                            String iban,
                            String accountType,
                            String currency,
                            BigDecimal balance,
                            String status,
                            LocalDate createdDate,
                            String customerFirstName,
                            String customerLastName) {
        this.id = id;
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.status = status;
        this.createdDate = createdDate;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
    }

    public long getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public String getCustomerName() {
        String fn = customerFirstName == null ? "" : customerFirstName.trim();
        String ln = customerLastName == null ? "" : customerLastName.trim();
        String full = (fn + " " + ln).trim();
        return full.isEmpty() ? "Unnamed" : full;
    }

    public String getCustomerInitials() {
        StringBuilder builder = new StringBuilder();
        if (customerFirstName != null && !customerFirstName.isBlank()) {
            builder.append(customerFirstName.trim().charAt(0));
        }
        if (customerLastName != null && !customerLastName.isBlank()) {
            builder.append(customerLastName.trim().charAt(0));
        }
        String initials = builder.toString();
        if (initials.isEmpty()) {
            String name = getCustomerName();
            return name.isEmpty() ? "??" : name.substring(0, 1).toUpperCase();
        }
        return initials.toUpperCase();
    }
}
