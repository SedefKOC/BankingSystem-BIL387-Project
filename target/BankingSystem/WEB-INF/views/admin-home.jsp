<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) request.getAttribute("homeUsername");
    if (username == null) {
        username = "";
    }
    String initials = (String) request.getAttribute("homeInitials");
    if (initials == null || initials.isBlank()) {
        initials = "??";
    }
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
        <nav class="admin-sidebar__nav">
            <a class="nav-item active" href="#">Dashboard</a>
            <a class="nav-item" href="#">Customers</a>
            <a class="nav-item" href="#">Accounts</a>
            <a class="nav-item" href="#">QR Requests</a>
            <a class="nav-item" href="#">Reports</a>
        </nav>
        <a class="logout" href="<%= request.getContextPath() %>/">Logout System</a>
    </aside>

    <main class="admin-content">
        <header class="admin-header">
            <div>
                <h1>Admin Dashboard</h1>
                <p class="muted">Merhaba <%= username %>, sistem durumunu buradan takip edebilirsin.</p>
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

        <section class="admin-stats">
            <article class="stat-card">
                <p>Active Customers</p>
                <h2>24,592</h2>
                <span class="positive">↑ +12% this month</span>
            </article>
            <article class="stat-card">
                <p>Total Active Accounts</p>
                <h2>38,145</h2>
                <span class="positive">↑ +5.4% this week</span>
            </article>
            <article class="stat-card">
                <p>Today's Volume</p>
                <h2>₺ 45.2M</h2>
                <span class="negative">↓ -2.1% vs yesterday</span>
            </article>
        </section>

        <section class="admin-table">
            <header>
                <h3>Recent Transactions</h3>
                <a href="#">View All</a>
            </header>
            <table>
                <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Customer</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Date & Time</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>#TRX-98421</td>
                    <td>Ahmet Kaya</td>
                    <td>SWIFT Transfer</td>
                    <td>$ 12,500.00</td>
                    <td>Nov 21, 2025 14:30</td>
                    <td><span class="badge badge-success">Completed</span></td>
                    <td><a href="#">Details</a></td>
                </tr>
                <tr>
                    <td>#TRX-98422</td>
                    <td>Elif Demir</td>
                    <td>QR Payment</td>
                    <td>₺ 450.00</td>
                    <td>Nov 21, 2025 14:28</td>
                    <td><span class="badge badge-success">Completed</span></td>
                    <td><a href="#">Details</a></td>
                </tr>
                <tr>
                    <td>#TRX-98423</td>
                    <td>Tech Solutions Ltd</td>
                    <td>Corporate Loan</td>
                    <td>₺ 500,000.00</td>
                    <td>Nov 21, 2025 14:15</td>
                    <td><span class="badge badge-warning">Pending Approval</span></td>
                    <td><a href="#">Review</a></td>
                </tr>
                <tr>
                    <td>#TRX-98424</td>
                    <td>Mehmet Can</td>
                    <td>Credit Card Bill</td>
                    <td>₺ 8,240.50</td>
                    <td>Nov 21, 2025 14:05</td>
                    <td><span class="badge badge-error">Failed</span></td>
                    <td><a href="#">Details</a></td>
                </tr>
                <tr>
                    <td>#TRX-98425</td>
                    <td>Selin Yılmaz</td>
                    <td>EFT / Transfer</td>
                    <td>₺ 2,100.00</td>
                    <td>Nov 21, 2025 13:55</td>
                    <td><span class="badge badge-success">Completed</span></td>
                    <td><a href="#">Details</a></td>
                </tr>
                </tbody>
            </table>
        </section>
    </main>
</div>
</body>
</html>
