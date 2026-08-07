<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.Entity.NguoiDung" %>
<%
  NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
  if (currentUser == null || !"QuanLy".equals(currentUser.getVaiTro())) {
    response.sendRedirect("index.jsp");
    return;
  }
%>
<html>
<head>
  <title>Trang quản lý</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="page-shell">
  <header class="topbar">
    <div class="brand-block">
      <div class="brand-mark">L</div>
      <div>
        <p class="eyebrow">Khu vực quản lý</p>
        <h1>Xin chào, <%= currentUser.getTenND() %></h1>
        <p class="muted">Quản lý bàn, thực đơn và nhân viên.</p>
      </div>
    </div>

    <nav class="nav-actions">
      <!-- Trang phục vụ đã ẩn cho role quản lý -->
      <a class="nav-link" href="logout" onclick="return confirm('Bạn có chắc muốn đăng xuất?')">Đăng xuất</a>
    </nav>
  </header>

  <section class="manager-grid">
    <a class="manager-tile" href="ban?action=list">
      <span>B</span>
      <h3>Quản lý bàn</h3>
      <p class="muted">Thêm, sửa, xóa bàn và cập nhật trạng thái phục vụ.</p>
    </a>
    <a class="manager-tile" href="thucdon?action=list">
      <span>M</span>
      <h3>Quản lý món</h3>
      <p class="muted">Thêm món mới, sửa giá, phân loại món ăn và đồ uống.</p>
    </a>
    <a class="manager-tile" href="nguoidung?action=list">
      <span>N</span>
      <h3>Quản lý nhân viên</h3>
      <p class="muted">Tạo tài khoản, phân quyền và cập nhật thông tin nhân viên.</p>
    </a>
    <a class="manager-tile" href="voucher?action=list">
      <span>V</span>
      <h3>Quản lý voucher</h3>
      <p class="muted">Tạo mã giảm giá, cập nhật hạn sử dụng và quản lý lượt dùng.</p>
    </a>
    <a class="manager-tile" href="hoadon?action=thongke">
      <span>T</span>
      <h3>Thống kê</h3>
      <p class="muted">Báo cáo doanh thu, món bán chạy và báo cáo nhân viên.</p>
    </a>
  </section>
</main>
</body>
</html>