package com.bankingsystem.controller;

import com.bankingsystem.entity.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCustomersServlet extends HttpServlet {
    private static final List<Map<String, Object>> SAMPLE_CUSTOMERS = createSampleCustomers();

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
        req.setAttribute("adminNavActive", "customers");
        req.setAttribute("adminCustomers", SAMPLE_CUSTOMERS);
        req.getRequestDispatcher("/WEB-INF/views/admin-customers.jsp").forward(req, resp);
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

    private static List<Map<String, Object>> createSampleCustomers() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(customer("Ahmet Demir", "AD", "ID: 12345678901",
                "ahmet.d@example.com", "+90 565 123 45 67", LocalDate.of(2023, 11, 15),
                "Active", "status-active"));
        list.add(customer("Elif Demir", "ED", "ID: 48291283012",
                "elif.demir@example.com", "+90 532 987 65 43", LocalDate.of(2024, 8, 22),
                "Active", "status-active"));
        list.add(customer("Mehmet Can", "MC", "ID: 33210409212",
                "mehmet.c@company.net", "+90 544 222 11 00", LocalDate.of(2025, 1, 5),
                "Suspended", "status-suspended"));
        list.add(customer("Selin Yılmaz", "SY", "ID: 99128347100",
                "selin.y@example.com", "+90 505 111 22 33", LocalDate.of(2024, 10, 10),
                "Active", "status-active"));
        return list;
    }

    private static Map<String, Object> customer(String name, String initials, String idLabel,
                                                String email, String phone, LocalDate joinDate,
                                                String status, String statusClass) {
        Map<String, Object> row = new HashMap<>();
        row.put("name", name);
        row.put("initials", initials);
        row.put("idLabel", idLabel);
        row.put("email", email);
        row.put("phone", phone);
        row.put("joinDate", joinDate);
        row.put("status", status);
        row.put("statusClass", statusClass);
        return row;
    }
}
