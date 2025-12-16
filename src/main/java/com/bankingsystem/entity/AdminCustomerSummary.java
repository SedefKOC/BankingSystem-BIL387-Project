package com.bankingsystem.entity;

import java.time.LocalDate;

public class AdminCustomerSummary {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String identifier;
    private final LocalDate joinDate;

    public AdminCustomerSummary(long id,
                                String firstName,
                                String lastName,
                                String email,
                                String phone,
                                String identifier,
                                LocalDate joinDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.identifier = identifier;
        this.joinDate = joinDate;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getIdentifier() {
        return identifier;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public String getFullName() {
        String fn = firstName == null ? "" : firstName.trim();
        String ln = lastName == null ? "" : lastName.trim();
        String full = (fn + " " + ln).trim();
        return full.isEmpty() ? "Unnamed" : full;
    }

    public String getInitials() {
        StringBuilder builder = new StringBuilder();
        if (firstName != null && !firstName.isBlank()) {
            builder.append(firstName.trim().charAt(0));
        }
        if (lastName != null && !lastName.isBlank()) {
            builder.append(lastName.trim().charAt(0));
        }
        String initials = builder.toString();
        if (initials.isEmpty() && !getFullName().isEmpty()) {
            return getFullName().substring(0, 1).toUpperCase();
        }
        return initials.toUpperCase();
    }
}
