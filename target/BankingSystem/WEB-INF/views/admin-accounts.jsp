<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
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
    List<Map<String, String>> accounts = (List<Map<String, String>>) request.getAttribute("adminAccounts");
    if (accounts == null) {
        accounts = java.util.Collections.emptyList();
    }
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
                <p class="muted">Hesapları para birimi, durum veya müşteri adına göre takip edebilirsin.</p>
            </div>
            <div class="header-actions">
                <div class="search">
                    <input type="search" placeholder="Search accounts, IBAN, or customers" />
                </div>
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
            <div class="filter-controls">
                <input type="text" placeholder="Account Number or Customer Name" />
                <select>
                    <option>All Currencies</option>
                    <option>TRY</option>
                    <option>USD</option>
                    <option>EUR</option>
                    <option>XAU</option>
                </select>
                <select>
                    <option>All Statuses</option>
                    <option>Active</option>
                    <option>Frozen</option>
                    <option>Closed</option>
                </select>
                <button class="btn btn-outline">Filter</button>
            </div>
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
                <span>ACTIONS</span>
            </div>
            <div class="accounts-table__body">
                <%
                    for (Map<String, String> row : accounts) {
                        String number = row.get("number");
                        String created = row.get("created");
                        String customer = row.get("customer");
                        String initialsCell = row.get("initials");
                        String type = row.get("type");
                        String currency = row.get("currency");
                        String balance = row.get("balance");
                        String balanceClass = row.get("balanceClass");
                        String status = row.get("status");
                        String statusClass = row.get("statusClass");
                %>
                <div class="account-row">
                    <div class="account-number">
                        <strong><%= number %></strong>
                        <span class="muted"><%= created %></span>
                    </div>
                    <div class="account-customer">
                        <div class="customer-avatar"><%= initialsCell %></div>
                        <div>
                            <strong><%= customer %></strong>
                        </div>
                    </div>
                    <div class="account-type"><%= type %></div>
                    <div class="account-currency"><%= currency %></div>
                    <div class="account-balance <%= balanceClass %>"><%= balance %></div>
                    <div class="account-status">
                        <span class="status-pill <%= statusClass %>"><%= status %></span>
                    </div>
                    <div class="account-actions">
                        <button class="btn btn-outline">Details</button>
                        <%
                            if ("status-frozen".equals(statusClass)) {
                        %>
                        <button class="btn btn-success">Unfreeze</button>
                        <%
                            } else if ("status-closed".equals(statusClass)) {
                        %>
                        <button class="btn btn-outline">History</button>
                        <%
                            } else {
                        %>
                        <button class="btn btn-outline">Edit</button>
                        <%
                            }
                        %>
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
