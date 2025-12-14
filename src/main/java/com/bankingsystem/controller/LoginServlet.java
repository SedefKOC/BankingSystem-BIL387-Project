package com.bankingsystem.controller;

import com.bankingsystem.entity.UserRole;
import com.bankingsystem.service.AuthService;
import com.bankingsystem.service.AuthService.AuthResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        AuthResult result = authService.authenticate(
                req.getParameter("role"),
                req.getParameter("username"),
                req.getParameter("password")
        );

        if (result.success()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUserRole", result.role());
            session.setAttribute("currentUsername", req.getParameter("username"));
            String redirect = result.role() == UserRole.ADMIN ? "/admin/home" : "/customer/home";
            resp.sendRedirect(req.getContextPath() + redirect);
            return;
        } else {
            req.setAttribute("errorMessage", result.message());
        }

        req.setAttribute("selectedRole", roleParam(req, result.role()));
        req.setAttribute("typedUsername", sanitizeForDisplay(req.getParameter("username")));
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private String roleParam(HttpServletRequest req, UserRole role) {
        if (role != null) {
            return role.name();
        }
        String raw = req.getParameter("role");
        return raw == null ? "" : raw;
    }

    private String sanitizeForDisplay(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
