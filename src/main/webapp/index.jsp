<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
    String selectedRole = (String) request.getAttribute("selectedRole");
    if (selectedRole == null) {
        selectedRole = "";
    }
    String typedUsername = (String) request.getAttribute("typedUsername");
    if (typedUsername == null) {
        typedUsername = "";
    }
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Giriş</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="assets/css/login.css" />
</head>
<body>
<main class="login-page">
    <section class="brand">
        <div class="logo-badge">SB</div>
        <h1>SedefBank</h1>
        <p>Dijital bankacılık hesabına güvenle giriş yap.111xxx666xx</p>
    </section>

    <section class="login-card">
        <h2>Tekrar Hoş Geldin</h2>
        <p>Devam etmek için giriş bilgilerini doldur.</p>

        <% if (errorMessage != null) { %>
        <div class="alert alert-error"><%= errorMessage %></div>
        <% } else if (successMessage != null) { %>
        <div class="alert alert-success"><%= successMessage %></div>
        <% } %>

        <form action="login" method="post" novalidate>
            <div class="role-toggle">
                <div class="role-option">
                    <input type="radio" name="role" value="ADMIN" id="role-admin" <%= "ADMIN".equals(selectedRole) ? "checked" : "" %> />
                    <label for="role-admin">Admin</label>
                </div>
                <div class="role-option">
                    <input type="radio" name="role" value="CUSTOMER" id="role-customer" <%= "CUSTOMER".equals(selectedRole) ? "checked" : "" %> />
                    <label for="role-customer">Customer</label>
                </div>
            </div>

            <div class="input-group">
                <span class="input-label">USERNAME</span>
                <input class="form-input" type="text" name="username" placeholder="e.g. john.doe" value="<%= typedUsername %>" maxlength="50" autocomplete="username" required />
            </div>

            <div class="input-group">
                <span class="input-label">PASSWORD</span>
                <input class="form-input" type="password" name="password" placeholder="••••••••" maxlength="64" autocomplete="current-password" required />
            </div>

            <div class="form-actions">
                <label>
                    <input type="checkbox" name="remember" disabled />
                    Keep me logged in
                </label>
                <a href="#">Forgot Password?</a>
            </div>

            <button type="submit" class="btn-login">Login</button>
        </form>

        <div class="page-footer">
            <p>© 2025 SedefBank Digital. All rights reserved.</p>
            <p class="secure-icon">🔒 SSL Encrypted Connection</p>
        </div>
    </section>
</main>
</body>
</html>
