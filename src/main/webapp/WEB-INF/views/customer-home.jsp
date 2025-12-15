<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.DecimalFormatSymbols"%>
<%@ page import="com.bankingsystem.entity.TransactionRecord"%>
<%
    String username = (String) request.getAttribute("homeUsername");
    if (username == null) {
        username = "";
    }
    String initials = (String) request.getAttribute("homeInitials");
    if (initials == null || initials.isBlank()) {
        initials = "??";
    }
    List<TransactionRecord> recentTransactions = (List<TransactionRecord>) request.getAttribute("recentTransactions");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm", Locale.ENGLISH);
    DecimalFormatSymbols trSymbols = new DecimalFormatSymbols(new Locale("tr", "TR"));
    trSymbols.setGroupingSeparator('.');
    trSymbols.setDecimalSeparator(',');
    DecimalFormat amountFormat = new DecimalFormat("#,##0.00", trSymbols);
    java.math.BigDecimal totalBalance = (java.math.BigDecimal) request.getAttribute("totalBalance");
    if (totalBalance == null) {
        totalBalance = java.math.BigDecimal.ZERO;
    }
    boolean totalPositive = totalBalance.signum() >= 0;
    String totalText = (totalPositive ? "" : "-") + "₺ " + amountFormat.format(totalBalance.abs());
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
<%
    String navActive = (String) request.getAttribute("navActive");
    if (navActive == null) {
        navActive = "overview";
    }
%>
<div class="dashboard">
    <aside class="sidebar">
        <div class="sidebar__brand">
            <img src="<%=request.getContextPath()%>/assets/img/sedefbank-logo.svg" alt="SedefBank Logo" class="logo-image" />
            <span>SedefBank</span>
        </div>
        <nav class="sidebar__nav">
            <a class="nav-item <%= "overview".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/customer/home">Overview</a>
            <a class="nav-item <%= "accounts".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/customer/accounts">Accounts</a>
            <a class="nav-item <%= "transfers".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/customer/transfers">Transfers</a>
            <a class="nav-item <%= "market".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/customer/market">Market Data</a>
            <a class="nav-item <%= "profile".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/customer/profile">Profile</a>
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
                    <div class="chip-avatar"><%= initials %></div>
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
                <!-- <h4>Instant Loan</h4>
                <p>Get up to 100.000₺ instantly.</p>
                <button type="button">Apply Now</button> -->
            </article>
        </section>

        <section class="balance">
            <div>
                <p>Total Assets Balance</p>
                <h2 class='<%= totalPositive ? "positive" : "negative" %>'><%= totalText %></h2>
                <div class="balance__actions">
                    <!-- <button>Deposit Money</button>
                    <button>New Transfer</button>
                    <button>Exchange</button> -->
                </div>
            </div>
        </section>

        <section class="transactions">
            <header>
                <h3>Last 5 Transactions</h3>
                <!-- <a href="#">View All Transactions →</a> -->
            </header>
            <table>
                <thead>
                <tr>
                    <th>Transaction</th>
                    <th>Date</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (recentTransactions != null && !recentTransactions.isEmpty()) {
                        for (TransactionRecord record : recentTransactions) {
                            java.math.BigDecimal amount = record.getAmount() == null ? java.math.BigDecimal.ZERO : record.getAmount();
                            boolean positive = amount.signum() >= 0;
                            String amountClass = positive ? "positive" : "negative";
                            String amountText = (positive ? "+" : "-") + "₺" + amountFormat.format(amount.abs());
                            String dateText = record.getTransactionDate() == null ? "-" : record.getTransactionDate().format(dateFormatter);
                            String description = record.getDescription() == null ? "—" : record.getDescription()
                                    .replace("&", "&amp;")
                                    .replace("<", "&lt;")
                                    .replace(">", "&gt;")
                                    .replace("\"", "&quot;")
                                    .replace("'", "&#x27;");
                %>
                <tr>
                    <td><%= description %></td>
                    <td><%= dateText %></td>
                    <td class="<%= amountClass %>"><%= amountText %></td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="3">Henüz görüntülenecek işlem bulunmuyor.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </section>
    </main>
</div>
</body>
</html>
