package com.bankingsystem.service;

import com.bankingsystem.dao.UserDao;
import com.bankingsystem.dao.impl.JdbcUserDao;
import com.bankingsystem.entity.UserAccount;
import com.bankingsystem.entity.UserRole;

import java.sql.SQLException;
import java.util.Optional;

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
            return new AuthResult(false, "Lütfen Admin veya Customer rolünü seç.", null, null);
        }
        if (username.isEmpty() || password.isEmpty()) {
            return new AuthResult(false, "Kullanıcı adı ve şifre boş bırakılamaz.", role, null);
        }
        try {
            Optional<UserAccount> account = userDao.findByCredentials(username, password, role);
            if (account.isPresent()) {
                return new AuthResult(true, role.getDisplayName() + " girişi onaylandı. Hoş geldin " + account.get().getFullName() + "!", role, account.get());
            }
            return new AuthResult(false, "Kullanıcı adı veya şifre hatalı.", role, null);
        } catch (SQLException e) {
            return new AuthResult(false, "Şu an giriş yapılamıyor. Lütfen biraz sonra tekrar dene.", role, null);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public record AuthResult(boolean success, String message, UserRole role, UserAccount account) {
    }
}
