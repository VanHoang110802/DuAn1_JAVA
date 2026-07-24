<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Đăng nhập hệ thống</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="login-page">
  <section class="login-card">
    <div class="login-visual">
      <p class="eyebrow">Hotpot POS</p>
      <h1>Quản lý bàn nhà hàng Lẩu</h1>
    </div>

    <div class="login-form">
      <p class="eyebrow">Đăng nhập</p>
      <h2>Vào hệ thống</h2>
      <form action="login" method="post">
        <div class="form-group">
          <label for="username">Tên đăng nhập</label>
          <input type="text" id="username" name="username" required>
        </div>

        <div class="form-group">
          <label for="password">Mật khẩu</label>
          <input type="password" id="password" name="password" required>
        </div>

        <button type="submit">Đăng nhập</button>
      </form>

      <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
      %>
      <p class="notice error"><%= error %></p>
      <%
        }
      %>
    </div>
  </section>
</main>
</body>
</html>
