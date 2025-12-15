package com.bankingsystem.entity;

public class UserProfile {
    private final long userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String nationalId;
    private final String birthDate;
    private final String address;

    public UserProfile(long userId,
                       String firstName,
                       String lastName,
                       String email,
                       String phoneNumber,
                       String nationalId,
                       String birthDate,
                       String address) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.address = address;
    }

    public long getUserId() {
        return userId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }
}
