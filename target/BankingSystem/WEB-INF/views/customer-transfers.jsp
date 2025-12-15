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
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
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
        .tabs {
            display: flex;
            gap: 12px;
            margin-bottom: 16px;
        }
        .tab-button {
            flex: 1;
            padding: 10px;
            border-radius: 16px;
            border: 1px solid #dee3f0;
            background: #f4f6fb;
            font-weight: 600;
            cursor: pointer;
            text-align: center;
        }
        .tab-button.active {
            background: linear-gradient(135deg, #103dff, #06208d);
            color: #fff;
            border-color: transparent;
        }
        .tab-content { display: none; }
        .tab-content.active { display: block; }
        .alert {
            border-radius: 14px;
            padding: 12px 16px;
            margin-bottom: 16px;
            font-weight: 600;
        }
        .alert-success {
            background: #e1f7ef;
            color: #077346;
        }
        .alert-error {
            background: #fdeaea;
            color: #c62828;
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
            <section class="transfer-card">
                <h3>Transfer Between Accounts</h3>
                <%
                    if (successMessage != null) {
                %>
                <div class="alert alert-success"><%= successMessage %></div>
                <%
                    } else if (errorMessage != null) {
                %>
                <div class="alert alert-error"><%= errorMessage %></div>
                <%
                    }
                %>
                <div class="tabs">
                    <div class="tab-button active" data-tab="myAccounts">My Accounts</div>
                    <div class="tab-button" data-tab="otherPerson">Another Person / IBAN</div>
                </div>
                <%
                    List<com.bankingsystem.entity.AccountSummary> transferAccounts =
                            (List<com.bankingsystem.entity.AccountSummary>) request.getAttribute("transferAccounts");
                    if (transferAccounts == null) {
                        transferAccounts = java.util.Collections.emptyList();
                    }
                %>
                <div id="myAccounts" class="tab-content active">
                    <form method="post" class="transfer-form" data-action="myAccounts">
                        <input type="hidden" name="action" value="myAccounts" />
                        <label for="fromAccountIdMy">From Account</label>
                        <select id="fromAccountIdMy" name="fromAccountId" required>
                            <% for (com.bankingsystem.entity.AccountSummary account : transferAccounts) { %>
                            <option value="<%= account.getId() %>"><%= account.getName() %> - <%= account.getIban() %></option>
                            <% } %>
                        </select>
                        <label for="toAccountIdMy">To Account</label>
                        <select id="toAccountIdMy" name="toAccountId" required>
                            <% for (com.bankingsystem.entity.AccountSummary account : transferAccounts) { %>
                            <option value="<%= account.getId() %>"><%= account.getName() %> - <%= account.getIban() %></option>
                            <% } %>
                        </select>
                        <label for="amountMy">Amount</label>
                        <input id="amountMy" type="number" step="0.01" min="0.01" name="amount" placeholder="₺ 0.00" required />
                        <label for="dateMy">Transfer Date</label>
                        <input id="dateMy" type="date" name="transactionDate" value="<%= java.time.LocalDate.now() %>" required />
                        <label for="descMy">Description (Optional)</label>
                        <textarea id="descMy" rows="3" name="description" placeholder="e.g. Rent payment, Debt return"></textarea>
                        <button type="submit">Confirm Transfer</button>
                    </form>
                </div>
                <div id="otherPerson" class="tab-content">
                    <form method="post" class="transfer-form" data-action="otherPerson">
                        <input type="hidden" name="action" value="otherPerson" />
                        <label for="fromAccountIdOther">From Account</label>
                        <select id="fromAccountIdOther" name="fromAccountId" required>
                            <% for (com.bankingsystem.entity.AccountSummary account : transferAccounts) { %>
                            <option value="<%= account.getId() %>"><%= account.getName() %> - <%= account.getIban() %></option>
                            <% } %>
                        </select>
                        <label for="ibanOther">To Account (IBAN)</label>
                        <input id="ibanOther" type="text" name="toAccountIban" placeholder="TRXX XXXX XXXX XXXX" required />
                        <label for="amountOther">Amount</label>
                        <input id="amountOther" type="number" step="0.01" min="0.01" name="amount" placeholder="₺ 0.00" required />
                        <label for="dateOther">Transfer Date</label>
                        <input id="dateOther" type="date" name="transactionDate" value="<%= java.time.LocalDate.now() %>" required />
                        <label for="descOther">Description (Optional)</label>
                        <textarea id="descOther" rows="3" name="description" placeholder="e.g. Rent payment, Debt return"></textarea>
                        <button type="submit">Confirm Transfer</button>
                    </form>
                </div>
            </section>

            <!-- <section class="history-card">
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
            </section> -->
        </div>
    </main>
</div>
<script>
    (function () {
        const tabs = document.querySelectorAll('.tab-button');
        const contents = document.querySelectorAll('.tab-content');
        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                tabs.forEach(t => t.classList.remove('active'));
                contents.forEach(c => c.classList.remove('active'));
                tab.classList.add('active');
                const target = document.getElementById(tab.getAttribute('data-tab'));
                if (target) {
                    target.classList.add('active');
                }
            });
        });
        const showAlert = (message) => {
            alert(message);
        };
        document.querySelectorAll('.transfer-form').forEach(form => {
            form.addEventListener('submit', (event) => {
                const action = form.dataset.action;
                const amountInput = form.querySelector('input[name="amount"]');
                const dateInput = form.querySelector('input[name="transactionDate"]');
                const amount = parseFloat(amountInput.value);
                if (isNaN(amount) || amount <= 0) {
                    event.preventDefault();
                    showAlert('Tutar 0\'dan büyük olmalı.');
                    return;
                }
                if (!dateInput.value) {
                    event.preventDefault();
                    showAlert('Lütfen geçerli bir tarih seçin.');
                    return;
                }
                if (action === 'myAccounts') {
                    const from = form.querySelector('select[name="fromAccountId"]').value;
                    const to = form.querySelector('select[name="toAccountId"]').value;
                    if (from === to) {
                        event.preventDefault();
                        showAlert('Kaynak ve hedef hesap farklı olmalı.');
                    }
                } else if (action === 'otherPerson') {
                    const iban = form.querySelector('input[name="toAccountIban"]').value.trim();
                    if (!iban) {
                        event.preventDefault();
                        showAlert('IBAN boş olamaz.');
                    }
                }
            });
        });
    })();
</script>
</body>
</html>
