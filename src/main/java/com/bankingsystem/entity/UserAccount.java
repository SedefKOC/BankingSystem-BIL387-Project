package com.bankingsystem.entity;

public class UserAccount {
    private final long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final UserRole role;

    public UserAccount(long id, String username, String firstName, String lastName, UserRole role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public String getFullName() {
        String fn = firstName == null ? "" : firstName.trim();
        String ln = lastName == null ? "" : lastName.trim();
        return (fn + " " + ln).trim();
    }
}
