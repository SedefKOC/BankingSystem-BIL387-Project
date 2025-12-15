<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.DecimalFormatSymbols"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.List"%>
<%@ page import="com.bankingsystem.entity.AccountSummary"%>
<%
    String username = (String) request.getAttribute("homeUsername");
    if (username == null) {
        username = "";
    }
    String initials = (String) request.getAttribute("homeInitials");
    if (initials == null || initials.isBlank()) {
        initials = "??";
    }
    String navActive = (String) request.getAttribute("navActive");
    if (navActive == null) {
        navActive = "accounts";
    }
    java.math.BigDecimal totalBalance = (java.math.BigDecimal) request.getAttribute("totalBalance");
    if (totalBalance == null) {
        totalBalance = java.math.BigDecimal.ZERO;
    }
    DecimalFormatSymbols trSymbols = new DecimalFormatSymbols(new Locale("tr", "TR"));
    trSymbols.setGroupingSeparator('.');
    trSymbols.setDecimalSeparator(',');
    DecimalFormat amountFormat = new DecimalFormat("#,##0.00", trSymbols);
    List<AccountSummary> accounts = (List<AccountSummary>) request.getAttribute("accounts");
    if (accounts == null) {
        accounts = java.util.Collections.emptyList();
    }
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Accounts</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/customer-dashboard.css" />
    <style>
        .account-cards {
            display: flex;
            gap: 16px;
            flex-wrap: wrap;
        }
        .account-card {
            background: #fff;
            border-radius: 16px;
            padding: 18px;
            box-shadow: 0 12px 30px rgba(10, 28, 60, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
        }
        .account-card + .account-card {
            margin-top: 14px;
        }
        .account-details h4 {
            margin: 0;
        }
        .account-details span {
            color: #7b879c;
            font-size: 14px;
        }
        .account-actions button {
            background: transparent;
            border: 1px solid #dee3f0;
            border-radius: 12px;
            padding: 10px;
            cursor: pointer;
            margin-left: 8px;
        }
        .info-cards {
            display: flex;
            gap: 14px;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }
        .info-card {
            flex: 1;
            min-width: 220px;
            background: #fff;
            border-radius: 16px;
            padding: 16px;
            box-shadow: 0 10px 26px rgba(10, 30, 60, 0.08);
        }
        .info-card h3 {
            margin: 8px 0 0;
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
            <a class="nav-item" href="#">Market Data</a>
            <a class="nav-item <%= "profile".equals(navActive) ? "active" : "" %>" href="<%=request.getContextPath()%>/customer/profile">Profile</a>
        </nav>
        <a class="logout" href="<%= request.getContextPath() %>/">Logout</a>
    </aside>

    <main class="content">
        <header class="content__header">
            <div>
                <p class="subtitle">My Accounts</p>
            </div>
            <div class="profile-chip">
                <div>
                    <strong><%= username %></strong>
                    <span>Private Banking</span>
                </div>
                <div class="chip-avatar"><%= initials %></div>
            </div>
        </header>

        <section class="info-cards">
            <div class="info-card" style="background: linear-gradient(135deg, #103dff, #06208d); color:#fff;">
                <p>Total Assets (TRY)</p>
                <h3>₺ <%= amountFormat.format(totalBalance.abs()) %></h3>
            </div>
            <div class="info-card">
  
            </div>
            <div class="info-card">
  
            </div>
        </section>

        <section class="transactions">
            <header>
                <h3>Active Accounts (<%= accounts.size() %>)</h3>
            </header>
            <div class="account-cards">
                <%
                    if (accounts.isEmpty()) {
                %>
                <p>Hesap bulunamadı.</p>
                <%
                    } else {
                        for (AccountSummary account : accounts) {
                %>
                <div class="account-card">
                    <div class="account-details">
                        <h4><%= account.getName() %></h4>
                        <span><%= account.getIban() %></span>
                    </div>
                    <div style="text-align:right;">
                        <strong><%= account.getCurrencyLabel() %> <%= amountFormat.format(account.getBalance()) %></strong>
                        <div class="muted"><%= account.getStatusLabel() %></div>
                    </div>
                    <div class="account-actions">
                        <!-- <button title="Copy IBAN">📋</button> -->
                        <button title="Details" onclick="window.location.href='<%=request.getContextPath()%>/customer/accounts/detail?accountId=<%= account.getId() %>'">↗</button>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </div>
        </section>
    </main>
</div>
</body>
</html>
