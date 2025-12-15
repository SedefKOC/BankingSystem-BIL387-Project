package com.bankingsystem.controller;

import com.bankingsystem.entity.UserProfile;
import com.bankingsystem.entity.UserRole;
import com.bankingsystem.service.CustomerProfileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class CustomerProfileServlet extends HttpServlet {
    private final CustomerProfileService profileService = new CustomerProfileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        renderProfile(req, resp, session);
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
        String action = req.getParameter("action");
        if ("personal".equals(action)) {
            handlePersonalUpdate(req, userId);
        } else if ("password".equals(action)) {
            handlePasswordUpdate(req, userId);
        }
        renderProfile(req, resp, session);
    }

    private void handlePersonalUpdate(HttpServletRequest req, long userId) {
        UserProfile profile = new UserProfile(
                userId,
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                req.getParameter("email"),
                req.getParameter("phoneNumber"),
                req.getParameter("nationalId"),
                req.getParameter("birthDate"),
                req.getParameter("address")
        );
        boolean success = profileService.updatePersonalInfo(profile);
        req.setAttribute(success ? "successMessage" : "errorMessage",
                success ? "Bilgilerin güncellendi." : "Bilgiler güncellenirken bir sorun oluştu.");
    }

    private void handlePasswordUpdate(HttpServletRequest req, long userId) {
        String newPassword = req.getParameter("newPassword");
        if (newPassword == null || newPassword.isBlank()) {
            req.setAttribute("errorMessage", "Yeni şifre boş olamaz.");
            return;
        }
        boolean success = profileService.updatePassword(userId, newPassword);
        req.setAttribute(success ? "successMessage" : "errorMessage",
                success ? "Şifren güncellendi." : "Şifre güncellenemedi.");
    }

    private void renderProfile(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws ServletException, IOException {
        Long userId = (Long) session.getAttribute("currentUserId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String displayName = resolveDisplayName(session);
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("navActive", "profile");
        req.setAttribute("customerId", userId);
        Optional<UserProfile> profileOpt = profileService.getProfile(userId);
        UserProfile profile = profileOpt.orElse(new UserProfile(userId, "", "", "", "", "", "", ""));
        req.setAttribute("profile", profile);
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
            return trimmed.substring(0, 2).toUpperCase(Locale.ROOT);
        }
        if (!trimmed.isEmpty()) {
            return trimmed.substring(0, 1).toUpperCase(Locale.ROOT);
        }
        return "??";
    }
}
