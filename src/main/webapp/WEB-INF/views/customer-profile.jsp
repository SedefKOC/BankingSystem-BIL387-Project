<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        navActive = "profile";
    }
    Long customerId = (Long) request.getAttribute("customerId");
%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <title>SedefBank | Profile</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/customer-dashboard.css" />
    <style>
        .profile-grid {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 24px;
        }
        .profile-card {
            background: #fff;
            border-radius: 24px;
            padding: 24px;
            box-shadow: 0 12px 32px rgba(12, 29, 61, 0.08);
        }
        .profile-avatar {
            width: 120px;
            height: 120px;
            border-radius: 32px;
            background: rgba(15, 98, 254, 0.1);
            color: #0f62fe;
            font-size: 36px;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 16px;
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 16px;
        }
        .form-grid label {
            font-weight: 600;
            display: block;
            margin-bottom: 4px;
        }
        .form-grid input,
        .form-grid textarea {
            width: 100%;
            padding: 12px;
            border-radius: 14px;
            border: 1px solid #dde3f0;
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
            <a class="nav-item active" href="<%=request.getContextPath()%>/customer/profile">Profile</a>
        </nav>
        <a class="logout" href="<%= request.getContextPath() %>/">Logout</a>
    </aside>

    <main class="content">
        <header class="content__header">
            <div>
                <p class="subtitle">My Profile</p>
            </div>
            <div class="profile-chip">
                <div>
                    <strong><%= username %></strong>
                    <span>Private Customer</span>
                </div>
                <div class="chip-avatar"><%= initials %></div>
            </div>
        </header>

        <div class="profile-grid">
            <section class="profile-card">
                <div class="profile-avatar"><%= initials %></div>
                <h2><%= username %></h2>
                <p class="muted">Verified Account</p>
                <p><strong>Customer ID</strong><br /><%= customerId == null ? "123456789" : customerId %></p>
                <p><strong>Joined</strong><br />November 2020</p>
            </section>

            <section class="profile-card">
                <h3>Personal Information</h3>
                <div class="form-grid" style="margin-top:16px;">
                    <div>
                        <label>First Name</label>
                        <input type="text" value="Ali" />
                    </div>
                    <div>
                        <label>Last Name</label>
                        <input type="text" value="Yılmaz" />
                    </div>
                    <div>
                        <label>Email Address</label>
                        <input type="email" value="ali.yilmaz@example.com" />
                    </div>
                    <div>
                        <label>Phone Number</label>
                        <input type="text" value="+90 555 123 45 67" />
                    </div>
                    <div>
                        <label>National ID (TCKN)</label>
                        <input type="text" value="12345678901" />
                    </div>
                    <div>
                        <label>Date of Birth</label>
                        <input type="text" value="15.05.1985" />
                    </div>
                    <div style="grid-column: span 2;">
                        <label>Home Address</label>
                        <textarea rows="2">Caddebostan Mah. Bağdat Cad. No:123/4 Kadıköy/İstanbul</textarea>
                    </div>
                </div>
                <button class="btn-login" style="margin-top:16px;width:auto;padding:12px 24px;">Save Changes</button>
            </section>
        </div>

        <section class="profile-card" style="margin-top:24px;">
            <h3>Security Settings</h3>
            <div class="form-grid" style="margin-top:16px;">
                <div>
                    <label>Current Password</label>
                    <input type="password" value="********" />
                </div>
                <div>
                    <label>New Password</label>
                    <input type="password" placeholder="Enter new password" />
                </div>
                <div>
                    <label>Confirm Password</label>
                    <input type="password" placeholder="Repeat new password" />
                </div>
            </div>
            <button class="btn-login" style="margin-top:16px;width:auto;padding:12px 24px;">Update Password</button>
        </section>
    </main>
</div>
</body>
</html>
