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
    <title>SedefBank | Customer</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login.css" />
    <style>
        .dashboard-card {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }
        .balance-box {
            background: linear-gradient(135deg, #0f62fe, #7c42ec);
            color: #fff;
            padding: 18px;
            border-radius: 24px;
            box-shadow: 0 18px 40px rgba(13, 45, 89, 0.25);
        }
        .balance-box h3 {
            margin: 0 0 8px;
        }
    </style>
</head>
<body>
<main class="login-page">
    <section class="brand">
        <div class="logo-badge">SB</div>
        <h1>SedefBank</h1>
        <p>Hesabını yönetmeye hazırsın.</p>
    </section>
    <section class="login-card dashboard-card">
        <h2>Hoş geldin <%= username %>!</h2>
        <div class="balance-box">
            <h3>Toplam Bakiyen</h3>
            <p style="font-size:32px;font-weight:600;">₺ 25.000,00</p>
        </div>
        <button class="btn-login" style="margin-top:12px;" disabled>Para Transferi</button>
        <a href="<%= request.getContextPath() %>/" style="color:#0f62fe;font-weight:600;text-decoration:none;">← Login ekranına dön</a>
    </section>
</main>
</body>
</html>
