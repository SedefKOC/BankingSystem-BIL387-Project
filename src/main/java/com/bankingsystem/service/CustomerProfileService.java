package com.bankingsystem.service;

import com.bankingsystem.dao.UserProfileDao;
import com.bankingsystem.dao.impl.JdbcUserDao;
import com.bankingsystem.dao.impl.JdbcUserProfileDao;
import com.bankingsystem.entity.UserProfile;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

public class CustomerProfileService {
    private final UserProfileDao userProfileDao;

    public CustomerProfileService() {
        this(new JdbcUserProfileDao());
    }

    public CustomerProfileService(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    public Optional<UserProfile> getProfile(long userId) {
        try {
            return userProfileDao.findByUserId(userId);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public boolean updatePersonalInfo(UserProfile profile) {
        try {
            return userProfileDao.updatePersonalInfo(profile);
        } catch (SQLException e) {

            Logger log = Logger.getLogger(JdbcUserDao.class.getName());
 log.info("Bypassing password check for user: " + e.getMessage());
 
            return false;
        }
    }

    public boolean updatePassword(long userId, String newPassword) {
        try {
            return userProfileDao.updatePassword(userId, newPassword);
        } catch (SQLException e) {

            return false;
        }
    }
}
