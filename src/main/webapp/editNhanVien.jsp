<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.Entity.NguoiDung" %>

<%
  NguoiDung nv = (NguoiDung) request.getAttribute("nv");
  if (nv == null) {
    response.sendRedirect("nguoidung?action=list");
    return;
  }
%>

<html>
<head>
  <title>Sửa nhân viên</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="edit-shell">
  <section class="panel edit-card">
    <div class="panel-header">
      <h2>Sửa nhân viên</h2>
      <span class="status done"><%= nv.getMaND() %></span>
    </div>
    <div class="panel-body">
      <form action="nguoidung" method="post" class="stack-form">
        <input type="hidden" name="action" value="update">
        <div class="field">
          <label for="maND">Mã NV</label>
          <input type="text" id="maND" name="maND" value="<%= nv.getMaND() %>" readonly>
        </div>
        <div class="field">
          <label for="tenND">Tên NV</label>
          <input type="text" id="tenND" name="tenND" value="<%= nv.getTenND() %>" required>
        </div>
        <div class="field">
          <label for="vaiTro">Vai trò</label>
          <select id="vaiTro" name="vaiTro">
            <option value="QuanLy" <%= "QuanLy".equals(nv.getVaiTro()) ? "selected" : "" %>>Quản lý</option>
            <option value="NhanVien" <%= "NhanVien".equals(nv.getVaiTro()) ? "selected" : "" %>>Nhân viên</option>
          </select>
        </div>
        <div class="field">
          <label for="username">Username</label>
          <input type="text" id="username" name="username" value="<%= nv.getUsername() %>" required>
        </div>
        <div class="field">
          <label for="password">Password</label>
          <input type="password" id="password" name="password" value="<%= nv.getPassword() %>" required>
        </div>
        <button type="submit">Cập nhật</button>
        <a class="nav-link" href="nguoidung?action=list" style="text-align:center;">Quay lại danh sách</a>
      </form>
    </div>
  </section>
</main>
</body>
</html>
