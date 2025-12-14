package com.bankingsystem.controller;

import com.bankingsystem.util.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter writer = resp.getWriter()) {
            try (Connection ignored = DBConnectionManager.getConnection()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.println("DB connection successful: BankingSystemDb");
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.println("DB connection FAILED");
                writer.println(e.getMessage());
            }
        }
    }
}
