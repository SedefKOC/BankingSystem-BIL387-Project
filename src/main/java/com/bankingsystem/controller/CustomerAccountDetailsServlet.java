package com.bankingsystem.controller;

import com.bankingsystem.entity.AccountSummary;
import com.bankingsystem.entity.TransactionRecord;
import com.bankingsystem.entity.UserRole;
import com.bankingsystem.service.CustomerDashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CustomerAccountDetailsServlet extends HttpServlet {
    private final CustomerDashboardService dashboardService = new CustomerDashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String displayName = getDisplayName(session);
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("navActive", "accounts");

        Long accountId = parseAccountId(req.getParameter("accountId"));
        Long userId = (Long) session.getAttribute("currentUserId");
        if (accountId == null || userId == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/accounts");
            return;
        }
        Optional<AccountSummary> accountOpt = dashboardService.getAccount(userId, accountId);
        if (accountOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customer/accounts");
            return;
        }
        req.setAttribute("account", accountOpt.get());
        req.setAttribute("accountTransactions", dashboardService.getAccountTransactions(accountId, 20));
        req.getRequestDispatcher("/WEB-INF/views/customer-account-detail.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("currentUserRole");
        return role == UserRole.CUSTOMER;
    }

    private Long parseAccountId(String raw) {
        try {
            return raw == null ? null : Long.parseLong(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String getDisplayName(HttpSession session) {
        Object fullName = session.getAttribute("currentFullName");
        if (fullName instanceof String && !((String) fullName).isBlank()) {
            return (String) fullName;
        }
        Object username = session.getAttribute("currentUsername");
        return username == null ? "" : username.toString();
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
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
}
