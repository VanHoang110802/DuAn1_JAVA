<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.NguoiDung" %>

<%
  List<NguoiDung> listNV = (List<NguoiDung>) request.getAttribute("listNV");
  NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
  if (currentUser == null || !"QuanLy".equals(currentUser.getVaiTro())) {
    response.sendRedirect("index.jsp");
    return;
  }
%>

<html>
<head>
  <title>Quản lý nhân viên</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="page-shell">
  <header class="topbar">
    <div class="brand-block">
      <div class="brand-mark">N</div>
      <div>
        <p class="eyebrow">Admin</p>
        <h1>Quản lý nhân viên</h1>
        <p class="muted">Tạo tài khoản, phân quyền và cập nhật thông tin đăng nhập.</p>
      </div>
    </div>
    <nav class="nav-actions">
      <a class="nav-link" href="quanly.jsp">Admin</a>
      <a class="nav-link" href="logout">Đăng xuất</a>
    </nav>
  </header>

  <section class="admin-layout">
    <div class="panel form-panel">
      <div class="panel-header">
        <h2>Thêm nhân viên</h2>
      </div>
      <div class="panel-body">
        <form action="nguoidung" method="post" class="stack-form">
          <input type="hidden" name="action" value="insert">
          <div class="field">
            <label for="maND">Mã NV</label>
            <input type="text" id="maND" name="maND" required>
          </div>
          <div class="field">
            <label for="tenND">Tên NV</label>
            <input type="text" id="tenND" name="tenND" required>
          </div>
          <div class="field">
            <label for="vaiTro">Vai trò</label>
            <select id="vaiTro" name="vaiTro">
              <option value="QuanLy">Quản lý</option>
              <option value="NhanVien">Nhân viên</option>
            </select>
          </div>
          <div class="field">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>
          </div>
          <div class="field">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
          </div>
          <button type="submit">Thêm nhân viên</button>
        </form>
      </div>
    </div>

    <div class="panel">
        <div class="panel-header">
          <h2>Danh sách nhân viên</h2>
          <span class="status done"><%= request.getAttribute("nvTotal") != null ? request.getAttribute("nvTotal") : (listNV == null ? 0 : listNV.size()) %> nhân viên</span>
        </div>
      <div class="panel-body table-wrap">
        <table>
          <tr>
            <th>Mã NV</th><th>Tên NV</th><th>Vai trò</th><th>Username</th><th>Hành động</th>
          </tr>
          <% if (listNV != null) {
            for (NguoiDung nv : listNV) { %>
          <tr>
            <td><%= nv.getMaND() %></td>
            <td><strong><%= nv.getTenND() %></strong></td>
            <td><%= nv.getVaiTro() %></td>
            <td><%= nv.getUsername() %></td>
            <td>
              <div class="table-actions">
                <a class="action-link" href="nguoidung?action=edit&maND=<%= nv.getMaND() %>">Sửa</a>
                <a class="action-link danger" href="nguoidung?action=delete&maND=<%= nv.getMaND() %>"
                   onclick="return confirm('Bạn có chắc muốn xóa nhân viên này?');">Xóa</a>
              </div>
            </td>
          </tr>
          <% } } %>
        </table>
        <!-- Phân trang -->
        <div class="pagination">
          <% int nvTotalPages = request.getAttribute("nvTotalPages") != null ? (Integer) request.getAttribute("nvTotalPages") : 1;
             int nvCurrentPage = request.getAttribute("nvCurrentPage") != null ? (Integer) request.getAttribute("nvCurrentPage") : 1;
             int nvPageSize = request.getAttribute("nvPageSize") != null ? (Integer) request.getAttribute("nvPageSize") : 10;
             for (int p = 1; p <= nvTotalPages; p++) { %>
          <a href="nguoidung?action=list&page=<%= p %>&size=<%= nvPageSize %>" class="<%= (p == nvCurrentPage ? "active" : "") %>">Trang <%= p %></a>
          <% } %>
        </div>
        <p>Trang <%= nvCurrentPage %></p>
      </div>
    </div>
  </section>
</main>
</body>
</html>
