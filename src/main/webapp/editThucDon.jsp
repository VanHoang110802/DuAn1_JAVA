<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.Entity.ThucDon" %>

<%
  ThucDon td = (ThucDon) request.getAttribute("mon");
  if (td == null) {
    response.sendRedirect("thucdon?action=list");
    return;
  }
%>

<html>
<head>
  <title>Sửa món</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="edit-shell">
  <section class="panel edit-card">
    <div class="panel-header">
      <h2>Sửa thông tin món</h2>
      <span class="status done"><%= td.getMaItem() %></span>
    </div>
    <div class="panel-body">
      <form action="thucdon" method="post" class="stack-form">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="maItem" value="<%= td.getMaItem() %>">
        <div class="field">
          <label for="tenItem">Tên món</label>
          <input type="text" id="tenItem" name="tenItem" value="<%= td.getTenItem() %>" required>
        </div>
        <div class="field">
          <label for="gia">Giá</label>
          <input type="number" id="gia" name="gia" value="<%= td.getGia() %>" min="0" step="1000" required>
        </div>
        <div class="field">
          <label for="loai">Loại</label>
          <input type="text" id="loai" name="loai" value="<%= td.getLoai() %>" required>
        </div>
        <div class="field">
          <label for="donViTinh">Đơn vị tính</label>
          <input type="text" id="donViTinh" name="donViTinh" value="<%= td.getDonViTinh() %>" required>
        </div>
        <button type="submit">Cập nhật</button>
        <a class="nav-link" href="thucdon?action=list" style="text-align:center;">Quay lại danh sách</a>
      </form>
    </div>
  </section>
</main>
</body>
</html>
