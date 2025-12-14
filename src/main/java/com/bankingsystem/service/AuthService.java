package com.bankingsystem.service;

import com.bankingsystem.dao.UserDao;
import com.bankingsystem.dao.impl.JdbcUserDao;
import com.bankingsystem.entity.UserRole;

import java.sql.SQLException;

public class AuthService {
    private final UserDao userDao;

    public AuthService() {
        this(new JdbcUserDao());
    }

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public AuthResult authenticate(String rawRole, String rawUsername, String rawPassword) {
        UserRole role = UserRole.fromParam(rawRole).orElse(null);
        String username = normalize(rawUsername);
        String password = normalize(rawPassword);

        if (role == null) {
            return new AuthResult(false, "Lütfen Admin veya Customer rolünü seç.", null);
        }
        if (username.isEmpty() || password.isEmpty()) {
            return new AuthResult(false, "Kullanıcı adı ve şifre boş bırakılamaz.", role);
        }
        try {
            boolean valid = userDao.validateCredentials(username, password, role);
            if (valid) {
                String roleMessage = role == null ? "" : role.getDisplayName() + " girişi";
                return new AuthResult(true, roleMessage + " onaylandı. Hoş geldin!", role);
            }
            return new AuthResult(false, "Kullanıcı adı veya şifre hatalı.", role);
        } catch (SQLException e) {
            return new AuthResult(false, "Şu an giriş yapılamıyor. Lütfen biraz sonra tekrar dene.", role);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public record AuthResult(boolean success, String message, UserRole role) {
    }
}
