package com.bankingsystem.dao;

import com.bankingsystem.entity.AdminCustomerSummary;

import java.sql.SQLException;
import java.util.List;

public interface AdminCustomerDao {
    List<AdminCustomerSummary> findAllCustomers() throws SQLException;
    long createCustomer(String username, String password, String firstName, String lastName,
                        String email, String phone, String nationalId) throws SQLException;
}
