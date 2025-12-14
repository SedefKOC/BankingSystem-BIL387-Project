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
    <title>SedefBank | Admin Paneli</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login.css" />
    <style>
        .dashboard-card {
            display: flex;
            flex-direction: column;
            gap: 14px;
        }
        .info-pill {
            padding: 12px 16px;
            border-radius: 16px;
            background: rgba(255, 255, 255, 0.8);
            font-weight: 600;
            box-shadow: 0 15px 35px rgba(11, 26, 51, 0.18);
        }
    </style>
</head>
<body>
<main class="login-page">
    <section class="brand">
        <div class="logo-badge">SB</div>
        <h1>SedefBank</h1>
        <p>Admin panelindesin. Buradan sistemi yönetebilirsin.</p>
    </section>
    <section class="login-card dashboard-card">
        <h2>Merhaba <%= username %>, Admin girişi yapıldı.</h2>
        <p class="info-pill">Yeni kullanıcı onayları, raporlar ve sistem sağlığı buradan gelecek.</p>
        <button class="btn-login" style="margin-top:12px;" disabled>Paneli yapılandır</button>
        <a href="<%= request.getContextPath() %>/" style="color:#0f62fe;font-weight:600;text-decoration:none;">← Login ekranına dön</a>
    </section>
</main>
</body>
</html>
