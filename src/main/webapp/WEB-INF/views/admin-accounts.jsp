<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Locale"%>
<%@ page import="com.bankingsystem.entity.AdminAccountView"%>
<%@ page import="com.bankingsystem.entity.AdminCustomerSummary"%>
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
    String successMessage = (String) request.getAttribute("accountSuccess");
    String formError = (String) request.getAttribute("accountFormError");
    Boolean accountFormVisibleAttr = (Boolean) request.getAttribute("accountFormVisible");
    boolean accountFormVisible = accountFormVisibleAttr != null && accountFormVisibleAttr;
    Map<String, String> newAccountForm = (Map<String, String>) request.getAttribute("newAccountForm");
    if (newAccountForm == null) {
        newAccountForm = java.util.Collections.emptyMap();
    }
    Long filterUserId = (Long) request.getAttribute("accountFilterUserId");
    List<AdminAccountView> accounts = (List<AdminAccountView>) request.getAttribute("adminAccounts");
    if (accounts == null) {
        accounts = java.util.Collections.emptyList();
    }
    List<AdminCustomerSummary> accountCustomers = (List<AdminCustomerSummary>) request.getAttribute("accountCustomers");
    if (accountCustomers == null) {
        accountCustomers = java.util.Collections.emptyList();
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
            <button class="btn btn-primary" type="button" id="openAccountForm">+ Open New Account</button>
        </section>

        <section class="account-form-panel <%= accountFormVisible ? "" : "hidden" %>" id="accountFormPanel">
            <h3>Open New Account</h3>
            <p class="muted">Hangi müşteri için hangi para biriminde hesap açmak istediğini seç.</p>
            <%
                if (formError != null) {
            %>
            <div class="alert alert-error"><%= formError %></div>
            <%
                }
            %>
            <form method="post" class="account-form">
                <input type="hidden" name="action" value="addAccount" />
                <div class="form-grid">
                    <label>
                        <span>Customer *</span>
                        <select name="accountUserId" required>
                            <option value="">Select Customer</option>
                            <%
                                for (AdminCustomerSummary customer : accountCustomers) {
                                    String idStr = String.valueOf(customer.getId());
                                    String selected = idStr.equals(newAccountForm.get("accountUserId")) ? "selected" : "";
                            %>
                            <option value="<%= customer.getId() %>" <%= selected %>><%= customer.getFullName() %></option>
                            <%
                                }
                            %>
                        </select>
                    </label>
                    <label>
                        <span>Account Name *</span>
                        <input type="text" name="accountName" required value="<%= newAccountForm.getOrDefault("accountName", "") %>" />
                    </label>
                    <label>
                        <span>IBAN</span>
                        <input type="text" name="iban" value="<%= newAccountForm.getOrDefault("iban", "") %>" />
                    </label>
                    <label>
                        <span>Currency *</span>
                        <select name="currency" required>
                            <%
                                String selectedCurrency = newAccountForm.getOrDefault("currency", "");
                                String[] currencies = {"TRY", "USD", "EUR", "GBP", "XAU"};
                                for (String curr : currencies) {
                            %>
                            <option value="<%= curr %>" <%= curr.equalsIgnoreCase(selectedCurrency) ? "selected" : "" %>><%= curr %></option>
                            <%
                                }
                            %>
                        </select>
                    </label>
                    <label>
                        <span>Initial Balance</span>
                        <input type="number" step="0.01" name="balance" value="<%= newAccountForm.getOrDefault("balance", "") %>" />
                    </label>
                    <label>
                        <span>Status</span>
                        <select name="status">
                            <%
                                String selectedStatus = newAccountForm.getOrDefault("status", "ACTIVE").toUpperCase();
                                String[][] statuses = {{"ACTIVE","Active"},{"FROZEN","Frozen"},{"CLOSED","Closed"}};
                                for (String[] option : statuses) {
                            %>
                            <option value="<%= option[0] %>" <%= option[0].equalsIgnoreCase(selectedStatus) ? "selected" : "" %>><%= option[1] %></option>
                            <%
                                }
                            %>
                        </select>
                    </label>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Create Account</button>
                    <button type="button" class="btn btn-outline" id="cancelAccountForm">Cancel</button>
                </div>
            </form>
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
                    if (successMessage != null) {
                %>
                <div class="alert alert-success"><%= successMessage %></div>
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
<script>
    (function () {
        const formPanel = document.getElementById('accountFormPanel');
        const openBtn = document.getElementById('openAccountForm');
        const cancelBtn = document.getElementById('cancelAccountForm');
        const showForm = () => formPanel && formPanel.classList.remove('hidden');
        const hideForm = () => formPanel && formPanel.classList.add('hidden');
        if (openBtn) {
            openBtn.addEventListener('click', showForm);
        }
        if (cancelBtn) {
            cancelBtn.addEventListener('click', hideForm);
        }
    })();
</script>
</html>
