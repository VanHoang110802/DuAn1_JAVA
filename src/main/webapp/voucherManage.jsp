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
    int pageSize = request.getAttribute("pageSize") != null ? (Integer) request.getAttribute("pageSize") : 10;
    int currentPage = request.getAttribute("currentPage") != null ? (Integer) request.getAttribute("currentPage") : 1;
    int totalVoucher = request.getAttribute("totalVoucher") != null ? (Integer) request.getAttribute("totalVoucher") : 0;
    int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : 1;
%>

<html>
<head>
    <title>Quản lý Voucher</title>
    <link rel="stylesheet" href="assets/app.css">
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
            <a class="nav-link" href="index">Trang chính</a>
            <a class="nav-link" href="logout">Đăng xuất</a>
        </nav>
    </header>

    <section class="dashboard-grid">
        <div class="panel">
            <div class="panel-header">
                <h2>Quản lý Voucher</h2>
                <span class="status done"><%= totalVoucher %> voucher</span>
            </div>
            <div class="panel-body">
                <a href="voucher?action=add" style="margin-bottom: 15px; display: inline-block;">
                    <button style="background-color: #27ae60; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;">
                        + Thêm voucher mới
                    </button>
                </a>
            </div>
            <div class="panel-body table-wrap">
                <table>
                    <tr>
                        <th>Mã Voucher</th>
                        <th>Tên Voucher</th>
                        <th>Loại Giảm</th>
                        <th>Giá Trị</th>
                        <th>Còn lượt</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                    <% if (listVoucher != null) {
                        for (Voucher v : listVoucher) { %>
                    <tr>
                        <td><strong><%= v.getMaVoucher() %></strong></td>
                        <td><%= v.getTenVoucher() %></td>
                        <td>
                            <% if ("TienMat".equals(v.getLoaiGiamGia())) { %>
                                Tiền mặt
                            <% } else { %>
                                Phần trăm
                            <% } %>
                        </td>
                        <td>
                            <% if ("TienMat".equals(v.getLoaiGiamGia())) { %>
                                <%= String.format("%,d", (long) v.getGiaTriGiamGia()) %> đ
                            <% } else { %>
                                <%= (long) v.getGiaTriGiamGia() %>%
                            <% } %>
                        </td>
                        <td><%= v.getSoLanConLai() %> / <%= v.getSoLanDung() %></td>
                        <td>
                            <% if (v.isTrangThai()) { %>
                                <span style="color: #27ae60; font-weight: bold;">Hoạt động</span>
                            <% } else { %>
                                <span style="color: #e74c3c; font-weight: bold;">Vô hiệu</span>
                            <% } %>
                        </td>
                        <td>
                            <div style="white-space: nowrap;">
                                <a href="voucher?action=edit&maVoucher=<%= v.getMaVoucher() %>" style="margin-right: 5px;">
                                    <button style="background-color: #3498db; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer;">Sửa</button>
                                </a>
                                <form action="voucher" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="maVoucher" value="<%= v.getMaVoucher() %>">
                                    <button type="submit" style="background-color: #e74c3c; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer;" onclick="return confirm('Bạn có chắc muốn xóa voucher này?')">Xóa</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    <% } } %>
                </table>

                <!-- Phân trang -->
                <div class="pagination" style="margin-top: 20px;">
                    <% for (int p = 1; p <= totalPages; p++) { %>
                        <a href="<%= request.getContextPath() %>/voucher?action=list&page=<%= p %>&size=<%= pageSize %>"
                           class="<%= (p == currentPage ? "active" : "") %>">Trang <%= p %></a>
                    <% } %>
                </div>
            </div>
        </div>
    </section>
</main>
</body>
</html>

