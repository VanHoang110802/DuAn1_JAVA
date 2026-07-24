<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.*" %>

<%
  NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
  if (currentUser == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  HoaDon currentHD = (HoaDon) request.getAttribute("currentHD");
  List<Ban> listBan = (List<Ban>) request.getAttribute("listBan");
  List<ThucDon> listMon = (List<ThucDon>) request.getAttribute("listMon");
  List<HoaDon> listHD = (List<HoaDon>) request.getAttribute("listHD");
  List<ChiTietHoaDon> listCTHD = (List<ChiTietHoaDon>) request.getAttribute("listCTHD");
  boolean viewOnly = request.getAttribute("viewOnly") != null && (Boolean) request.getAttribute("viewOnly");
%>

<%
  // Pagination attributes are provided by controller when listing invoices
  int pageSize = request.getAttribute("pageSize") != null ? (Integer) request.getAttribute("pageSize") : 10;
  int currentPage = request.getAttribute("currentPage") != null ? (Integer) request.getAttribute("currentPage") : 1;
  int totalHD = request.getAttribute("totalHD") != null ? (Integer) request.getAttribute("totalHD") : (listHD == null ? 0 : listHD.size());
  int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : (int) Math.ceil((double) totalHD / pageSize);
%>


<html>
<head>
  <title>Trang nhân viên phục vụ</title>
  <link rel="stylesheet" href="assets/app.css">
</head>
<body>
<main class="page-shell">
  <header class="topbar">
    <div class="brand-block">
      <div class="brand-mark">L</div>
      <div>
        <p class="eyebrow">Nhân viên phục vụ</p>
        <h1>Xin chào, <%= currentUser.getTenND() %></h1>
        <p class="muted">Theo dõi bàn, gọi món và thanh toán trong cùng một dashboard.</p>
      </div>
    </div>

    <nav class="nav-actions">
      <a class="nav-link" href="index">Trang chính</a>
      <a class="nav-link" href="hoadon?action=list">Danh sách hóa đơn</a>
      <a class="nav-link" href="logout">Đăng xuất</a>
    </nav>
  </header>

  <section class="dashboard-grid">
    <div class="panel">
      <div class="panel-header">
        <h2>Danh sách bàn</h2>
        <span class="status done"><%= listBan == null ? 0 : listBan.size() %> bàn</span>
      </div>
      <div class="panel-body table-wrap">
        <table>
          <tr><th>Mã bàn</th><th>Số ghế</th><th>Tình trạng</th></tr>
          <% if (listBan != null) {
            for (Ban b : listBan) { %>
          <tr>
            <td><strong><%= b.getMaBan() %></strong></td>
            <td><%= b.getSoGhe() %></td>
            <td>
              <% if (!b.isTinhTrang()) { %>
              <form action="ban" method="post" class="inline-form">
                <input type="hidden" name="action" value="datban">
                <input type="hidden" name="maBan" value="<%= b.getMaBan() %>">
                <button type="submit">Đặt bàn</button>
              </form>
              <% } else { %>
              <span class="status busy">Đang phục vụ</span>
              <% } %>
            </td>
          </tr>
          <% } } %>
        </table>
        <!-- Phân trang cho danh sách bàn trên dashboard -->
          <div class="pagination">
          <% int banTotalPages = request.getAttribute("banTotalPages") != null ? (Integer) request.getAttribute("banTotalPages") : 1;
             int banCurrentPage = request.getAttribute("banCurrentPage") != null ? (Integer) request.getAttribute("banCurrentPage") : 1;
             int banPageSize = request.getAttribute("banPageSize") != null ? (Integer) request.getAttribute("banPageSize") : 10;
             for (int p = 1; p <= banTotalPages; p++) { %>
          <a href="<%= request.getContextPath() %>/hoadon?action=list&banPage=<%= p %>&banSize=<%= banPageSize %>" class="<%= (p == banCurrentPage ? "active" : "") %>">Trang <%= p %></a>
          <% } %>
        </div>
      </div>
    </div>

    <div class="panel">
      <div class="panel-header">
        <h2>Thực đơn</h2>
        <% if (currentHD != null) { %>
        <span class="status done">HD <%= currentHD.getMaHD() %></span>
        <% } %>
      </div>
      <div class="panel-body table-wrap">
        <% if (currentHD != null && !"Đã thanh toán".equals(currentHD.getTrangThai())) { %>
        <table>
          <tr><th>Mã món</th><th>Tên món</th><th>Giá</th><th>Số lượng</th><th>Chọn</th></tr>
          <% if (listMon != null) {
            for (ThucDon td : listMon) { %>
          <tr>
            <td><%= td.getMaItem() %></td>
            <td><strong><%= td.getTenItem() %></strong></td>
            <td><%= td.getGia() %></td>
            <td colspan="2">
              <form action="chitiethoadon" method="post" class="inline-form">
                <input type="hidden" name="action" value="insert">
                <input type="hidden" name="maHD" value="<%= currentHD.getMaHD() %>">
                <input type="hidden" name="maItem" value="<%= td.getMaItem() %>">
                <input type="number" name="soLuong" value="1" min="1">
                <button type="submit">Thêm</button>
              </form>
            </td>
          </tr>
          <% } } %>
        </table>
          <% } else { %>
          <p class="muted">Chọn hoặc đặt bàn để bắt đầu thêm món vào hóa đơn.</p>
          <% } %>
          <!-- Phân trang cho Thực đơn trên dashboard -->
            <div class="pagination">
              <% int monTotalPages = request.getAttribute("monTotalPages") != null ? (Integer) request.getAttribute("monTotalPages") : 1;
                 int monCurrentPage = request.getAttribute("monCurrentPage") != null ? (Integer) request.getAttribute("monCurrentPage") : 1;
                 int monPageSize = request.getAttribute("monPageSize") != null ? (Integer) request.getAttribute("monPageSize") : 10;
                 for (int p = 1; p <= monTotalPages; p++) { %>
             <a href="<%= request.getContextPath() %>/hoadon?action=list&monPage=<%= p %>&monSize=<%= monPageSize %>" class="<%= (p == monCurrentPage ? "active" : "") %>">Trang <%= p %></a>
             <% } %>
           </div>
      </div>
    </div>



    <div class="panel">
      <div class="panel-header">
        <h2>Danh sách hóa đơn</h2>
        <span class="status done"><%= listHD == null ? 0 : listHD.size() %> hóa đơn</span>
      </div>
      <div class="panel-body table-wrap">
        <table>
          <tr>
            <th>Mã HD</th><th>Ngày lập</th><th>Tổng tiền</th>
            <th>Trạng thái</th><th>Mã bàn</th><th>Mã NV</th>
          </tr>
          <% if (listHD != null) {
            for (HoaDon hd : listHD) { %>
          <tr>
            <td><a href="chitiethoadon?action=list&maHD=<%= hd.getMaHD() %>"><%= hd.getMaHD() %></a></td>
            <td><%= hd.getNgayLap() %></td>
            <td><strong><%= hd.getTongTien() %></strong></td>
            <td><%= hd.getTrangThai() %></td>
            <td><%= hd.getMaBan() %></td>
            <td><%= hd.getMaND() %></td>
          </tr>
          <% } } %>
        </table>


        <!-- Phân trang -->
        <div class="pagination">
          <% for (int p = 1; p <= totalPages; p++) { %>
          <a href="<%= request.getContextPath() %>/hoadon?action=list&page=<%= p %>&size=<%= pageSize %>" class="<%= (p == currentPage ? "active" : "") %>">Trang <%= p %></a>
          <% } %>
        </div>

      </div>
    </div>

    <div class="panel">
      <div class="panel-header">
        <h2>Chi tiết hóa đơn</h2>
        <% if (currentHD != null) { %>
        <span class="status done">HD <%= currentHD.getMaHD() %></span>
        <% } %>
      </div>
      <div class="panel-body table-wrap">
        <% if (listCTHD != null && currentHD != null) { %>

        <!-- Form thanh toán (ẩn khi xem read-only) -->
        <% if (!viewOnly) { %>
          <% if (!"Đã thanh toán".equals(currentHD.getTrangThai())) { %>
          <% String payError = (String) request.getAttribute("error"); %>
          <% if (payError != null) { %>
            <p class="notice error"><%= payError %></p>
          <% } %>
          <form id="payForm" action="hoadon" method="post" class="pay-form">
            <input type="hidden" name="action" value="pay">
            <input type="hidden" name="maHD" value="<%= currentHD.getMaHD() %>">
            <label>Khách hàng (tên): <input type="text" name="customerName" placeholder="Tên khách" /></label>
            <label>SDT: <input type="tel" name="customerPhone" placeholder="Số điện thoại" /></label>
            <%-- Hiển thị tổng tiền để nhân viên biết số cần thu --%>
            <label>Tổng tiền: <input type="text" value="<%= currentHD.getTongTien() %>" readonly /></label>
            <input type="hidden" id="invoiceTotal" value="<%= currentHD.getTongTien() %>" />
            <input id="tienKhachDua" type="number" name="tienKhachDua" placeholder="Tiền khách đưa" required>
            <button type="submit">Thanh toán</button>
          </form>
          <% } else { %>
          <p class="notice success">Đã thanh toán. Tiền thừa: <%= currentHD.getTienThua() %></p>
          <%-- Nếu controller cung cấp thông tin biên lai (khách, sđt), hiển thị ở đây --%>
          <% if (request.getAttribute("receiptCustomerName") != null) { %>
            <div class="receipt">
              <h3>Biên lai thanh toán</h3>
              <p>Mã hóa đơn: <strong><%= request.getAttribute("receiptMaHD") %></strong></p>
              <p>Khách hàng: <strong><%= request.getAttribute("receiptCustomerName") %></strong></p>
              <p>SDT: <strong><%= request.getAttribute("receiptCustomerPhone") %></strong></p>
              <p>Tổng tiền: <strong><%= request.getAttribute("receiptTongTien") %></strong></p>
              <p>Tiền thừa: <strong><%= request.getAttribute("receiptTienThua") %></strong></p>
            </div>
          <% } %>
          <% } %>
        <% } else { %>
          <!-- viewOnly mode: show basic customer info if available -->
          <div class="receipt">
            <h3>Thông tin khách hàng</h3>
            <% String rcName = (String) request.getAttribute("receiptCustomerName"); %>
            <% String rcPhone = (String) request.getAttribute("receiptCustomerPhone"); %>
            <% if (rcName != null || rcPhone != null) { %>
              <p>Khách hàng: <strong><%= rcName != null ? rcName : "-" %></strong></p>
              <p>SDT: <strong><%= rcPhone != null ? rcPhone : "-" %></strong></p>
            <% } else { %>
              <p class="muted">Không có thông tin khách hàng cho hóa đơn này.</p>
            <% } %>
          </div>
        <% } %>

        <!-- Hủy đơn (ẩn khi xem read-only) -->
        <% if (!viewOnly) { %>
        <form action="hoadon" method="post" class="inline-form">
          <input type="hidden" name="action" value="cancel">
          <input type="hidden" name="maHD" value="<%= currentHD.getMaHD() %>">
          <button type="submit" onclick="return confirm('Bạn có chắc muốn hủy hóa đơn này?')">Hủy hóa đơn</button>
        </form>
        <% } %>


        <!-- Bảng chi tiết món -->
        <table>
          <tr>
            <th>Mã HD</th><th>Mã món</th><th>Số lượng</th>
            <th>Đơn giá</th><th>Thành tiền</th><th>Thao tác</th>
          </tr>
          <% for (ChiTietHoaDon ct : listCTHD) { %>
          <tr>
            <td><%= ct.getMaHD() %></td>
            <td><%= ct.getMaItem() %></td>
            <td><%= ct.getSoLuong() %></td>
            <td><%= ct.getDonGia() %></td>
            <td><strong><%= ct.getSoLuong() * ct.getDonGia() %></strong></td>
            <td>
              <% if (!viewOnly) { %>
              <div class="table-actions" style="white-space:nowrap;">
                <!-- Form sửa số lượng -->
                <form action="chitiethoadon" method="post" class="inline-form">
                  <input type="hidden" name="action" value="update">
                  <input type="hidden" name="maHD" value="<%= ct.getMaHD() %>">
                  <input type="hidden" name="maItem" value="<%= ct.getMaItem() %>">
                  <input type="number" name="soLuong" value="<%= ct.getSoLuong() %>" min="1">
                  <button type="submit">Sửa</button>
                </form>

                <!-- Form xóa món -->
                <form action="chitiethoadon" method="post" class="inline-form">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="maHD" value="<%= ct.getMaHD() %>">
                  <input type="hidden" name="maItem" value="<%= ct.getMaItem() %>">
                  <button type="submit">Xóa</button>
                </form>
              </div>
              <% } else { %>
                <span class="muted">-</span>
              <% } %>
            </td>
          </tr>
          <% } %>
        </table>
        <% } else { %>
        <p class="muted">Mở một hóa đơn để xem chi tiết món đã gọi.</p>
        <% } %>
        <!-- Phân trang chi tiết hóa đơn -->
        <% if (request.getAttribute("ctTotalPages") != null) { %>
          <div class="pagination">
           <% int ctTotalPages = (Integer) request.getAttribute("ctTotalPages");
              int ctCurrentPage = request.getAttribute("ctCurrentPage") != null ? (Integer) request.getAttribute("ctCurrentPage") : 1;
              int ctPageSize = request.getAttribute("ctPageSize") != null ? (Integer) request.getAttribute("ctPageSize") : 10;
              for (int p = 1; p <= ctTotalPages; p++) { %>
           <a href="<%= request.getContextPath() %>/chitiethoadon?action=list&maHD=<%= currentHD != null ? currentHD.getMaHD() : "" %>&page=<%= p %>&size=<%= ctPageSize %>" class="<%= (p == ctCurrentPage ? "active" : "") %>">Trang <%= p %></a>
           <% } %>
         </div>
        <% } %>
      </div>
    </div>
  </section>
</main>
<script>
  // Client-side validation for payment form
  (function(){
    var payForm = document.getElementById('payForm');
    if (!payForm) return;
    payForm.addEventListener('submit', function(e){
      // Clear previous errors
      var existing = document.querySelector('.pay-form .client-error');
      if (existing) existing.remove();

      var customerNameEl = payForm.querySelector('input[name="customerName"]');
      var customerPhoneEl = payForm.querySelector('input[name="customerPhone"]');
      var totalEl = document.getElementById('invoiceTotal');
      var givenEl = document.getElementById('tienKhachDua');

      var errorMsg = '';

      // Validate customer name (bắt buộc)
      if (!customerNameEl || !customerNameEl.value.trim()) {
        errorMsg = 'Tên khách hàng không được để trống!';
      } else if (customerNameEl.value.trim().length < 2) {
        errorMsg = 'Tên khách hàng phải có ít nhất 2 ký tự!';
      }

      // Validate phone (optional, nhưng nếu nhập thì phải 10-11 chữ số)
      if (!errorMsg && customerPhoneEl && customerPhoneEl.value.trim()) {
        var phone = customerPhoneEl.value.trim();
        if (!/^\d{10,11}$/.test(phone)) {
          errorMsg = 'Số điện thoại không hợp lệ (phải là 10-11 chữ số)!';
        }
      }

      // Validate amount
      if (!errorMsg && (!totalEl || !givenEl)) {
        errorMsg = 'Thiếu thông tin tính tiền, vui lòng thử lại!';
      } else if (!errorMsg) {
        var total = parseFloat(totalEl.value) || 0;
        var given = parseFloat(givenEl.value) || 0;
        if (isNaN(given) || given <= 0) {
          errorMsg = 'Tiền khách đưa phải là số dương!';
        } else if (given < total) {
          errorMsg = 'Số tiền khách đưa chưa đủ (cần: ' + total + ').';
        }
      }

      // Show error if any
      if (errorMsg) {
        e.preventDefault();
        var p = document.createElement('p');
        p.className = 'notice error client-error';
        p.textContent = errorMsg;
        payForm.insertBefore(p, payForm.firstChild);
        if (customerNameEl && !customerNameEl.value.trim()) {
          customerNameEl.focus();
        } else if (customerPhoneEl && customerPhoneEl.value.trim() && !/^\d{10,11}$/.test(customerPhoneEl.value.trim())) {
          customerPhoneEl.focus();
        } else if (givenEl) {
          givenEl.focus();
        }
      }
    });
  })();
</script>
</body>
</html>
