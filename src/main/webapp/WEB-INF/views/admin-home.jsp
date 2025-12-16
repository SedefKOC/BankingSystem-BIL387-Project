<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="com.bankingsystem.service.AdminDashboardService.AdminDashboardMetrics"%>
<%@ page import="com.bankingsystem.service.AdminDashboardService.AdminTransactionView"%>
<%
    String username = (String) request.getAttribute("homeUsername");
    if (username == null) {
        username = "";
    }
    String initials = (String) request.getAttribute("homeInitials");
    if (initials == null || initials.isBlank()) {
        initials = "??";
    }
    AdminDashboardMetrics metrics = (AdminDashboardMetrics) request.getAttribute("adminMetrics");
    String dashboardError = (String) request.getAttribute("adminDashboardError");
    List<AdminTransactionView> recentTransactions = (List<AdminTransactionView>) request.getAttribute("recentAdminTransactions");
    if (recentTransactions == null) {
        recentTransactions = java.util.Collections.emptyList();
    }
    java.text.DecimalFormat numberFormat = new java.text.DecimalFormat("#,##0");
    java.text.DecimalFormat amountFormat = new java.text.DecimalFormat("#,##0.00");
    DateTimeFormatter tableFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    long activeCustomers = metrics == null ? 0 : metrics.activeCustomers();
    long activeAccounts = metrics == null ? 0 : metrics.activeAccounts();
    java.math.BigDecimal todaysVolume = metrics == null ? java.math.BigDecimal.ZERO : metrics.todaysVolume();
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Admin Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/admin-dashboard.css" />
</head>
<body>
<div class="admin-dashboard">
    <aside class="admin-sidebar">
        <div class="admin-sidebar__brand">
            <img src="<%=request.getContextPath()%>/assets/img/sedefbank-logo.svg" alt="SedefBank Logo" class="logo-image" />
            <div>
                <span>SedefBank</span>
                <small>Admin Portal</small>
            </div>
        </div>
        <%
            String navActive = (String) request.getAttribute("adminNavActive");
        %>
        <nav class="admin-sidebar__nav">
            <a class="nav-item <%= "dashboard".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a>
            <a class="nav-item <%= "customers".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/admin/customers">Customers</a>
            <a class="nav-item <%= "accounts".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/admin/accounts">Accounts</a>
        </nav>
        <a class="logout" href="<%= request.getContextPath() %>/">Logout System</a>
    </aside>

    <main class="admin-content">
        <header class="admin-header">
            <div>
                <h1>Admin Dashboard</h1>
                <p class="muted">Hello <%= username %>, you can monitor the system status from here.</p>
            </div>
            <div class="header-actions">
                <div class="profile-chip">
                    <div>
                        <strong><%= username %></strong>
                        <span>Super Admin</span>
                    </div>
                    <div class="chip-avatar"><%= initials %></div>
                </div>
            </div>
        </header>

        <section class="admin-stats">
            <article class="stat-card">
                <p>Active Customers</p>
                <h2><%= numberFormat.format(activeCustomers) %></h2>
            </article>
            <article class="stat-card">
                <p>Active Accounts</p>
                <h2><%= numberFormat.format(activeAccounts) %></h2>
            </article>
            <article class="stat-card">
                <p>Today's Volume</p>
                <h2>₺ <%= amountFormat.format(todaysVolume.abs()) %></h2>
                <span class="<%= todaysVolume.signum() >= 0 ? "positive" : "negative" %>">
                </span>
            </article>
        </section>

        <section class="admin-table">
            <header>
                <h3>Recent Transactions</h3>
            </header>
            <%
                if (dashboardError != null) {
            %>
            <div class="alert alert-error"><%= dashboardError %></div>
            <%
                }
                if (recentTransactions.isEmpty()) {
            %>
            <p class="muted">Henüz işlem kaydı bulunmuyor.</p>
            <%
                } else {
            %>
            <table>
                <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Customer</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>Date & Time</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (AdminTransactionView tx : recentTransactions) {
                        boolean positiveAmount = tx.amount().signum() >= 0;
                %>
                <tr>
                    <td>#TRX-<%= tx.id() %></td>
                    <td><%= tx.customerName() %></td>
                    <td><%= tx.description() %></td>
                    <td class="<%= positiveAmount ? "positive" : "negative" %>">
                        ₺ <%= amountFormat.format(tx.amount().abs()) %>
                    </td>
                    <td><%= tx.transactionDate() == null ? "-" : tx.transactionDate().format(tableFormatter) %></td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
            <%
                }
            %>
        </section>
    </main>
</div>
</body>
</html>
