<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Locale"%>
<%@ page import="com.bankingsystem.entity.AdminAccountView"%>
<%
    String username = (String) request.getAttribute("homeUsername");
    if (username == null) {
        username = "";
    }
    String initials = (String) request.getAttribute("homeInitials");
    if (initials == null || initials.isBlank()) {
        initials = "??";
    }
    String navActive = (String) request.getAttribute("adminNavActive");
    String errorMessage = (String) request.getAttribute("adminAccountError");
    Long filterUserId = (Long) request.getAttribute("accountFilterUserId");
    List<AdminAccountView> accounts = (List<AdminAccountView>) request.getAttribute("adminAccounts");
    if (accounts == null) {
        accounts = java.util.Collections.emptyList();
    }
    DateTimeFormatter createdFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Admin Accounts</title>
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
                <h1>Account Management</h1>
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

        <section class="customer-filter-card">
            <button class="btn btn-primary">+ Open New Account</button>
        </section>

        <section class="accounts-table">
            <div class="accounts-table__header">
                <span>ACCOUNT NUMBER</span>
                <span>CUSTOMER</span>
                <span>TYPE</span>
                <span>CURRENCY</span>
                <span>BALANCE</span>
                <span>STATUS</span>
            </div>
            <div class="accounts-table__body">
                <%
                    if (filterUserId != null) {
                %>
                <div class="filter-pill">Filtered by User ID: <%= filterUserId %></div>
                <%
                    }
                    if (errorMessage != null) {
                %>
                <div class="alert alert-error"><%= errorMessage %></div>
                <%
                    }
                    java.text.DecimalFormat amountFormat = new java.text.DecimalFormat("#,##0.00");
                    for (AdminAccountView account : accounts) {
                        boolean positive = account.getBalance() != null && account.getBalance().signum() >= 0;
                        String balanceClass = positive ? "balance-positive" : "balance-negative";
                        String status = account.getStatus() == null ? "ACTIVE" : account.getStatus();
                        String statusClass;
                        if ("FROZEN".equalsIgnoreCase(status)) {
                            statusClass = "status-frozen";
                        } else if ("CLOSED".equalsIgnoreCase(status)) {
                            statusClass = "status-closed";
                        } else {
                            statusClass = "status-active";
                        }
                %>
                <div class="account-row">
                    <div class="account-number">
                        <strong><%= account.getIban() == null ? "-" : account.getIban() %></strong>
                        <span class="muted"><%= account.getCreatedDate() == null ? "-" : account.getCreatedDate().format(createdFormatter) %></span>
                    </div>
                    <div class="account-customer">
                        <div class="customer-avatar"><%= account.getCustomerInitials() %></div>
                        <div>
                            <strong><%= account.getCustomerName() %></strong>
                        </div>
                    </div>
                    <div class="account-type"><%= account.getAccountType() == null ? "-" : account.getAccountType() %></div>
                    <div class="account-currency"><%= account.getCurrency() == null ? "-" : account.getCurrency() %></div>
                    <div class="account-balance <%= balanceClass %>">
                        <%
                            if (account.getBalance() == null) {
                        %>
                        -
                        <%
                            } else {
                                String currencyLabel = account.getCurrency() == null ? "" : account.getCurrency().toUpperCase();
                        %>
                        <%= (positive ? "" : "- ") + currencyLabel + " " + amountFormat.format(account.getBalance().abs()) %>
                        <%
                            }
                        %>
                    </div>
                    <div class="account-status">
                        <span class="status-pill <%= statusClass %>"><%= status %></span>
                    </div>
                </div>
                <%
                    }
                    if (accounts.isEmpty()) {
                %>
                <p class="muted">Henüz hesap kaydı bulunmuyor.</p>
                <%
                    }
                %>
            </div>
        </section>
    </main>
</div>
</body>
</html>
