<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Entity.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>

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
                <h1>Xin chào, <%= currentUser.getTenND() %>
                </h1>
                <p class="muted">Theo dõi bàn, gọi món và thanh toán trong cùng một dashboard.</p>
            </div>
        </div>

        <nav class="nav-actions">
            <a class="nav-link" href="index">Trang chính</a>
            <a class="nav-link" href="hoadon?action=list">Danh sách hóa đơn</a>
            <% if ("QuanLy".equals(currentUser.getVaiTro())) { %>
              <a class="nav-link" href="quanly">Quản lý</a>
            <% } %>
            <a class="nav-link" href="logout">Đăng xuất</a>
        </nav>
    </header>

    <% if ("NhanVien".equals(currentUser.getVaiTro())) { %>
    <section class="dashboard-grid">
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
                    </tr>
                    <% if (listBan != null) {
                        for (Ban b : listBan) { %>
                    <tr>
                        <td><strong><%= b.getMaBan() %>
                        </strong></td>
                        <td><%= b.getSoGhe() %>
                        </td>
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
                    <% }
                    } %>
                </table>
                <!-- Phân trang cho danh sách bàn trên dashboard -->
                <div class="pagination">
                    <% int banTotalPages = request.getAttribute("banTotalPages") != null ? (Integer) request.getAttribute("banTotalPages") : 1;
                        int banCurrentPage = request.getAttribute("banCurrentPage") != null ? (Integer) request.getAttribute("banCurrentPage") : 1;
                        int banPageSize = request.getAttribute("banPageSize") != null ? (Integer) request.getAttribute("banPageSize") : 10;
                        for (int p = 1; p <= banTotalPages; p++) { %>
                    <a href="<%= request.getContextPath() %>/hoadon?action=list&banPage=<%= p %>&banSize=<%= banPageSize %>"
                       class="<%= (p == banCurrentPage ? "active" : "") %>">Trang <%= p %>
                    </a>
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
                <div style="margin-top:8px;">
                  <!-- Search in dashboard should go to HoaDonController so results stay on index.jsp -->
                  <%-- Only allow searching menu when a table/hóa đơn is opened (currentHD) --%>
                  <% if (currentHD != null && "Đang phục vụ".equals(currentHD.getTrangThai())) { %>
                    <form action="hoadon" method="get" style="display:inline-flex; gap:6px; align-items:center;">
                        <input type="hidden" name="action" value="open" />
                        <input type="hidden" name="maHD" value="<%= currentHD.getMaHD() %>" />
                        <input type="text" name="monSearch" placeholder="Tìm mã/tên món" value="<%= request.getAttribute("monSearch") != null ? request.getAttribute("monSearch") : "" %>" />
                        <button type="submit">Tìm</button>
                    </form>
                  <% } else { %>
                    <div style="display:inline-flex; gap:6px; align-items:center;">
                        <input type="text" placeholder="Bạn phải đặt bàn trước để tìm món" disabled style="opacity:0.7;" />
                        <button type="button" onclick="window.location.href='ban?action=list'" title="Đặt bàn">Đặt bàn</button>
                    </div>
                  <% } %>
                </div>
            </div>
            <div class="panel-body table-wrap">
                <% if (currentHD != null && !"Đã thanh toán".equals(currentHD.getTrangThai())) { %>
                <table>
                    <tr>
                        <th>Ảnh</th>
                        <th>Mã món</th>
                        <th>Tên món</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th>Chọn</th>
                    </tr>
                    <% if (listMon != null) {
                        for (ThucDon td : listMon) { %>
                    <tr>
                        <td>
                            <% String img = td.getImagePath(); %>
                            <img src="<%= request.getContextPath() %>/images/<%= (img != null && !img.isEmpty()) ? img : "default-food.png" %>" alt="" style="width:64px;height:48px;object-fit:cover;"/>
                        </td>
                        <td><%= td.getMaItem() %>
                        </td>
                        <td><strong><%= td.getTenItem() %>
                        </strong></td>
                        <td><%= td.getGia() %>
                        </td>
                        <td>
                            <%= td.getSoLuong() %>
                        </td>
                        <td>
                            <% if (td.getSoLuong() <= 0) { %>
                                <span class="status busy">Hết hàng</span>
                            <% } else { %>
                            <form action="chitiethoadon" method="post" class="inline-form" onsubmit="if (<%= td.getSoLuong() %> <= 0) { alert('Món này đã hết hàng, không thể thêm'); return false; } return confirm('Thêm món này vào đơn?');">
                                <input type="hidden" name="action" value="insert">
                                <input type="hidden" name="maHD" value="<%= currentHD.getMaHD() %>">
                                <input type="hidden" name="maItem" value="<%= td.getMaItem() %>">
                                <input type="number" name="soLuong" value="1" min="1" max="<%= td.getSoLuong() %>">
                                <button type="submit">Thêm</button>
                            </form>
                            <% } %>
                        </td>
                    </tr>
                    <% }
                    } %>
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
                    <% if (currentHD != null && "Đang phục vụ".equals(currentHD.getTrangThai())) { %>
                    <a href="<%= request.getContextPath() %>/hoadon?action=open&maHD=<%= currentHD.getMaHD() %>&monPage=<%= p %>&monSize=<%= monPageSize %>"
                       class="<%= (p == monCurrentPage ? "active" : "") %>">Trang <%= p %>
                    </a>
                    <% } else { %>
                    <a href="<%= request.getContextPath() %>/hoadon?action=list&monPage=<%= p %>&monSize=<%= monPageSize %>"
                       class="<%= (p == monCurrentPage ? "active" : "") %>">Trang <%= p %>
                    </a>
                    <% } %>
                    <% } %>
                </div>
            </div>
        </div>


        <div class="panel">
            <div class="panel-header">
                <h2>Danh sách hóa đơn</h2>
                <span class="status done"><%= listHD == null ? 0 : listHD.size() %> hóa đơn</span>
                <div style="margin-top: 10px; font-size: 0.9em; display:flex; gap:8px; align-items:center;">
                  <form action="hoadon" method="get" style="display:inline-flex; gap:6px; align-items:center;">
                    <input type="hidden" name="action" value="list" />
                    <input type="text" name="hdSearch" placeholder="Tìm HD/Mã bàn/Mã NV" value="<%= request.getAttribute("hdSearch") != null ? request.getAttribute("hdSearch") : "" %>" />
                    <button type="submit">Tìm</button>
                  </form>
                    <%
                        String currentSort = (String) request.getAttribute("sort");
                        if (currentSort == null) currentSort = "DESC";
                        String nextSort = currentSort.equals("DESC") ? "ASC" : "DESC";
                        String sortLabel = currentSort.equals("DESC") ? "↓ Mới nhất" : "↑ Cũ nhất";
                        String nextLabel = currentSort.equals("DESC") ? "↑ Cũ nhất" : "↓ Mới nhất";
                    %>
                    Sắp xếp theo ngày:
                    <a href="<%= request.getContextPath() %>/hoadon?action=list&sort=<%= nextSort %>&page=1&size=<%= request.getAttribute("pageSize") != null ? request.getAttribute("pageSize") : 10 %>"
                       style="margin-left: 5px;">
                        <%= nextLabel %>
                    </a>
                </div>
            </div>
            <div class="panel-body table-wrap">
                <table>
                    <tr>
                        <th>Mã HD</th>
                        <th>Ngày lập</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th>Tên bàn</th>
                        <th>Tên NV</th>
                    </tr>
                    <% if (listHD != null) {
                        for (HoaDon hd : listHD) {
                            Ban banInfo = null;
                            if (listBan != null) {
                                for (Ban b : listBan) {
                                    if (b.getMaBan().equals(hd.getMaBan())) {
                                        banInfo = b;
                                        break;
                                    }
                                }
                            }
                            String banName = (banInfo != null) ? banInfo.getMaBan() : hd.getMaBan();
                            String nvName = (hd.getMaND().equals(currentUser.getMaND())) ? currentUser.getTenND() : hd.getMaND();
                    %>
                    <tr>
                        <td>
                            <% if ("Đang phục vụ".equals(hd.getTrangThai())) { %>
                            <a href="hoadon?action=open&maHD=<%= hd.getMaHD() %>"><%= hd.getMaHD() %>
                            </a>
                            <% } else { %>
                            <a href="chitiethoadon?action=list&maHD=<%= hd.getMaHD() %>"><%= hd.getMaHD() %>
                            </a>
                            <% } %>
                        </td>
                        <td><%= hd.getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
                        </td>
                        <td><strong><%= (long) hd.getTongTien() %>
                        </strong></td>
                        <td><%= hd.getTrangThai() %>
                        </td>
                        <td><%= banName %>
                        </td>
                        <td><%= nvName %>
                        </td>
                    </tr>
                    <% }
                    } %>
                </table>


                <!-- Phân trang -->
                <div class="pagination">
                    <% for (int p = 1; p <= totalPages; p++) {
                        String sortParam = (String) request.getAttribute("sort");
                        if (sortParam == null) sortParam = "DESC";
                    %>
                    <a href="<%= request.getContextPath() %>/hoadon?action=list&page=<%= p %>&size=<%= pageSize %>&sort=<%= sortParam %>"
                       class="<%= (p == currentPage ? "active" : "") %>">Trang <%= p %>
                    </a>
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
                <p class="notice error"><%= payError %>
                </p>
                <% } %>
                <form id="payForm" action="hoadon" method="post" class="pay-form" onsubmit="return confirm('Xác nhận thanh toán?')">
                    <input type="hidden" name="action" value="pay">
                    <input type="hidden" name="maHD" value="<%= currentHD.getMaHD() %>">
                    <div class="field-group">
                        <label>Khách hàng</label>
                        <input id="customerName" type="text" name="customerName" placeholder="Tên khách"/>
                    </div>
                    <div class="field-group">
                        <label>SĐT</label>
                        <input id="customerPhone" type="tel" name="customerPhone" placeholder="Số điện thoại"/>
                    </div>

                    <%-- Chọn voucher --%>
                    <%
                        List<Voucher> listVoucher = (List<Voucher>) request.getAttribute("listVoucher");
                        double orderTotal = 0.0;
                        double maxItemValue = 0.0;
                        if (listCTHD != null) {
                            for (ChiTietHoaDon ct : listCTHD) {
                                double itemTotal = ct.getSoLuong() * ct.getDonGia();
                                orderTotal += itemTotal;
                                if (itemTotal > maxItemValue) {
                                    maxItemValue = itemTotal;
                                }
                            }
                        }
                        if (orderTotal == 0.0 && currentHD != null) {
                            orderTotal = currentHD.getTongTien();
                            maxItemValue = orderTotal;
                        }
                        Object orderTotalAttr = request.getAttribute("orderTotal");
                        if (orderTotalAttr instanceof Number) {
                            orderTotal = ((Number) orderTotalAttr).doubleValue();
                        }
                        String selectedVoucherCode = request.getAttribute("selectedVoucherCode") != null ? (String) request.getAttribute("selectedVoucherCode") : "";
                    %>
                    <div class="field-group">
                        <label>Voucher</label>
                        <select id="voucherSelect" name="maVoucher" onchange="calculatePrice()">
                            <option value="">-- Không áp dụng voucher --</option>
                            <% if (listVoucher != null) {
                                LocalDateTime now = LocalDateTime.now();
                                for (Voucher v : listVoucher) {
                                    boolean eligible = orderTotal >= v.getDonGiaTuoiNhap() || maxItemValue >= v.getDonGiaTuoiNhap();
                                    boolean selected = selectedVoucherCode.equals(v.getMaVoucher());
                                    boolean active = v.isConThe(now);
                                    boolean expired = v.getNgayKetThuc().isBefore(now);
                                    boolean notStarted = v.getNgayBatDau().isAfter(now);
                                    boolean noUses = v.getSoLanConLai() <= 0;
                            %>
                            <option value="<%= v.getMaVoucher() %>" data-loai="<%= v.getLoaiGiamGia() %>"
                                    data-value="<%= (long) v.getGiaTriGiamGia() %>"
                                    data-min="<%= (long) v.getDonGiaTuoiNhap() %>" data-label="<%= v.getTenVoucher() %>"
                                    data-eligible="<%= eligible %>" data-active="<%= active %>" data-remaining="<%= v.getSoLanConLai() %>" data-expired="<%= expired %>" data-notstarted="<%= notStarted %>" <%= selected ? "selected" : "" %>>
                                <%= v.getTenVoucher() %>
                                <% if (!active) { %>
                                    <% if (!v.isTrangThai()) { %>(Vô hiệu hóa)<% } else if (noUses) { %>(Hết lượt)<% } else if (expired) { %>(Hết hạn)<% } else if (notStarted) { %>(Chưa đến ngày áp dụng)<% } %>
                                <% } else if (eligible) { %>
                                    (Có thể dùng)
                                <% } else { %>
                                    (Cần tối thiểu <%= String.format("%,d", (long) v.getDonGiaTuoiNhap()) %> đ)
                                <% } %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="field-group">
                        <label>Tiền khách đưa</label>
                        <input id="tienKhachDua" type="number" name="tienKhachDua" placeholder="Nhập số tiền" required>
                    </div>

                    <div class="field-group full-width">
                        <div class="inline-actions">
                            <button type="button" class="secondary-button" onclick="reloadVouchers()">Tải lại</button>
                            <button type="submit">Thanh toán</button>
                        </div>
                    </div>

                    <div class="field-group full-width">
                        <div class="pay-summary">
                            <div class="summary-row">
                                <span class="summary-label">Tổng đơn</span>
                                <span class="summary-value"
                                      id="priceOriginal"><%= String.format("%,d", (long) orderTotal) %> đ</span>
                            </div>

                            <div class="summary-row">
                                <span class="summary-label">Giảm giá</span>
                                <span class="summary-value" id="priceDiscount">0 đ</span>
                            </div>
                            <div class="summary-row">
                                <span class="summary-label">Phải trả</span>
                                <span class="summary-value"
                                      id="priceFinal"><%= String.format("%,d", (long) orderTotal) %> đ</span>
                            </div>
                            <input type="hidden" id="invoiceOriginal" value="<%= orderTotal %>"/>
                            <input type="hidden" id="invoiceItemMax" value="<%= maxItemValue %>"/>
                            <input type="hidden" id="invoiceTotal" value="<%= orderTotal %>"/>
                        </div>
                    </div>
                </form>
                <% } else { %>
                <p class="notice success">Đã thanh toán</p>
                <%-- Nếu controller cung cấp thông tin biên lai (khách, sđt), hiển thị ở đây --%>
                <% if (request.getAttribute("receiptCustomerName") != null) { %>
                <div class="receipt">
                    <h3>Biên lai thanh toán</h3>
                    <p>Mã hóa đơn: <strong><%= request.getAttribute("receiptMaHD") %>
                    </strong></p>
                    <p>Khách hàng: <strong><%= request.getAttribute("receiptCustomerName") %>
                    </strong></p>
                    <p>SDT: <strong><%= request.getAttribute("receiptCustomerPhone") %>
                    </strong></p>
                    <p>Tổng tiền: <strong><%= request.getAttribute("receiptTongTien") %>
                    </strong></p>
                    <% if ((Double) request.getAttribute("receiptTienThua") != null && (Double) request.getAttribute("receiptTienThua") > 0) { %>
                    <p style="color: #27ae60; font-weight: bold;">Tiền thừa:
                        <strong><%= request.getAttribute("receiptTienThua") %>
                        </strong></p>
                    <% } %>
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
                    <p>Khách hàng: <strong><%= rcName != null ? rcName : "-" %>
                    </strong></p>
                    <p>SDT: <strong><%= rcPhone != null ? rcPhone : "-" %>
                    </strong></p>
                    <% } else { %>
                    <p class="muted">Không có thông tin khách hàng cho hóa đơn này.</p>
                    <% } %>
                </div>
                <% } %>

                <!-- Hủy đơn: chỉ hiển thị khi không ở chế độ viewOnly và hóa đơn chưa được thanh toán -->
                <% if (!viewOnly && currentHD != null && !"Đã thanh toán".equals(currentHD.getTrangThai())) { %>
                <form action="hoadon" method="post" class="inline-form">
                    <input type="hidden" name="action" value="cancel">
                    <input type="hidden" name="maHD" value="<%= currentHD.getMaHD() %>">
                    <button type="submit" onclick="return confirm('Bạn có chắc muốn hủy hóa đơn này?')">Hủy hóa đơn
                    </button>
                </form>
                <% } %>


                <!-- Bảng chi tiết món -->
                <table>
                    <tr>
                        <th>Tên món</th>
                        <th>Số lượng</th>
                        <th>Đơn giá</th>
                        <th>Thành tiền</th>
                        <th>Thao tác</th>
                    </tr>
                    <% for (ChiTietHoaDon ct : listCTHD) {
                        ThucDon tenItem = null;
                        if (listMon != null) {
                            for (ThucDon td : listMon) {
                                if (td.getMaItem().equals(ct.getMaItem())) {
                                    tenItem = td;
                                    break;
                                }
                            }
                        }
                        String itemName = (tenItem != null) ? tenItem.getTenItem() : ct.getMaItem();
                    %>
                    <tr>
                        <td><%= itemName %>
                        </td>
                        <td><%= ct.getSoLuong() %>
                        </td>
                        <td><%= ct.getDonGia() %>
                        </td>
                        <td><strong><%= ct.getSoLuong() * ct.getDonGia() %>
                        </strong></td>
                        <td>
                            <% if (!viewOnly && currentHD != null && !"Đã thanh toán".equals(currentHD.getTrangThai())) { %>
                            <div class="table-actions" style="white-space:nowrap;">
                                <!-- Form sửa số lượng -->
                                <form action="chitiethoadon" method="post" class="inline-form" onsubmit="return confirm('Cập nhật số lượng?');">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="maHD" value="<%= ct.getMaHD() %>">
                                    <input type="hidden" name="maItem" value="<%= ct.getMaItem() %>">
                                    <input type="number" name="soLuong" value="<%= ct.getSoLuong() %>" min="1">
                                    <button type="submit">Sửa</button>
                                </form>

                                <!-- Form xóa món -->
                                <form action="chitiethoadon" method="post" class="inline-form" onsubmit="return confirm('Xóa món này khỏi đơn?');">
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
                    <a href="<%= request.getContextPath() %>/chitiethoadon?action=list&maHD=<%= currentHD != null ? currentHD.getMaHD() : "" %>&page=<%= p %>&size=<%= ctPageSize %>"
                       class="<%= (p == ctCurrentPage ? "active" : "") %>">Trang <%= p %>
                    </a>
                    <% } %>
                </div>
                <% } %>
            </div>
        </div>
    </section>
    <% } else { %>
    <section class="dashboard-grid">
      <div class="panel">
        <div class="panel-header">
          <h2>Bảng điều khiển quản lý</h2>
        </div>
        <div class="panel-body">
          <div class="manager-grid">
            <a class="manager-tile" href="ban?action=list">Quản lý bàn</a>
            <a class="manager-tile" href="thucdon?action=list">Quản lý món</a>
            <a class="manager-tile" href="nguoidung?action=list">Quản lý nhân viên</a>
            <a class="manager-tile" href="voucher?action=list">Quản lý voucher</a>
          </div>
        </div>
      </div>
    </section>
    <% } %>
</main>
<script>
    // Tính giá khi chọn voucher
    function calculatePrice() {
        var voucherSelect = document.getElementById('voucherSelect');
        var originalTotal = parseFloat(document.getElementById('invoiceOriginal').value) || 0;
        var itemMax = parseFloat(document.getElementById('invoiceItemMax').value) || 0;
        var priceDiscount = 0;
        var priceFinal = originalTotal;

        removeExistingMessage();

        if (voucherSelect && voucherSelect.value) {
            var selectedOption = voucherSelect.options[voucherSelect.selectedIndex];
            var loai = selectedOption.getAttribute('data-loai');
            var giaTriGiamGia = parseFloat(selectedOption.getAttribute('data-value')) || 0;
            var donGiaTuoiNhap = parseFloat(selectedOption.getAttribute('data-min')) || 0;
            var active = selectedOption.getAttribute('data-active') === 'true';
            var remaining = parseInt(selectedOption.getAttribute('data-remaining')) || 0;
            var expired = selectedOption.getAttribute('data-expired') === 'true';
            var notStarted = selectedOption.getAttribute('data-notstarted') === 'true';

            if (!active) {
                var reason = 'Voucher không thể dùng';
                if (!selectedOption) reason = 'Voucher không hợp lệ';
                else if (!selectedOption.getAttribute('data-active')) reason = 'Voucher vô hiệu hóa';
                if (remaining === 0) reason = 'Voucher đã hết lượt sử dụng';
                if (expired) reason = 'Voucher đã hết hạn';
                if (notStarted) reason = 'Voucher chưa tới ngày áp dụng';

                var payForm = document.getElementById('payForm');
                var p = document.createElement('p');
                p.className = 'notice error client-error';
                p.textContent = reason + '. Vui lòng chọn voucher khác.';
                payForm.insertBefore(p, payForm.firstChild);

                // no discount applied
                priceDiscount = 0;
                priceFinal = originalTotal;
            } else if (originalTotal >= donGiaTuoiNhap || itemMax >= donGiaTuoiNhap) {
                if (loai === 'TienMat') {
                    priceDiscount = giaTriGiamGia;
                } else {
                    priceDiscount = originalTotal * giaTriGiamGia / 100;
                }
                priceFinal = originalTotal - priceDiscount;
            } else {
                // Not meeting min amount — show gentle notice
                var payForm2 = document.getElementById('payForm');
                var p2 = document.createElement('p');
                p2.className = 'notice error client-error';
                p2.textContent = 'Đơn hàng chưa đủ điều kiện tối thiểu để dùng voucher này.';
                payForm2.insertBefore(p2, payForm2.firstChild);
            }
        }

        document.getElementById('priceDiscount').textContent = formatNumber(Math.round(priceDiscount)) + ' đ';
        document.getElementById('priceFinal').textContent = formatNumber(Math.round(priceFinal)) + ' đ';
        document.getElementById('invoiceTotal').value = priceFinal;
    }

    function checkVoucherEligibility() {
        var voucherSelect = document.getElementById('voucherSelect');
        var originalTotal = parseFloat(document.getElementById('invoiceOriginal').value) || 0;
        var itemMax = parseFloat(document.getElementById('invoiceItemMax').value) || 0;
        var payForm = document.getElementById('payForm');
        var resultBox = document.createElement('p');
        resultBox.className = 'notice success';

        var options = Array.from(voucherSelect.options);
        var selectedValue = voucherSelect.value;
        var eligibleOptions = [];

        options.forEach(function (option) {
            if (!option.value) {
                return;
            }
            var minValue = parseFloat(option.getAttribute('data-min')) || 0;
            if (originalTotal >= minValue || itemMax >= minValue) {
                eligibleOptions.push(option);
            }
        });

        if (eligibleOptions.length === 0) {
            resultBox.className = 'notice error';
            resultBox.textContent = 'Hiện chưa có voucher nào phù hợp với tổng đơn hoặc món lớn nhất.';
            removeExistingMessage();
            payForm.insertBefore(resultBox, payForm.firstChild);
            return;
        }

        var newOptions = [];
        newOptions.push(voucherSelect.options[0]);
        eligibleOptions.forEach(function (option) {
            newOptions.push(option);
        });

        voucherSelect.innerHTML = '';
        newOptions.forEach(function (option) {
            voucherSelect.appendChild(option.cloneNode(true));
        });

        if (selectedValue && voucherSelect.querySelector('option[value="' + selectedValue + '"]')) {
            voucherSelect.value = selectedValue;
        } else {
            voucherSelect.value = '';
        }

        resultBox.textContent = 'Đã lọc được ' + eligibleOptions.length + ' voucher phù hợp.';
        removeExistingMessage();
        payForm.insertBefore(resultBox, payForm.firstChild);
        calculatePrice();
    }

    function removeExistingMessage() {
        var existing = document.querySelector('.pay-form .notice');
        if (existing) {
            existing.remove();
        }
    }

    function formatNumber(num) {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    // Client-side validation for payment form
    (function () {
        var payForm = document.getElementById('payForm');
        if (!payForm) return;
        payForm.addEventListener('submit', function (e) {
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
                    errorMsg = 'Số tiền khách đưa chưa đủ (cần: ' + formatNumber(Math.round(total)) + ' đ).';
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

    // Reload voucher list via AJAX and update the voucher select while preserving other form inputs
    function reloadVouchers() {
        var ctx = '<%= request.getContextPath() %>';
        var voucherSelect = document.getElementById('voucherSelect');
        if (!voucherSelect) return;
        var selected = voucherSelect.value;
        fetch(ctx + '/vouchers', { credentials: 'same-origin' })
            .then(function (res) { if (!res.ok) throw new Error('network'); return res.json(); })
            .then(function (data) {
                // data is an array of voucher objects
                // rebuild options
                var firstOption = document.createElement('option');
                firstOption.value = '';
                firstOption.textContent = '-- Không áp dụng voucher --';
                voucherSelect.innerHTML = '';
                voucherSelect.appendChild(firstOption);
                data.forEach(function (v) {
                    var opt = document.createElement('option');
                    opt.value = v.maVoucher;
                    opt.setAttribute('data-loai', v.loai);
                    opt.setAttribute('data-value', v.giaTri);
                    opt.setAttribute('data-min', v.min);
                    opt.setAttribute('data-label', v.tenVoucher);
                    opt.setAttribute('data-active', (!!v.active).toString());
                    opt.setAttribute('data-remaining', v.remaining);
                    opt.setAttribute('data-expired', (!!v.expired).toString());
                    opt.setAttribute('data-notstarted', (!!v.notStarted).toString());
                    opt.textContent = v.tenVoucher + (v.active ? '' : ' (Không hoạt động)');
                    voucherSelect.appendChild(opt);
                });
                // restore selection if still present
                if (selected && voucherSelect.querySelector('option[value="' + selected + '"]')) {
                    voucherSelect.value = selected;
                } else {
                    voucherSelect.value = '';
                }
                // recalc UI
                calculatePrice();
                showTempMessage('Đã cập nhật danh sách voucher', 'success');
            })
            .catch(function (err) {
                console.debug('Failed to reload vouchers', err);
                showTempMessage('Không thể nạp voucher lúc này', 'error');
            });
    }

    function showTempMessage(msg, cls) {
        var payForm = document.getElementById('payForm');
        if (!payForm) return;
        var p = document.createElement('p');
        p.className = 'notice ' + (cls || 'success') + ' client-info';
        p.textContent = msg;
        payForm.insertBefore(p, payForm.firstChild);
        setTimeout(function () { if (p && p.parentNode) p.parentNode.removeChild(p); }, 3000);
    }

    // Tự động điền thông tin khách hàng khi nhân viên nhập số điện thoại.
    (function () {
        var ctx = '<%= request.getContextPath() %>';
        var phoneEl = document.getElementById('customerPhone');
        var nameEl = document.getElementById('customerName');
        if (!phoneEl || !nameEl) return;
        var timer = null;

        function showTempMessage(msg, cls) {
            // reuse removeExistingMessage to avoid clutter
            removeExistingMessage();
            var p = document.createElement('p');
            p.className = 'notice ' + (cls || 'success') + ' client-info';
            p.textContent = msg;
            var payForm = document.getElementById('payForm');
            if (payForm) payForm.insertBefore(p, payForm.firstChild);
            setTimeout(function () { if (p && p.parentNode) p.parentNode.removeChild(p); }, 4000);
        }

        function lookup(phone) {
            fetch(ctx + '/khachhang?phone=' + encodeURIComponent(phone), { credentials: 'same-origin' })
                .then(function (res) {
                    if (!res.ok) throw new Error('Network response was not ok');
                    return res.json();
                })
                .then(function (data) {
                    if (data && data.found) {
                        nameEl.value = data.tenKH || '';
                        showTempMessage('Đã nạp thông tin khách cũ: ' + (data.tenKH || ''), 'success');
                    } else {
                        showTempMessage('Không tìm thấy khách hàng, vui lòng nhập thông tin mới nếu cần', 'info');
                    }
                })
                .catch(function (err) {
                    // silent fail — network or server error
                    console.debug('Lookup error', err);
                });
        }

        function scheduleLookup() {
            var phone = phoneEl.value.trim();
            if (/^\d{10,11}$/.test(phone)) {
                if (timer) clearTimeout(timer);
                timer = setTimeout(function () { lookup(phone); }, 450);
            }
        }

        phoneEl.addEventListener('blur', scheduleLookup);
        phoneEl.addEventListener('keyup', scheduleLookup);
    })();
</script>
</body>
</html>
