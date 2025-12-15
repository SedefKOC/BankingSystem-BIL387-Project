package com.bankingsystem.dao;

import com.bankingsystem.entity.UserProfile;

import java.sql.SQLException;
import java.util.Optional;

public interface UserProfileDao {
    Optional<UserProfile> findByUserId(long userId) throws SQLException;
    boolean updatePersonalInfo(UserProfile profile) throws SQLException;
    boolean updatePassword(long userId, String newPassword) throws SQLException;
}
