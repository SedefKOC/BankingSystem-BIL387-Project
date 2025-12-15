package com.bankingsystem.controller;

import com.bankingsystem.entity.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CustomerProfileServlet extends HttpServlet {
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
        req.setAttribute("navActive", "profile");
        req.setAttribute("customerId", session.getAttribute("currentUserId"));
        req.getRequestDispatcher("/WEB-INF/views/customer-profile.jsp").forward(req, resp);
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
}
