<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.ThucDon" %>
<%@ page import="com.example.Entity.NguoiDung" %>

<%
  List<ThucDon> listMon = (List<ThucDon>) request.getAttribute("listMon");
  NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
  if (currentUser == null || !"QuanLy".equals(currentUser.getVaiTro())) {
    response.sendRedirect("index.jsp");
    return;
  }
%>

<html>
<head>
  <title>Quản lý thực đơn</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="page-shell">
  <header class="topbar">
    <div class="brand-block">
      <div class="brand-mark">M</div>
      <div>
        <p class="eyebrow">Admin</p>
        <h1>Quản lý món</h1>
        <p class="muted">Thêm món, cập nhật giá bán, loại món và đơn vị tính.</p>
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
        <h2>Thêm món mới</h2>
      </div>
      <div class="panel-body">
        <form action="thucdon" method="post" class="stack-form" enctype="multipart/form-data">
          <input type="hidden" name="action" value="insert">
          <div class="field">
            <label for="maItem">Mã món</label>
            <input type="text" id="maItem" name="maItem" required>
          </div>
          <div class="field">
            <label for="tenItem">Tên món</label>
            <input type="text" id="tenItem" name="tenItem" required>
          </div>
          <div class="field">
            <label for="gia">Giá</label>
            <input type="number" id="gia" name="gia" min="0" step="1000" required>
          </div>
          <div class="field">
            <label for="loai">Loại</label>
            <input type="text" id="loai" name="loai" placeholder="Món lẩu, topping, đồ uống..." required>
          </div>
          <div class="field">
            <label for="donViTinh">Đơn vị tính</label>
            <input type="text" id="donViTinh" name="donViTinh" placeholder="Phần, đĩa, chai..." required>
          </div>


          <div class="field">
            <label for="image">Ảnh món (jpg/png)</label>
            <input type="file" id="image" name="image" accept="image/*">
          </div>
          <button type="submit">Thêm món</button>
        </form>
      </div>
    </div>

    <div class="panel">
      <div class="panel-header">
        <h2>Danh sách món</h2>
        <span class="status done"><%= listMon == null ? 0 : listMon.size() %> món</span>
      </div>
      <div class="panel-body table-wrap">
        <table>
          <tr>
            <th>Ảnh</th><th>Mã món</th><th>Tên món</th><th>Giá</th><th>Loại</th><th>Đơn vị</th><th>Hành động</th>
          </tr>
          <% if (listMon != null) {
            for (ThucDon td : listMon) { %>
          <tr>
            <td>
              <% String img = td.getImagePath(); %>
              <img src="<%= request.getContextPath() %>/images/<%= (img != null && !img.isEmpty()) ? img : "default-food.png" %>" alt="" style="width:64px;height:48px;object-fit:cover;"/>
            </td>
            <td><%= td.getMaItem() %></td>
            <td><strong><%= td.getTenItem() %></strong></td>
            <td><%= td.getGia() %></td>
            <td><%= td.getLoai() %></td>
            <td><%= td.getDonViTinh() %></td>
            <td>
              <div class="table-actions">
                <a class="action-link" href="thucdon?action=edit&maItem=<%= td.getMaItem() %>">Sửa</a>
                <a class="action-link danger" href="thucdon?action=delete&maItem=<%= td.getMaItem() %>"
                   onclick="return confirm('Bạn có chắc muốn xóa món này?');">Xóa</a>
              </div>
            </td>
          </tr>
          <% } } %>
        </table>
        <!-- Phân trang -->
        <div class="pagination">
          <% int monTotalPages = request.getAttribute("monTotalPages") != null ? (Integer) request.getAttribute("monTotalPages") : 1;
            int monCurrentPage = request.getAttribute("monCurrentPage") != null ? (Integer) request.getAttribute("monCurrentPage") : 1;
            int monPageSize = request.getAttribute("monPageSize") != null ? (Integer) request.getAttribute("monPageSize") : 10;
            for (int p = 1; p <= monTotalPages; p++) { %>
          <a href="thucdon?action=list&page=<%= p %>&size=<%= monPageSize %>" class="<%= (p == monCurrentPage ? "active" : "") %>">Trang <%= p %></a>
          <% } %>
        </div>
      </div>
    </div>
  </section>
</main>
</body>
</html>