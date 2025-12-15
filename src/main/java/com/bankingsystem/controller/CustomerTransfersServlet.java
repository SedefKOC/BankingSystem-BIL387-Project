package com.bankingsystem.controller;

import com.bankingsystem.entity.AccountSummary;
import com.bankingsystem.entity.TransactionRecord;
import com.bankingsystem.entity.UserRole;
import com.bankingsystem.service.CustomerDashboardService;
import com.bankingsystem.service.CustomerTransferService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CustomerTransfersServlet extends HttpServlet {
    private final CustomerDashboardService dashboardService = new CustomerDashboardService();
    private final CustomerTransferService transferService = new CustomerTransferService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        populatePageAttributes(req, session);
        req.getRequestDispatcher("/WEB-INF/views/customer-transfers.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        Long userId = (Long) session.getAttribute("currentUserId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String dateRaw = req.getParameter("transactionDate");
        String amountRaw = req.getParameter("amount");
        java.time.LocalDate date = parseDate(dateRaw);
        java.math.BigDecimal amount = parseAmount(amountRaw);
        CustomerTransferService.TransferResult result;
        if (date == null) {
            result = CustomerTransferService.TransferResult.error("Lütfen geçerli bir tarih seç.");
        } else if (amount == null || amount.signum() <= 0) {
            result = CustomerTransferService.TransferResult.error("Tutar 0'dan büyük olmalı.");
        } else if ("myAccounts".equals(action)) {
            long fromId = parseLong(req.getParameter("fromAccountId"));
            long toId = parseLong(req.getParameter("toAccountId"));
            result = transferService.transferBetweenMyAccounts(
                    userId, fromId, toId, amount, date, req.getParameter("description"));
        } else if ("otherPerson".equals(action)) {
            long fromId = parseLong(req.getParameter("fromAccountId"));
            String toIban = req.getParameter("toAccountIban");
            if (toIban == null || toIban.isBlank()) {
                result = CustomerTransferService.TransferResult.error("IBAN boş olamaz.");
            } else {
                result = transferService.transferToIban(
                        userId, fromId, toIban, amount, date, req.getParameter("description"));
            }
        } else {
            result = CustomerTransferService.TransferResult.error("Geçersiz transfer tipi.");
        }
        req.setAttribute(result.success() ? "successMessage" : "errorMessage", result.message());
        populatePageAttributes(req, session);
        req.getRequestDispatcher("/WEB-INF/views/customer-transfers.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session) {
        return session != null && session.getAttribute("currentUserRole") == UserRole.CUSTOMER;
    }

    private String resolveDisplayName(HttpSession session) {
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

    private List<TransactionRecord> sampleTransfers() {
        return List.of(
                new TransactionRecord("Ahmet Demir", LocalDateTime.now().minusDays(1).withHour(8).withMinute(15), java.math.BigDecimal.valueOf(-2500)),
                new TransactionRecord("Elif Demir", LocalDateTime.now().minusDays(3).withHour(14).withMinute(30), java.math.BigDecimal.valueOf(-450)),
                new TransactionRecord("Gold Purchase", LocalDateTime.now().minusDays(5).withHour(11).withMinute(20), java.math.BigDecimal.valueOf(-8200)),
                new TransactionRecord("Mehmet Can", LocalDateTime.now().minusDays(6).withHour(8).withMinute(45), java.math.BigDecimal.valueOf(-150)),
                new TransactionRecord("Card Payment", LocalDateTime.now().minusDays(7).withHour(16).withMinute(0), java.math.BigDecimal.valueOf(-12400))
        );
    }

    private void populatePageAttributes(HttpServletRequest req, HttpSession session) {
        String displayName = resolveDisplayName(session);
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("navActive", "transfers");
        Long userId = (Long) session.getAttribute("currentUserId");
        List<AccountSummary> accounts = userId == null ? Collections.emptyList() : dashboardService.getAccounts(userId);
        req.setAttribute("transferAccounts", accounts);
        req.setAttribute("transferHistory", sampleTransfers());
    }

    private java.math.BigDecimal parseAmount(String raw) {
        try {
            return raw == null ? null : new java.math.BigDecimal(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private java.time.LocalDate parseDate(String raw) {
        try {
            return raw == null || raw.isBlank() ? null : java.time.LocalDate.parse(raw);
        } catch (java.time.format.DateTimeParseException e) {
            return null;
        }
    }

    private long parseLong(String raw) {
        try {
            return raw == null ? 0 : Long.parseLong(raw);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
