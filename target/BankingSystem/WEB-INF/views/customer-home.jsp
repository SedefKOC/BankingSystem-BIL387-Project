<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) request.getAttribute("homeUsername");
    if (username == null) {
        username = "";
    }
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Customer Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/customer-dashboard.css" />
</head>
<body>
<div class="dashboard">
    <aside class="sidebar">
        <div class="sidebar__brand">
            <div class="logo">SB</div>
            <span>SedefBank</span>
        </div>
        <nav class="sidebar__nav">
            <a class="nav-item active" href="#">
                <span>Overview</span>
            </a>
            <a class="nav-item" href="#">Accounts</a>
            <a class="nav-item" href="#">Transfers</a>
            <a class="nav-item" href="#">QR Operations</a>
            <a class="nav-item" href="#">Market Data</a>
            <a class="nav-item" href="#">Profile</a>
        </nav>
        <a class="logout" href="<%= request.getContextPath() %>/">
            Logout
        </a>
    </aside>

    <main class="content">
        <header class="content__header">
            <div>
                <p class="subtitle">Welcome, <%= username %></p>
                <p class="muted">Last login: Today, 09:42 AM</p>
            </div>
            <div class="profile-chip">
                <div>
                    <strong><%= username %></strong>
                    <span>Private Banking</span>
                </div>
                <div class="chip-avatar"><%= username.isEmpty() ? "??" : username.substring(0, 2).toUpperCase() %></div>
            </div>
        </header>

        <section class="stats">
            <article class="stat-card">
                <p>USD / TRY</p>
                <h3>34.5842</h3>
                <span class="positive">▲ 0.45%</span>
            </article>
            <article class="stat-card">
                <p>EUR / TRY</p>
                <h3>36.4012</h3>
                <span class="positive">▲ 0.21%</span>
            </article>
            <article class="stat-card">
                <p>GRAM GOLD (TRY)</p>
                <h3>2,954.10</h3>
                <span class="negative">▼ 0.15%</span>
            </article>
            <article class="loan-card">
                <h4>Instant Loan</h4>
                <p>Get up to 100.000₺ instantly.</p>
                <button type="button">Apply Now</button>
            </article>
        </section>

        <section class="balance">
            <div>
                <p>Total Assets Balance</p>
                <h2>₺ 842.350,75</h2>
                <div class="balance__actions">
                    <button>Deposit Money</button>
                    <button>New Transfer</button>
                    <button>Exchange</button>
                </div>
            </div>
        </section>

        <section class="transactions">
            <header>
                <h3>Last 5 Transactions</h3>
                <a href="#">View All Transactions →</a>
            </header>
            <table>
                <thead>
                <tr>
                    <th>Transaction</th>
                    <th>Date</th>
                    <th>Category</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td> Migros Jet <span class="muted">Card •4821</span></td>
                    <td>Nov 21, 14:30</td>
                    <td>Shopping</td>
                    <td class="negative">-₺450,90</td>
                </tr>
                <tr>
                    <td>Ahmet Demir <span class="muted">IBAN Transfer</span></td>
                    <td>Nov 20, 09:15</td>
                    <td>Transfer</td>
                    <td class="negative">-₺2.500,00</td>
                </tr>
                <tr>
                    <td>Company Inc. <span class="muted">Salary Payment</span></td>
                    <td>Nov 15, 08:00</td>
                    <td>Income</td>
                    <td class="positive">+₺65.000,00</td>
                </tr>
                <tr>
                    <td>Airbnb</td>
                    <td>Nov 10, 19:45</td>
                    <td>Travel</td>
                    <td class="negative">-₺3.200,00</td>
                </tr>
                <tr>
                    <td>Netflix</td>
                    <td>Nov 05, 12:00</td>
                    <td>Entertainment</td>
                    <td class="negative">-₺109,99</td>
                </tr>
                </tbody>
            </table>
        </section>
    </main>
</div>
</body>
</html>
