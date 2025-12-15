package com.bankingsystem.controller;

import com.bankingsystem.entity.AccountSummary;
import com.bankingsystem.entity.UserRole;
import com.bankingsystem.service.CustomerDashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class CustomerAccountsServlet extends HttpServlet {
    private final CustomerDashboardService dashboardService = new CustomerDashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String displayName = CustomerViewHelper.displayName(session);
        req.setAttribute("homeUsername", CustomerViewHelper.sanitize(displayName));
        req.setAttribute("homeInitials", CustomerViewHelper.initials(displayName));
        req.setAttribute("navActive", "accounts");

        Long userId = (Long) session.getAttribute("currentUserId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        BigDecimal totalBalance = dashboardService.getTotalBalance(userId);
        req.setAttribute("totalBalance", totalBalance);
        req.setAttribute("accounts", dashboardService.getAccounts(userId));
        req.getRequestDispatcher("/WEB-INF/views/customer-accounts.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("currentUserRole");
        return role == UserRole.CUSTOMER;
    }

    public static final class CustomerViewHelper {
        private CustomerViewHelper() {
        }

        static String displayName(HttpSession session) {
            Object fullName = session.getAttribute("currentFullName");
            if (fullName instanceof String && !((String) fullName).isBlank()) {
                return (String) fullName;
            }
            Object username = session.getAttribute("currentUsername");
            return username == null ? "" : username.toString();
        }

        static String sanitize(String value) {
            if (value == null) {
                return "";
            }
            return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
        }

        static String initials(String value) {
            String trimmed = value == null ? "" : value.trim();
            if (trimmed.length() >= 2) {
                return trimmed.substring(0, 2).toUpperCase(Locale.ROOT);
            }
            if (!trimmed.isEmpty()) {
                return trimmed.substring(0, 1).toUpperCase(Locale.ROOT);
            }
            return "??";
        }
    }
}
