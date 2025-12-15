package com.bankingsystem.controller;

import com.bankingsystem.entity.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAccountsServlet extends HttpServlet {
    private static final List<Map<String, String>> SAMPLE_ACCOUNTS = createSampleAccounts();

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
        req.setAttribute("adminAccounts", SAMPLE_ACCOUNTS);
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

    private static List<Map<String, String>> createSampleAccounts() {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(account(
                "TR45 0001 2000 4582 1923",
                "Created: Nov 10, 2024",
                "Ahmet Demir",
                "AD",
                "Checking",
                "TRY",
                "₺ 24,582.90",
                "balance-positive",
                "Active",
                "status-active"));
        list.add(account(
                "TR45 0001 3000 9291 0055",
                "Created: Aug 22, 2024",
                "Elif Demir",
                "ED",
                "Savings",
                "USD",
                "$ 12,450.00",
                "balance-positive",
                "Active",
                "status-active"));
        list.add(account(
                "TR45 0001 1022 3344",
                "Created: Jan 16, 2023",
                "Mehmet Can",
                "MC",
                "Corporate",
                "TRY",
                "₺ 1,205,000.00",
                "balance-positive",
                "Frozen",
                "status-frozen"));
        list.add(account(
                "TR45 0001 4000 5521 8821",
                "Created: Oct 10, 2024",
                "Selin Yılmaz",
                "SY",
                "Investment",
                "XAU",
                "120 gr",
                "balance-positive",
                "Active",
                "status-active"));
        list.add(account(
                "TR45 0001 2600 6666 7777",
                "Created: Dec 12, 2022",
                "Ahmet Demir",
                "AD",
                "Credit Card",
                "TRY",
                "- ₺ 5,420.00",
                "balance-negative",
                "Closed",
                "status-closed"));
        return list;
    }

    private static Map<String, String> account(String number, String created, String customer,
                                               String initials, String type, String currency,
                                               String balance, String balanceClass,
                                               String status, String statusClass) {
        Map<String, String> map = new HashMap<>();
        map.put("number", number);
        map.put("created", created);
        map.put("customer", customer);
        map.put("initials", initials);
        map.put("type", type);
        map.put("currency", currency);
        map.put("balance", balance);
        map.put("balanceClass", balanceClass);
        map.put("status", status);
        map.put("statusClass", statusClass);
        return map;
    }
}
