<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.DecimalFormatSymbols"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.List"%>
<%@ page import="com.bankingsystem.entity.AccountSummary"%>
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
    AccountSummary account = (AccountSummary) request.getAttribute("account");
    List<TransactionRecord> accountTransactions = (List<TransactionRecord>) request.getAttribute("accountTransactions");
    if (accountTransactions == null) {
        accountTransactions = java.util.Collections.emptyList();
    }
    DecimalFormatSymbols trSymbols = new DecimalFormatSymbols(new Locale("tr", "TR"));
    trSymbols.setGroupingSeparator('.');
    trSymbols.setDecimalSeparator(',');
    DecimalFormat amountFormat = new DecimalFormat("#,##0.00", trSymbols);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Account Details</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/customer-dashboard.css" />
    <style>
        .detail-hero {
            background: linear-gradient(135deg, #103dff, #06208d);
            color: #fff;
            border-radius: 32px;
            padding: 24px;
            box-shadow: 0 18px 40px rgba(14, 31, 75, 0.35);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .detail-hero h2 {
            margin: 0 0 12px;
        }
        .detail-actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
            gap: 12px;
            margin-top: 20px;
        }
        .detail-actions button {
            padding: 12px;
            border-radius: 16px;
            border: none;
            background: #fff;
            font-weight: 600;
            cursor: pointer;
        }
        .transactions-table {
            background: #fff;
            border-radius: 24px;
            margin-top: 24px;
            padding: 24px;
            box-shadow: 0 12px 32px rgba(12, 29, 61, 0.08);
        }
        .transactions-table table {
            width: 100%;
            border-collapse: collapse;
        }
        .transactions-table th,
        .transactions-table td {
            padding: 12px 8px;
            border-bottom: 1px solid #e3e7ef;
            text-align: left;
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
             <a class="nav-item" href="<%=request.getContextPath()%>/customer/home">Overview</a>
            <a class="nav-item active" href="<%=request.getContextPath()%>/customer/accounts">Accounts</a>
            <a class="nav-item" href="<%=request.getContextPath()%>/customer/transfers">Transfers</a>
            <a class="nav-item" href="#">Market Data</a>
            <a class="nav-item" href="<%=request.getContextPath()%>/customer/profile">Profile</a>
        </nav>
        <a class="logout" href="<%= request.getContextPath() %>/">Logout</a>
    </aside>

    <main class="content">
        <header class="content__header">
            <div class="muted">Accounts > <%= account.getIban() %></div>
            <div class="profile-chip">
                <div>
                    <strong><%= username %></strong>
                    <span>Private Banking</span>
                </div>
                <div class="chip-avatar"><%= initials %></div>
            </div>
        </header>

        <section class="detail-hero">
            <div>
                <span class="muted"><%= account.getStatusLabel() %></span>
                <h2><%= account.getName() %></h2>
                <h1 style="margin:0;"> <%= account.getCurrencyLabel() %> <%= amountFormat.format(account.getBalance()) %></h1>
                <p><%= account.getIban() %></p>
            </div>
            <div class="detail-actions">
                <button>Withdraw</button>
                <button>Deposit</button>
                <button>Transfer</button>
                <button>Convert</button>
            </div>
        </section>

        <section class="transactions-table">
            <header style="display:flex;justify-content:space-between;align-items:center;">
                <div>
                    <strong>Transactions</strong>
                    <span class="muted">Son hareketler</span>
                </div>
                <button class="btn-login" style="width:auto;padding:10px 16px;">Export PDF</button>
            </header>
            <table>
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Type</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (accountTransactions.isEmpty()) {
                %>
                <tr><td colspan="4">Bu hesapta henüz işlem yok.</td></tr>
                <%
                    } else {
                        for (TransactionRecord record : accountTransactions) {
                            java.math.BigDecimal amount = record.getAmount() == null ? java.math.BigDecimal.ZERO : record.getAmount();
                            boolean positive = amount.signum() >= 0;
                %>
                <tr>
                    <td><%= record.getTransactionDate() == null ? "-" : record.getTransactionDate().format(dateFormatter) + " " + record.getTransactionDate().format(timeFormatter) %></td>
                    <td><%= record.getDescription() %></td>
                    <td><%= positive ? "INCOME" : "EXPENSE" %></td>
                    <td class="<%= positive ? "positive" : "negative" %>"><%= (positive ? "+" : "-") + "₺" + amountFormat.format(amount.abs()) %></td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </section>
    </main>
</div>
</body>
</html>
