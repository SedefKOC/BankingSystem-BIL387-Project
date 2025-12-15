<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Locale"%>
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
    List<Map<String, Object>> customers = (List<Map<String, Object>>) request.getAttribute("adminCustomers");
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
                <p class="muted">Müşterileri görüntüleyip durumlarını hızlıca güncelleyebilirsin.</p>
            </div>
            <div class="header-actions">
                <div class="search">
                    <input type="search" placeholder="Search customers or transaction" />
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
                <input type="search" placeholder="Search by Name, Email, or TCKN" />
                <select>
                    <option>All Statuses</option>
                    <option>Active</option>
                    <option>Suspended</option>
                </select>
                <button class="btn btn-primary">Search</button>
            </div>
            <button class="btn btn-secondary">+ Add New Customer</button>
        </section>

        <section class="customer-table">
            <div class="customer-table__header">
                <span>CUSTOMER NAME / ID</span>
                <span>CONTACT INFO</span>
                <span>JOIN DATE</span>
                <span>STATUS</span>
                <span>ACTIONS</span>
            </div>
            <div class="customer-table__body">
                <%
                    for (Map<String, Object> row : customers) {
                        String customerName = (String) row.get("name");
                        String customerInitials = (String) row.get("initials");
                        String idLabel = (String) row.get("idLabel");
                        String email = (String) row.get("email");
                        String phone = (String) row.get("phone");
                        java.time.LocalDate joinDate = (java.time.LocalDate) row.get("joinDate");
                        String status = (String) row.get("status");
                        String statusClass = (String) row.get("statusClass");
                %>
                <div class="customer-row">
                    <div class="customer-meta">
                        <div class="customer-avatar"><%= customerInitials %></div>
                        <div>
                            <strong><%= customerName %></strong>
                            <span class="muted"><%= idLabel %></span>
                        </div>
                    </div>
                    <div class="customer-contact">
                        <div><%= email %></div>
                        <small class="muted"><%= phone %></small>
                    </div>
                    <div class="customer-joined"><%= joinDate == null ? "-" : joinDate.format(joinFormatter) %></div>
                    <div class="customer-status">
                        <span class="status-pill <%= statusClass %>"><%= status %></span>
                    </div>
                    <div class="customer-actions">
                        <button class="btn btn-outline">View Accounts</button>
                        <button class="btn btn-outline">Edit</button>
                        <%
                            if ("status-suspended".equals(statusClass)) {
                        %>
                        <button class="btn btn-success">Reactivate</button>
                        <%
                            } else {
                        %>
                        <button class="btn btn-danger">Suspend</button>
                        <%
                            }
                        %>
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
</body>
</html>
