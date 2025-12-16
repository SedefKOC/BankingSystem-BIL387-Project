<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Locale"%>
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
    String errorMessage = (String) request.getAttribute("adminCustomerError");
    String formError = (String) request.getAttribute("customerFormError");
    String successMessage = (String) request.getAttribute("customerSuccess");
    Boolean formVisibleAttr = (Boolean) request.getAttribute("customerFormVisible");
    boolean formVisible = formVisibleAttr != null && formVisibleAttr;
    Map<String, String> newCustomerForm = (Map<String, String>) request.getAttribute("newCustomerForm");
    if (newCustomerForm == null) {
        newCustomerForm = java.util.Collections.emptyMap();
    }
    List<AdminCustomerSummary> customers = (List<AdminCustomerSummary>) request.getAttribute("adminCustomers");
    if (customers == null) {
        customers = java.util.Collections.emptyList();
    }
    DateTimeFormatter joinFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Admin Customers</title>
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
                <h1>Customer Management</h1>
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
            <button class="btn btn-secondary" type="button" id="openCustomerForm">+ Add New Customer</button>
        </section>

        <section class="customer-form-panel <%= formVisible ? "" : "hidden" %>" id="customerFormPanel">
            <h3>Add New Customer</h3>
            <p class="muted">You can create a new customer by filling in the basic information and setting a username and password.</p>
            <%
                if (formError != null) {
            %>
            <div class="alert alert-error"><%= formError %></div>
            <%
                }
            %>
            <form method="post" class="new-customer-form">
                <input type="hidden" name="action" value="add" />
                <div class="form-grid">
                    <label>
                        <span>First Name *</span>
                        <input type="text" name="firstName" required value="<%= newCustomerForm.getOrDefault("firstName", "") %>" />
                    </label>
                    <label>
                        <span>Last Name *</span>
                        <input type="text" name="lastName" required value="<%= newCustomerForm.getOrDefault("lastName", "") %>" />
                    </label>
                    <label>
                        <span>Email</span>
                        <input type="email" name="email" value="<%= newCustomerForm.getOrDefault("email", "") %>" />
                    </label>
                    <label>
                        <span>Phone</span>
                        <input type="text" name="phone" value="<%= newCustomerForm.getOrDefault("phone", "") %>" />
                    </label>
                    <label>
                        <span>National ID</span>
                        <input type="text" name="nationalId" value="<%= newCustomerForm.getOrDefault("nationalId", "") %>" />
                    </label>
                    <label>
                        <span>Username *</span>
                        <input type="text" name="username" required value="<%= newCustomerForm.getOrDefault("username", "") %>" />
                    </label>
                    <label>
                        <span>Password *</span>
                        <input type="password" name="password" required />
                    </label>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <button type="button" class="btn btn-outline" id="cancelCustomerForm">Cancel</button>
                </div>
            </form>
        </section>

        <section class="customer-table">
            <div class="customer-table__header">
                <span>CUSTOMER NAME / ID</span>
                <span>CONTACT INFO</span>
                <span>JOIN DATE</span>
                <span>ACTIONS</span>
            </div>
            <div class="customer-table__body">
                <%
                    if (successMessage != null) {
                %>
                <div class="alert alert-success"><%= successMessage %></div>
                <%
                    }
                %>
                <%
                    if (errorMessage != null) {
                %>
                <div class="alert alert-error"><%= errorMessage %></div>
                <%
                    }
                    for (AdminCustomerSummary customer : customers) {
                        java.time.LocalDate joinDate = customer.getJoinDate();
                %>
                <div class="customer-row">
                    <div class="customer-meta">
                        <div class="customer-avatar"><%= customer.getInitials() %></div>
                        <div>
                            <strong><%= customer.getFullName() %></strong><br>
                            <span class="muted"><%= customer.getIdentifier() == null ? "ID: " + customer.getId() : "ID: " + customer.getIdentifier() %></span>
                        </div>
                    </div>
                    <div class="customer-contact">
                        <div><%= customer.getEmail() == null ? "-" : customer.getEmail() %></div>
                        <small class="muted"><%= customer.getPhone() == null ? "-" : customer.getPhone() %></small>
                    </div>
                    <div class="customer-joined"><%= joinDate == null ? "-" : joinDate.format(joinFormatter) %></div>
                    <div class="customer-actions">
                        <a class="btn btn-outline" href="<%= request.getContextPath() %>/admin/accounts?userId=<%= customer.getId() %>">View Accounts</a>
                    </div>
                </div>
                <%
                    }
                    if (customers.isEmpty()) {
                %>
                <p class="muted">Henüz müşteri kaydı bulunmuyor.</p>
                <%
                    }
                %>
            </div>
        </section>
    </main>
</div>
<script>
    (function () {
        const formPanel = document.getElementById('customerFormPanel');
        const openBtn = document.getElementById('openCustomerForm');
        const cancelBtn = document.getElementById('cancelCustomerForm');
        const showForm = () => formPanel && formPanel.classList.remove('hidden');
        const hideForm = () => formPanel && formPanel.classList.add('hidden');
        if (openBtn) {
            openBtn.addEventListener('click', showForm);
        }
        if (cancelBtn) {
            cancelBtn.addEventListener('click', hideForm);
        }
        if (formPanel && formPanel.classList.contains('hidden') === false) {
            // keep visible when server requests it
        }
    })();
</script>
</body>
</html>
