<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.Entity.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Voucher voucher = (Voucher) request.getAttribute("voucher");
    if (voucher == null) {
        response.sendRedirect("voucher?action=list");
        return;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
%>

<html>
<head>
    <title>Sửa Voucher</title>
    <link rel="stylesheet" href="assets/app.css">
    <style>
        .form-container {
            max-width: 700px;
            margin: 20px auto;
            padding: 20px;
            background: white;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 3px;
            font-size: 0.95em;
            box-sizing: border-box;
        }
        .form-group input[readonly] {
            background-color: #f5f5f5;
        }
        .form-group .note {
            font-size: 0.85em;
            color: #666;
            margin-top: 3px;
        }
        .form-actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        .form-actions button {
            flex: 1;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 0.95em;
            font-weight: bold;
        }
        .btn-save {
            background-color: #27ae60;
            color: white;
        }
        .btn-cancel {
            background-color: #95a5a6;
            color: white;
        }
    </style>
</head>
<body>
<main class="page-shell">
    <header class="topbar">
        <div class="brand-block">
            <div class="brand-mark">L</div>
            <div>
                <p class="eyebrow">Quản lý</p>
                <h1>Xin chào, <%= currentUser.getTenND() %></h1>
            </div>
        </div>
        <nav class="nav-actions">
            <a class="nav-link" href="voucher?action=list">Danh sách Voucher</a>
            <a class="nav-link" href="logout" onclick="return confirm('Bạn có chắc muốn đăng xuất?')">Đăng xuất</a>
        </nav>
    </header>

    <section>
        <div class="form-container">
            <h2>Sửa Voucher</h2>
            <form method="post" action="voucher">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="maVoucher" value="<%= voucher.getMaVoucher() %>">

                <div class="form-group">
                    <label>Mã Voucher (không thể sửa)</label>
                    <input type="text" value="<%= voucher.getMaVoucher() %>" readonly>
                </div>

                <div class="form-group">
                    <label>Tên Voucher *</label>
                    <input type="text" name="tenVoucher" required value="<%= voucher.getTenVoucher() %>">
                </div>

                <div class="form-group">
                    <label>Loại Giảm Giá *</label>
                    <select name="loaiGiamGia" required onchange="updateLabel()">
                        <option value="PhanTram" <%= "PhanTram".equals(voucher.getLoaiGiamGia()) ? "selected" : "" %>>Phần trăm (%)</option>
                        <option value="TienMat" <%= "TienMat".equals(voucher.getLoaiGiamGia()) ? "selected" : "" %>>Tiền mặt (đ)</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Giá Trị Giảm *</label>
                    <input type="number" name="giaTriGiamGia" required value="<%= (long) voucher.getGiaTriGiamGia() %>" min="0">
                    <p class="note"><span id="giaBan">
                        <% if ("PhanTram".equals(voucher.getLoaiGiamGia())) { %>
                            Nếu phần trăm: nhập 10 cho 10%
                        <% } else { %>
                            Nếu tiền mặt: nhập 20000 cho giảm 20k đ
                        <% } %>
                    </span></p>
                </div>

                <div class="form-group">
                    <label>Đơn Hàng Tối Thiểu (đ) *</label>
                    <input type="number" name="donGiaTuoiNhap" required value="<%= (long) voucher.getDonGiaTuoiNhap() %>" min="0">
                </div>

                <div class="form-group">
                    <label>Ngày Bắt Đầu *</label>
                    <input type="datetime-local" name="ngayBatDau" required value="<%= voucher.getNgayBatDau().format(formatter) %>">
                </div>

                <div class="form-group">
                    <label>Ngày Kết Thúc *</label>
                    <input type="datetime-local" name="ngayKetThuc" required value="<%= voucher.getNgayKetThuc().format(formatter) %>">
                </div>

                <div class="form-group">
                    <label>Số Lần Sử Dụng (tổng) *</label>
                    <input type="number" name="soLanDung" required value="<%= voucher.getSoLanDung() %>" min="1">
                    <p class="note">Tổng số lần có thể sử dụng voucher</p>
                </div>

                <div class="form-group">
                    <label>Số Lần Còn Lại *</label>
                    <input type="number" name="soLanConLai" required value="<%= voucher.getSoLanConLai() %>" min="0">
                    <p class="note">Số lần còn có thể sử dụng (giảm tự động khi khách dùng)</p>
                </div>

                <div class="form-group">
                    <label>Trạng Thái</label>
                    <select name="trangThai">
                        <option value="1" <%= voucher.isTrangThai() ? "selected" : "" %>>Hoạt động</option>
                        <option value="0" <%= !voucher.isTrangThai() ? "selected" : "" %>>Vô hiệu hóa</option>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-save">Lưu Thay Đổi</button>
                    <a href="voucher?action=list" style="flex: 1; text-decoration: none;">
                        <button type="button" class="btn-cancel" style="width: 100%;">Hủy</button>
                    </a>
                </div>
            </form>
        </div>
    </section>
</main>

<script>
    function updateLabel() {
        const loaiGiamGia = document.querySelector('select[name="loaiGiamGia"]').value;
        const label = document.getElementById('giaBan');
        if (loaiGiamGia === 'PhanTram') {
            label.textContent = 'Nếu phần trăm: nhập 10 cho 10%';
        } else if (loaiGiamGia === 'TienMat') {
            label.textContent = 'Nếu tiền mặt: nhập 20000 cho giảm 20k đ';
        }
    }
</script>
</body>
</html>

