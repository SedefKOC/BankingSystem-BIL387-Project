package com.bankingsystem.controller;

import com.bankingsystem.dao.AdminAccountDao;
import com.bankingsystem.dao.AdminCustomerDao;
import com.bankingsystem.dao.impl.JdbcAdminAccountDao;
import com.bankingsystem.dao.impl.JdbcAdminCustomerDao;
import com.bankingsystem.entity.AdminAccountView;
import com.bankingsystem.entity.AdminCustomerSummary;
import com.bankingsystem.entity.UserRole;
import com.bankingsystem.dao.impl.JdbcUserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class AdminAccountsServlet extends HttpServlet {
    private final AdminAccountDao accountDao;
    private final AdminCustomerDao customerDao;

    public AdminAccountsServlet() {
        this(new JdbcAdminAccountDao(), new JdbcAdminCustomerDao());
    }

    public AdminAccountsServlet(AdminAccountDao accountDao, AdminCustomerDao customerDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        prepareSessionAttributes(req, session);
        Optional<Long> userFilter = parseUserId(req.getParameter("userId"));
        loadAccounts(req, userFilter);
        loadCustomers(req);
        req.getRequestDispatcher("/WEB-INF/views/admin-accounts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        prepareSessionAttributes(req, session);
        Optional<Long> userFilter = parseUserId(req.getParameter("userId"));
        String action = req.getParameter("action");
        Optional<Long> overrideFilter = Optional.empty();
        if ("addAccount".equals(action)) {
            overrideFilter = handleAddAccount(req);
        } else {
            req.setAttribute("accountFormError", "Geçersiz işlem.");
        }
        Optional<Long> effectiveFilter = overrideFilter.isPresent() ? overrideFilter : userFilter;
        if (req.getAttribute("accountFilterUserId") == null && effectiveFilter.isPresent()) {
            req.setAttribute("accountFilterUserId", effectiveFilter.get());
        }
        loadAccounts(req, effectiveFilter);
        loadCustomers(req);
        req.getRequestDispatcher("/WEB-INF/views/admin-accounts.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("currentUserRole");
        return role == UserRole.ADMIN;
    }

    private void prepareSessionAttributes(HttpServletRequest req, HttpSession session) {
        String displayName = asString(resolveDisplayName(session));
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("adminNavActive", "accounts");
    }

    private void loadAccounts(HttpServletRequest req, Optional<Long> userFilter) {
        userFilter.ifPresent(id -> req.setAttribute("accountFilterUserId", id));
        try {
            List<AdminAccountView> accounts = accountDao.findAll(userFilter);
            req.setAttribute("adminAccounts", accounts);
        } catch (java.sql.SQLException e) {
            req.setAttribute("adminAccountError", "Hesaplar yüklenirken hata oluştu.");
            req.setAttribute("adminAccounts", java.util.Collections.emptyList());
        }
    }

    private void loadCustomers(HttpServletRequest req) {
        try {
            List<AdminCustomerSummary> customers = customerDao.findAllCustomers();
            req.setAttribute("accountCustomers", customers);
        } catch (java.sql.SQLException e) {
            req.setAttribute("accountCustomers", java.util.Collections.emptyList());
            if (req.getAttribute("accountFormError") == null) {
                req.setAttribute("accountFormError", "Müşteri listesi getirilemedi.");
            }
        }
    }

    private Optional<Long> handleAddAccount(HttpServletRequest req) {
        Map<String, String> form = new HashMap<>();
        String userIdRaw = capture(form, "accountUserId", req.getParameter("accountUserId"));
        String name = capture(form, "accountName", req.getParameter("accountName"));
        String iban = capture(form, "iban", req.getParameter("iban"));
        String currency = capture(form, "currency", req.getParameter("currency"));
        String balanceRaw = capture(form, "balance", req.getParameter("balance"));
        String status = capture(form, "status", req.getParameter("status"));
        req.setAttribute("newAccountForm", form);
        req.setAttribute("accountFormVisible", true);

        long userId = parseLong(userIdRaw);
        if (userId <= 0) {
            req.setAttribute("accountFormError", "Lütfen bir müşteri seç.");
            return Optional.empty();
        }
        if (name.isEmpty()) {
            req.setAttribute("accountFormError", "Hesap adı boş bırakılamaz.");
            return Optional.empty();
        }
        if (currency.isEmpty()) {
            req.setAttribute("accountFormError", "Para birimi seçmelisin.");
            return Optional.empty();
        }
        String normalizedStatus = status.isEmpty() ? "ACTIVE" : status.toUpperCase();
        BigDecimal balance;
        try {
            balance = balanceRaw.isEmpty() ? BigDecimal.ZERO : new BigDecimal(balanceRaw);
        } catch (NumberFormatException e) {
            req.setAttribute("accountFormError", "Tutar sayısal olmalı.");
            return Optional.empty();
        }
        try {
            long newId = accountDao.createAccount(
                    userId,
                    name,
                    iban.isBlank() ? null : iban,
                    currency.toUpperCase(),
                    balance,
                    normalizedStatus
            );
            req.setAttribute("accountSuccess", name + " hesabı oluşturuldu (ID: " + newId + ").");
            req.setAttribute("newAccountForm", null);
            req.setAttribute("accountFormVisible", false);
            req.setAttribute("accountFilterUserId", userId);
            return Optional.of(userId);
        } catch (java.sql.SQLException e) {
            /* Logger log = Logger.getLogger(JdbcUserDao.class.getName());
            log.info("HATA!!!!!!!!!!!!!!!!: " + e); */
            req.setAttribute("accountFormError", "Hesap oluşturulurken hata oluştu.");
            return Optional.empty();
        }
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

    private long parseLong(String raw) {
        try {
            return raw == null ? 0 : Long.parseLong(raw);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String capture(Map<String, String> map, String key, String raw) {
        String value = raw == null ? "" : raw.trim();
        map.put(key, value);
        return value;
    }
}
