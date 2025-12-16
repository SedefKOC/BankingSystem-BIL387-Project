package com.bankingsystem.controller;

import com.bankingsystem.dao.AdminCustomerDao;
import com.bankingsystem.dao.impl.JdbcAdminCustomerDao;
import com.bankingsystem.entity.AdminCustomerSummary;
import com.bankingsystem.entity.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCustomersServlet extends HttpServlet {
    private final AdminCustomerDao customerDao;

    public AdminCustomersServlet() {
        this(new JdbcAdminCustomerDao());
    }

    public AdminCustomersServlet(AdminCustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        prepareSessionAttributes(req, session);
        populateCustomers(req);
        req.getRequestDispatcher("/WEB-INF/views/admin-customers.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        prepareSessionAttributes(req, session);
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            handleAddCustomer(req);
        } else {
            req.setAttribute("customerFormError", "Geçersiz işlem.");
        }
        populateCustomers(req);
        req.getRequestDispatcher("/WEB-INF/views/admin-customers.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("currentUserRole");
        return role == UserRole.ADMIN;
    }

    private void prepareSessionAttributes(HttpServletRequest req, HttpSession session) {
        String displayName = asString(resolveDisplayName(session));
        req.setAttribute("homeUsername", sanitize(displayName));
        req.setAttribute("homeInitials", initials(displayName));
        req.setAttribute("adminNavActive", "customers");
    }

    private void populateCustomers(HttpServletRequest req) {
        try {
            List<AdminCustomerSummary> customers = customerDao.findAllCustomers();
            req.setAttribute("adminCustomers", customers);
        } catch (java.sql.SQLException e) {
            req.setAttribute("adminCustomerError", "Müşteri listesi yüklenemedi.");
            req.setAttribute("adminCustomers", java.util.Collections.emptyList());
        }
    }

    private void handleAddCustomer(HttpServletRequest req) {
        Map<String, String> form = new HashMap<>();
        String firstName = capture(form, "firstName", req.getParameter("firstName"));
        String lastName = capture(form, "lastName", req.getParameter("lastName"));
        String email = capture(form, "email", req.getParameter("email"));
        String phone = capture(form, "phone", req.getParameter("phone"));
        String nationalId = capture(form, "nationalId", req.getParameter("nationalId"));
        String username = capture(form, "username", req.getParameter("username"));
        String password = capture(form, "password", req.getParameter("password"));
        req.setAttribute("newCustomerForm", form);
        req.setAttribute("customerFormVisible", true);

        if (firstName.isEmpty() || lastName.isEmpty()) {
            req.setAttribute("customerFormError", "Lütfen ad ve soyadı gir.");
            return;
        }
        if (username.isEmpty() || password.isEmpty()) {
            req.setAttribute("customerFormError", "Kullanıcı adı ve şifre boş bırakılamaz.");
            return;
        }
        try {
            long newId = customerDao.createCustomer(username, password, firstName, lastName, email, phone, nationalId);
            req.setAttribute("customerSuccess", firstName + " " + lastName + " (#" + newId + ") sisteme eklendi.");
            req.setAttribute("newCustomerForm", null);
            req.setAttribute("customerFormVisible", false);
        } catch (java.sql.SQLException e) {
            req.setAttribute("customerFormError", "Müşteri eklenirken hata oluştu. Kullanıcı adı daha önce kullanılmış olabilir.");
        }
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

    private String capture(Map<String, String> map, String key, String raw) {
        String value = raw == null ? "" : raw.trim();
        map.put(key, value);
        return value;
    }
}
