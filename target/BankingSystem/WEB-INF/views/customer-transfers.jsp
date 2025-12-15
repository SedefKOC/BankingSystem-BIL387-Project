<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Locale"%>
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
    String navActive = (String) request.getAttribute("navActive");
    if (navActive == null) {
        navActive = "transfers";
    }
    List<TransactionRecord> transferHistory = (List<TransactionRecord>) request.getAttribute("transferHistory");
    if (transferHistory == null) {
        transferHistory = java.util.Collections.emptyList();
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm", Locale.ENGLISH);
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Transfers</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/customer-dashboard.css" />
    <style>
        .transfer-card {
            background: #fff;
            border-radius: 24px;
            padding: 24px;
            box-shadow: 0 12px 34px rgba(11, 26, 51, 0.08);
            width: 100%;
        }
        .transfer-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 24px;
        }
        .transfer-form label {
            font-weight: 600;
            display: block;
            margin-bottom: 6px;
        }
        .transfer-form input,
        .transfer-form select,
        .transfer-form textarea {
            width: 100%;
            padding: 12px;
            border-radius: 14px;
            border: 1px solid #dee3f0;
            margin-bottom: 16px;
        }
        .transfer-form button {
            width: 100%;
            padding: 14px;
            border: none;
            border-radius: 16px;
            background: linear-gradient(135deg, #103dff, #06208d);
            color: #fff;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
        }
        .history-card {
            background: #fff;
            border-radius: 24px;
            padding: 24px;
            box-shadow: 0 12px 34px rgba(13, 30, 55, 0.08);
        }
        .history-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #edf0f6;
        }
        .history-item:last-child {
            border-bottom: none;
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
                <p class="subtitle">Transfers</p>
                <p class="muted">Send money securely to other SedefBank accounts or external IBANs.</p>
            </div>
            <div class="profile-chip">
                <div>
                    <strong><%= username %></strong>
                    <span>Private Banking</span>
                </div>
                <div class="chip-avatar"><%= initials %></div>
            </div>
        </header>

        <div class="transfer-grid">
            <section class="transfer-card transfer-form">
                <h3>Transfer Between Accounts</h3>
                <div style="margin:16px 0;">
                    <label>Transfer Type</label>
                    <div style="display:flex;gap:12px;">
                        <label><input type="radio" name="type" checked /> My Accounts</label>
                        <label><input type="radio" name="type" /> Another Person / IBAN</label>
                        <label><input type="radio" name="type" /> International (SWIFT)</label>
                    </div>
                </div>
                <label>From Account</label>
                <select>
                    <option>Vadesiz TL - TR45..1923 (₺ 24,582.90)</option>
                </select>
                <label>To Account</label>
                <select>
                    <option>Select Recipient</option>
                </select>
                <label>Amount</label>
                <input type="number" placeholder="₺ 0.00" />
                <label>Transfer Date</label>
                <input type="date" value="<%= java.time.LocalDate.now() %>" />
                <label>Description (Optional)</label>
                <textarea rows="3" placeholder="e.g. Rent payment, Debt return"></textarea>
                <button type="button">Confirm Transfer</button>
            </section>

            <section class="history-card">
                <h3>Last 5 Transfers</h3>
                <%
                    if (transferHistory.isEmpty()) {
                %>
                <p class="muted">Henüz transfer yapılmadı.</p>
                <%
                    } else {
                        for (TransactionRecord record : transferHistory) {
                            boolean positive = record.getAmount() != null && record.getAmount().signum() >= 0;
                %>
                <div class="history-item">
                    <div>
                        <strong><%= record.getDescription() %></strong>
                        <div class="muted"><%= record.getTransactionDate() == null ? "-" : record.getTransactionDate().format(formatter) %></div>
                    </div>
                    <div class="<%= positive ? "positive" : "negative" %>">
                        <%= (positive ? "+" : "-") + "₺" + (record.getAmount() == null ? "0,00" : new java.text.DecimalFormat("#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("tr","TR"))).format(record.getAmount().abs())) %>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </section>
        </div>
    </main>
</div>
</body>
</html>
