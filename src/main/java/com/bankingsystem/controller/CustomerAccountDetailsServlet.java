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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Map<Long, AccountSummary> accountMap = sampleAccounts();
        AccountSummary account = accountId == null ? null : accountMap.get(accountId);
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/accounts");
            return;
        }
        req.setAttribute("account", account);

        Long userId = (Long) session.getAttribute("currentUserId");
        BigDecimal totalBalance = BigDecimal.ZERO;
        if (userId != null) {
            totalBalance = dashboardService.getTotalBalance(userId);
        }
        req.setAttribute("totalBalance", totalBalance);
        req.setAttribute("accountTransactions", sampleTransactions(account));
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

    private Map<Long, AccountSummary> sampleAccounts() {
        List<AccountSummary> list = List.of(
                new AccountSummary(1L, "Checking Account / Vadesiz TL", "TR45 0001 2980 4582 1923 01", "₺", new BigDecimal("24582.90"), "TRY"),
                new AccountSummary(2L, "USD Savings / Vadeli", "TR45 9001 3008 1284 5592 02", "$", new BigDecimal("12450.00"), "USD"),
                new AccountSummary(3L, "Gold Investment / Altın", "TR45 0001 4080 5521 8821 03", "gr", new BigDecimal("120.5"), "XAU")
        );
        return list.stream().collect(java.util.stream.Collectors.toMap(AccountSummary::getId, a -> a));
    }

    private List<TransactionRecord> sampleTransactions(AccountSummary account) {
        List<TransactionRecord> records = new ArrayList<>();
        records.add(new TransactionRecord("Market Shopping", LocalDateTime.now().minusDays(1).withHour(14).withMinute(30), new BigDecimal("-450.90")));
        records.add(new TransactionRecord("Rent Payment", LocalDateTime.now().minusDays(2).withHour(8).withMinute(15), new BigDecimal("-12500.00")));
        records.add(new TransactionRecord("Salary Deposit", LocalDateTime.now().minusDays(7).withHour(9).withMinute(0), new BigDecimal("65000.00")));
        records.add(new TransactionRecord("ATM Withdrawal", LocalDateTime.now().minusDays(8).withHour(16).withMinute(20), new BigDecimal("-2000.00")));
        records.add(new TransactionRecord("Currency Exchange", LocalDateTime.now().minusDays(10).withHour(11).withMinute(45), new BigDecimal("-619.00")));
        return records;
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
