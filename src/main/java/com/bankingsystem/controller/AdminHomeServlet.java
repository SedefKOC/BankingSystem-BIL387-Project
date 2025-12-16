package com.bankingsystem.controller;

import com.bankingsystem.entity.UserRole;
import com.bankingsystem.service.AdminDashboardService;
import com.bankingsystem.service.AdminDashboardService.AdminDashboardMetrics;
import com.bankingsystem.service.AdminDashboardService.AdminTransactionView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AdminHomeServlet extends HttpServlet {
    private final AdminDashboardService dashboardService = new AdminDashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session, UserRole.ADMIN)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String displayName = asString(resolveDisplayName(session));
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("adminNavActive", "dashboard");
        try {
            AdminDashboardMetrics metrics = dashboardService.loadMetrics();
            List<AdminTransactionView> transactions = dashboardService.loadRecentTransactions(15);
            req.setAttribute("adminMetrics", metrics);
            req.setAttribute("recentAdminTransactions", transactions);
        } catch (SQLException e) {
            req.setAttribute("adminDashboardError", "Dashboard verileri yüklenemedi.");
            req.setAttribute("adminMetrics", null);
            req.setAttribute("recentAdminTransactions", Collections.emptyList());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin-home.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session, UserRole requiredRole) {
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("currentUserRole");
        return role == requiredRole;
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
}
