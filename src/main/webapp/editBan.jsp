<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.Entity.Ban" %>

<%
  Ban ban = (Ban) request.getAttribute("ban");
  if (ban == null) {
    response.sendRedirect("ban?action=list");
    return;
  }
%>

<html>
<head>
  <title>Sửa bàn</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="edit-shell">
  <section class="panel edit-card">
    <div class="panel-header">
      <h2>Sửa thông tin bàn</h2>
      <span class="status done"><%= ban.getMaBan() %></span>
    </div>
    <div class="panel-body">
      <form action="ban" method="post" class="stack-form">
        <input type="hidden" name="action" value="update">
        <div class="field">
          <label for="maBan">Mã bàn</label>
          <input type="text" id="maBan" name="maBan" value="<%= ban.getMaBan() %>" readonly>
        </div>
        <div class="field">
          <label for="soGhe">Số ghế</label>
          <input type="number" id="soGhe" name="soGhe" value="<%= ban.getSoGhe() %>" min="1" required>
        </div>
        <div class="field">
          <label for="tinhTrang">Tình trạng</label>
          <select id="tinhTrang" name="tinhTrang">
            <option value="false" <%= !ban.isTinhTrang() ? "selected" : "" %>>Trống</option>
            <option value="true" <%= ban.isTinhTrang() ? "selected" : "" %>>Đang phục vụ</option>
          </select>
        </div>
        <button type="submit">Cập nhật</button>
        <a class="nav-link" href="ban?action=list" style="text-align:center;">Quay lại danh sách</a>
      </form>
    </div>
  </section>
</main>
</body>
</html>
