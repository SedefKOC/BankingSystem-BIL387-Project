package com.bankingsystem.controller;

import com.bankingsystem.entity.TransactionRecord;
import com.bankingsystem.entity.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerTransfersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String displayName = resolveDisplayName(session);
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("navActive", "transfers");
        req.setAttribute("transferHistory", sampleTransfers());
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
}
