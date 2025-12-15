<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%
    String username = (String) request.getAttribute("homeUsername");
    if (username == null) username = "";
    String initials = (String) request.getAttribute("homeInitials");
    if (initials == null || initials.isBlank()) initials = "??";
    String navActive = (String) request.getAttribute("navActive");
    if (navActive == null) navActive = "market";
    List<com.bankingsystem.controller.CustomerMarketServlet.MarketRow> currencyRates = (List<com.bankingsystem.controller.CustomerMarketServlet.MarketRow>) request.getAttribute("currencyRates");
    List<com.bankingsystem.controller.CustomerMarketServlet.MarketRow> commodityRates = (List<com.bankingsystem.controller.CustomerMarketServlet.MarketRow>) request.getAttribute("commodityRates");
    List<String> marketNews = (List<String>) request.getAttribute("marketNews");
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Market Data</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/customer-dashboard.css" />
    <style>
        .market-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 16px;
        }
        .market-card {
            background: #fff;
            border-radius: 24px;
            padding: 20px;
            box-shadow: 0 12px 32px rgba(15, 35, 66, 0.08);
        }
        .market-card h3 {
            margin: 0 0 12px;
        }
        .market-table {
            width: 100%;
            border-collapse: collapse;
        }
        .market-table th,
        .market-table td {
            padding: 10px 4px;
            border-bottom: 1px solid #edf0f6;
        }
        .positive { color: #00b894; font-weight: 600; }
        .negative { color: #ff5a5a; font-weight: 600; }
        .news-list li {
            margin-bottom: 12px;
        }
    </style>
</head>
<body>
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
            <a class="nav-item" href="#">QR Operations</a>
            <a class="nav-item <%= "profile".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/customer/profile">Profile</a>
        </nav>
        <a class="logout" href="<%= request.getContextPath() %>/">Logout</a>
    </aside>

    <main class="content">
        <header class="content__header">
            <div>
                <p class="subtitle">Market Data</p>
                <p class="muted">Döviz, emtia ve piyasa haberleri</p>
            </div>
            <div class="profile-chip">
                <div>
                    <strong><%= username %></strong>
                    <span>Private Banking</span>
                </div>
                <div class="chip-avatar"><%= initials %></div>
            </div>
        </header>

        <div class="market-grid">
            <section class="market-card">
                <h3>Currency Rates</h3>
                <table class="market-table">
                    <thead>
                    <tr><th>Pair</th><th>Price</th><th>Change</th></tr>
                    </thead>
                    <tbody>
                    <% if (currencyRates != null) {
                        for (var row : currencyRates) { %>
                    <tr>
                        <td><%= row.name() %></td>
                        <td><%= row.value() %></td>
                        <td class="<%= row.change().contains("-") ? "negative" : "positive" %>"><%= row.change() %></td>
                    </tr>
                    <%  }} %>
                    </tbody>
                </table>
            </section>

            <section class="market-card">
                <h3>Commodities & Indices</h3>
                <table class="market-table">
                    <thead>
                    <tr><th>Instrument</th><th>Price</th><th>Change</th></tr>
                    </thead>
                    <tbody>
                    <% if (commodityRates != null) {
                        for (var row : commodityRates) { %>
                    <tr>
                        <td><%= row.name() %></td>
                        <td><%= row.value() %></td>
                        <td class="<%= row.change().contains("-") ? "negative" : "positive" %>"><%= row.change() %></td>
                    </tr>
                    <%  }} %>
                    </tbody>
                </table>
            </section>

            <section class="market-card">
                <h3>Market Headlines</h3>
                <ul class="news-list">
                    <% if (marketNews != null) {
                        for (String news : marketNews) { %>
                    <li><%= news %></li>
                    <%  }} %>
                </ul>
            </section>
        </div>
    </main>
</div>
</body>
</html>
