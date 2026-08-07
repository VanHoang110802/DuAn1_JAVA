<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Voucher> listVoucher = (List<Voucher>) request.getAttribute("listVoucher");
%>

<html>
<head>
    <title>Danh sách Voucher</title>
    <link rel="stylesheet" href="assets/app.css">
    <style>
        .voucher-card {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px;
            margin: 10px 0;
            background-color: #f9f9f9;
        }
        .voucher-header {
            font-weight: bold;
            font-size: 1.1em;
            color: #27ae60;
            margin-bottom: 10px;
        }
        .voucher-info {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
            font-size: 0.95em;
        }
        .voucher-info-item {
            display: flex;
            justify-content: space-between;
        }
        .voucher-info-label {
            font-weight: bold;
            color: #333;
        }
        .voucher-info-value {
            color: #666;
        }
    </style>
</head>
<body>
<main class="page-shell">
    <header class="topbar">
        <div class="brand-block">
            <div class="brand-mark">L</div>
            <div>
                <p class="eyebrow">Nhân viên phục vụ</p>
                <h1>Xin chào, <%= currentUser.getTenND() %></h1>
            </div>
        </div>
        <nav class="nav-actions">
            <a class="nav-link" href="index">Trang chính</a>
            <a class="nav-link" href="logout" onclick="return confirm('Bạn có chắc muốn đăng xuất?')">Đăng xuất</a>
        </nav>
    </header>

    <section>
        <div style="max-width: 1000px; margin: 20px auto; padding: 20px;">
            <h2>Danh sách Voucher có thể sử dụng</h2>
            <p style="color: #666; margin-bottom: 20px;">Chọn voucher để áp dụng cho khách hàng</p>

            <% if (listVoucher != null && !listVoucher.isEmpty()) { %>
                <% for (Voucher voucher : listVoucher) { %>
                    <div class="voucher-card">
                        <div class="voucher-header">
                            <%= voucher.getTenVoucher() %>
                            <% if ("TienMat".equals(voucher.getLoaiGiamGia())) { %>
                                <span style="color: #e74c3c;">- <%= (long) voucher.getGiaTriGiamGia() %> đ</span>
                            <% } else { %>
                                <span style="color: #e74c3c;">- <%= (long) voucher.getGiaTriGiamGia() %>%</span>
                            <% } %>
                        </div>
                        <div class="voucher-info">
                            <div class="voucher-info-item">
                                <span class="voucher-info-label">Mã voucher:</span>
                                <span class="voucher-info-value"><strong><%= voucher.getMaVoucher() %></strong></span>
                            </div>
                            <div class="voucher-info-item">
                                <span class="voucher-info-label">Còn lượt sử dụng:</span>
                                <span class="voucher-info-value"><strong><%= voucher.getSoLanConLai() %></strong></span>
                            </div>
                            <div class="voucher-info-item">
                                <span class="voucher-info-label">Đơn hàng tối thiểu:</span>
                                <span class="voucher-info-value"><%= String.format("%,d", (long) voucher.getDonGiaTuoiNhap()) %> đ</span>
                            </div>
                            <div class="voucher-info-item">
                                <span class="voucher-info-label">Hết hạn:</span>
                                <span class="voucher-info-value"><%= voucher.getNgayKetThuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></span>
                            </div>
                        </div>
                    </div>
                <% } %>
            <% } else { %>
                <div style="text-align: center; padding: 40px; color: #999;">
                    <p>Hiện không có voucher nào có thể sử dụng.</p>
                </div>
            <% } %>
        </div>
    </section>
</main>
</body>
</html>

