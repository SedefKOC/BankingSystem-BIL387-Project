package com.bankingsystem.controller;

import com.bankingsystem.dao.AdminAccountDao;
import com.bankingsystem.dao.impl.JdbcAdminAccountDao;
import com.bankingsystem.entity.AdminAccountView;
import com.bankingsystem.entity.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminAccountsServlet extends HttpServlet {
    private final AdminAccountDao accountDao;

    public AdminAccountsServlet() {
        this(new JdbcAdminAccountDao());
    }

    public AdminAccountsServlet(AdminAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String displayName = asString(resolveDisplayName(session));
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("adminNavActive", "accounts");
        Optional<Long> userFilter = parseUserId(req.getParameter("userId"));
        userFilter.ifPresent(id -> req.setAttribute("accountFilterUserId", id));
        try {
            List<AdminAccountView> accounts = accountDao.findAll(userFilter);
            req.setAttribute("adminAccounts", accounts);
        } catch (java.sql.SQLException e) {
            req.setAttribute("adminAccountError", "Hesaplar yüklenirken hata oluştu.");
            req.setAttribute("adminAccounts", java.util.Collections.emptyList());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin-accounts.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("currentUserRole");
        return role == UserRole.ADMIN;
    }

    private Object resolveDisplayName(HttpSession session) {
        Object fullName = session.getAttribute("currentFullName");
        if (fullName instanceof String && !((String) fullName).isBlank()) {
            return fullName;
        }
        return session.getAttribute("currentUsername");
    }

    private String asString(Object value) {
        return value == null ? "" : value.toString();
    }

    private String initials(String value) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.length() >= 2) {
            return trimmed.substring(0, 2).toUpperCase();
        }
        if (!trimmed.isEmpty()) {
            return trimmed.substring(0, 1).toUpperCase();
        }
        return "??";
    }

    private String sanitize(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    private Optional<Long> parseUserId(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(raw));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
