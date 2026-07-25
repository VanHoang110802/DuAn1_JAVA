<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.Ban" %>
<%@ page import="com.example.Entity.NguoiDung" %>

<%
  List<Ban> listBan = (List<Ban>) request.getAttribute("listBan");
  NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
  if (currentUser == null || !"QuanLy".equals(currentUser.getVaiTro())) {
    response.sendRedirect("index.jsp");
    return;
  }
%>

<html>
<head>
  <title>Quản lý bàn</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="page-shell">
  <header class="topbar">
    <div class="brand-block">
      <div class="brand-mark">B</div>
      <div>
        <p class="eyebrow">Admin</p>
        <h1>Quản lý bàn</h1>
        <p class="muted">Thêm bàn mới, cập nhật số ghế và trạng thái phục vụ.</p>
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
        <h2>Thêm bàn mới</h2>
      </div>
      <div class="panel-body">
        <form action="ban" method="post" class="stack-form">
          <input type="hidden" name="action" value="insert">
          <div class="field">
            <label for="maBan">Mã bàn</label>
            <input type="text" id="maBan" name="maBan" required>
          </div>
          <div class="field">
            <label for="soGhe">Số ghế</label>
            <input type="number" id="soGhe" name="soGhe" min="1" required>
          </div>
          <div class="field">
            <label for="tinhTrang">Tình trạng</label>
            <select id="tinhTrang" name="tinhTrang">
              <option value="false">Trống</option>
              <option value="true">Đang phục vụ</option>
            </select>
          </div>
          <button type="submit">Thêm bàn</button>
        </form>
      </div>
    </div>

    <div class="panel">
      <div class="panel-header">
        <h2>Danh sách bàn</h2>
        <span class="status done"><%= listBan == null ? 0 : listBan.size() %> bàn</span>
      </div>
      <div class="panel-body table-wrap">
        <table>
          <tr>
            <th>Mã bàn</th>
            <th>Số ghế</th>
            <th>Tình trạng</th>
            <th>Hành động</th>
          </tr>
          <% if (listBan != null) {
            for (Ban b : listBan) { %>
          <tr>
            <td><strong><%= b.getMaBan() %></strong></td>
            <td><%= b.getSoGhe() %></td>
            <td>
              <% if (b.isTinhTrang()) { %>
              <span class="status busy">Đang phục vụ</span>
              <% } else { %>
              <span class="status done">Trống</span>
              <% } %>
            </td>
            <td>
              <div class="table-actions">
                <a class="action-link" href="ban?action=edit&maBan=<%= b.getMaBan() %>">Sửa</a>
                <a class="action-link danger" href="ban?action=delete&maBan=<%= b.getMaBan() %>"
                   onclick="return confirm('Xóa bàn này?');">Xóa</a>
              </div>
            </td>
          </tr>
          <% } } %>
        </table>
        <!-- Phân trang -->
        <div class="pagination">
          <% int banTotalPages = request.getAttribute("banTotalPages") != null ? (Integer) request.getAttribute("banTotalPages") : 1;
            int banCurrentPage = request.getAttribute("banCurrentPage") != null ? (Integer) request.getAttribute("banCurrentPage") : 1;
            int banPageSize = request.getAttribute("banPageSize") != null ? (Integer) request.getAttribute("banPageSize") : 10;
            for (int p = 1; p <= banTotalPages; p++) { %>
          <a href="<%= request.getContextPath() %>/ban?action=list&page=<%= p %>&size=<%= banPageSize %>" class="<%= (p == banCurrentPage ? "active" : "") %>">Trang <%= p %></a>
          <% } %>
        </div>
      </div>
    </div>
  </section>
</main>
</body>
</html>