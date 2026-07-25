package com.example.Controller;

import com.example.Dao.HoaDonDAO;
import com.example.Dao.KhachHangDAO;
import com.example.Dao.BanDAO;
import com.example.Dao.ThucDonDAO;
import com.example.Dao.ChiTietHoaDonDAO;
import com.example.Dao.NguoiDungDAO;
import com.example.Dao.VoucherDAO;
import com.example.Entity.Ban;
import com.example.Entity.HoaDon;
import com.example.Entity.NguoiDung;
import com.example.Entity.Voucher;
import com.example.JDBC.DBConnect;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/hoadon")
public class HoaDonController extends HttpServlet {
    private HoaDonDAO hoaDonDao;
    private KhachHangDAO khachHangDao;
    private BanDAO banDao;
    private ThucDonDAO thucDonDao;
    private ChiTietHoaDonDAO ctDao;
    private NguoiDungDAO nguoiDungDao;
    private VoucherDAO voucherDao;
    private Connection conn;

    @Override
    public void init() {
        conn = DBConnect.getConnection();
        hoaDonDao = new HoaDonDAO(conn);
        khachHangDao = new KhachHangDAO(conn);
        banDao = new BanDAO(conn);
        thucDonDao = new ThucDonDAO(conn);
        ctDao = new ChiTietHoaDonDAO(conn);
        voucherDao = new VoucherDAO(conn);
        nguoiDungDao = new NguoiDungDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "list":
                    // server-side pagination: đọc param page và size
                    int page = 1;
                    int pageSize = 10; // mặc định 10, có thể đổi lên 15
                    String pageParam = req.getParameter("page");
                    String sizeParam = req.getParameter("size");
                    String sortParam = req.getParameter("sort"); // ASC hoặc DESC
                    if (pageParam != null) {
                        try { page = Integer.parseInt(pageParam); if (page < 1) page = 1; } catch (NumberFormatException ignored) {}
                    }
                    if (sizeParam != null) {
                        try { pageSize = Integer.parseInt(sizeParam); if (pageSize < 1) pageSize = 10; } catch (NumberFormatException ignored) {}
                    }
                    if (sortParam == null) {
                        sortParam = "DESC"; // Mặc định sắp xếp mới nhất trước
                    }

                    int totalHD = hoaDonDao.countAll();
                    int totalPages = (int) Math.ceil((double) totalHD / pageSize);
                    if (page > totalPages && totalPages > 0) page = totalPages;
                    int offset = (page - 1) * pageSize;

                    req.setAttribute("listHD", hoaDonDao.getPage(offset, pageSize, sortParam));
                    req.setAttribute("sort", sortParam); // Lưu sort direction cho JSP

                    // Load danh sách voucher có thể sử dụng
                    req.setAttribute("listVoucher", voucherDao.getAvailable(LocalDateTime.now()));

                    // pagination for Ban shown in dashboard (banPage / banSize)
                    int banPage = 1;
                    int banPageSize = 10;
                    String banPageParam = req.getParameter("banPage");
                    String banSizeParam = req.getParameter("banSize");
                    if (banPageParam != null) {
                        try { banPage = Integer.parseInt(banPageParam); if (banPage < 1) banPage = 1; } catch (NumberFormatException ignored) {}
                    }
                    if (banSizeParam != null) {
                        try { banPageSize = Integer.parseInt(banSizeParam); if (banPageSize < 1) banPageSize = 10; } catch (NumberFormatException ignored) {}
                    }
                    int banTotal = banDao.countAll();
                    int banTotalPages = (int) Math.ceil((double) banTotal / banPageSize);
                    if (banPage > banTotalPages && banTotalPages > 0) banPage = banTotalPages;
                    int banOffset = (banPage - 1) * banPageSize;
                    req.setAttribute("listBan", banDao.getPage(banOffset, banPageSize));
                    req.setAttribute("banTotal", banTotal);
                    req.setAttribute("banTotalPages", banTotalPages);
                    req.setAttribute("banCurrentPage", banPage);
                    req.setAttribute("banPageSize", banPageSize);

                    // pagination for ThucDon shown in index (menu inside dashboard)
                    int monPage = 1;
                    int monPageSize = 10;
                    String monPageParam = req.getParameter("monPage");
                    String monSizeParam = req.getParameter("monSize");
                    if (monPageParam != null) {
                        try { monPage = Integer.parseInt(monPageParam); if (monPage < 1) monPage = 1; } catch (NumberFormatException ignored) {}
                    }
                    if (monSizeParam != null) {
                        try { monPageSize = Integer.parseInt(monSizeParam); if (monPageSize < 1) monPageSize = 10; } catch (NumberFormatException ignored) {}
                    }
                    int monTotal = thucDonDao.countAll();
                    int monTotalPages = (int) Math.ceil((double) monTotal / monPageSize);
                    if (monPage > monTotalPages && monTotalPages > 0) monPage = monTotalPages;
                    int monOffset = (monPage - 1) * monPageSize;
                    req.setAttribute("listMon", thucDonDao.getPage(monOffset, monPageSize));
                    req.setAttribute("monTotal", monTotal);
                    req.setAttribute("monTotalPages", monTotalPages);
                    req.setAttribute("monCurrentPage", monPage);
                    req.setAttribute("monPageSize", monPageSize);

                    req.setAttribute("totalHD", totalHD);
                    req.setAttribute("totalPages", totalPages);
                    req.setAttribute("currentPage", page);
                    req.setAttribute("pageSize", pageSize);

                    RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                    rd.forward(req, resp);
                    break;

                case "delete":
                    String maHD = req.getParameter("maHD");
                    hoaDonDao.delete(maHD);
                    resp.sendRedirect("hoadon?action=list");
                    break;

                case "edit":
                    String maHDEdit = req.getParameter("maHD");
                    HoaDon hd = hoaDonDao.findById(maHDEdit);
                    req.setAttribute("hoadon", hd);
                    RequestDispatcher rdEdit = req.getRequestDispatcher("editHoaDon.jsp");
                    rdEdit.forward(req, resp);
                    break;

                case "open": {
                    // Open an invoice for editing if it's still being served
                    String maHDOpen = req.getParameter("maHD");
                    HoaDon hdOpen = hoaDonDao.findById(maHDOpen);

                    if (hdOpen != null && "Đang phục vụ".equals(hdOpen.getTrangThai())) {
                        // Handle menu pagination
                        int openMonPage = 1;
                        int openMonPageSize = 10;
                        String openMonPageParam = req.getParameter("monPage");
                        String openMonSizeParam = req.getParameter("monSize");
                        if (openMonPageParam != null) {
                            try { openMonPage = Integer.parseInt(openMonPageParam); if (openMonPage < 1) openMonPage = 1; } catch (NumberFormatException ignored) {}
                        }
                        if (openMonSizeParam != null) {
                            try { openMonPageSize = Integer.parseInt(openMonSizeParam); if (openMonPageSize < 1) openMonPageSize = 10; } catch (NumberFormatException ignored) {}
                        }
                        int openMonTotal = thucDonDao.countAll();
                        int openMonTotalPages = (int) Math.ceil((double) openMonTotal / openMonPageSize);
                        if (openMonPage > openMonTotalPages && openMonTotalPages > 0) openMonPage = openMonTotalPages;
                        int openMonOffset = (openMonPage - 1) * openMonPageSize;

                        // Handle ban pagination
                        int openBanPage = 1;
                        int openBanPageSize = 10;
                        String openBanPageParam = req.getParameter("banPage");
                        String openBanSizeParam = req.getParameter("banSize");
                        if (openBanPageParam != null) {
                            try { openBanPage = Integer.parseInt(openBanPageParam); if (openBanPage < 1) openBanPage = 1; } catch (NumberFormatException ignored) {}
                        }
                        if (openBanSizeParam != null) {
                            try { openBanPageSize = Integer.parseInt(openBanSizeParam); if (openBanPageSize < 1) openBanPageSize = 10; } catch (NumberFormatException ignored) {}
                        }
                        int openBanTotal = banDao.countAll();
                        int openBanTotalPages = (int) Math.ceil((double) openBanTotal / openBanPageSize);
                        if (openBanPage > openBanTotalPages && openBanTotalPages > 0) openBanPage = openBanTotalPages;
                        int openBanOffset = (openBanPage - 1) * openBanPageSize;

                        // Load all necessary data for editing
                        req.setAttribute("currentHD", hdOpen);
                        req.setAttribute("listBan", banDao.getPage(openBanOffset, openBanPageSize));
                        req.setAttribute("listMon", thucDonDao.getPage(openMonOffset, openMonPageSize));
                        req.setAttribute("listHD", hoaDonDao.getPage(0, 10, "DESC"));
                        req.setAttribute("listCTHD", ctDao.findByHoaDon(maHDOpen));
                        req.setAttribute("listVoucher", voucherDao.getAvailable(LocalDateTime.now()));

                        // Set pagination info for menu
                        req.setAttribute("monTotal", openMonTotal);
                        req.setAttribute("monTotalPages", openMonTotalPages);
                        req.setAttribute("monCurrentPage", openMonPage);
                        req.setAttribute("monPageSize", openMonPageSize);

                        // Set pagination info for ban
                        req.setAttribute("banTotal", openBanTotal);
                        req.setAttribute("banTotalPages", openBanTotalPages);
                        req.setAttribute("banCurrentPage", openBanPage);
                        req.setAttribute("banPageSize", openBanPageSize);

                        RequestDispatcher rdOpen = req.getRequestDispatcher("index.jsp");
                        rdOpen.forward(req, resp);
                    } else {
                        // If invoice is not being served or not found, redirect to list
                        resp.sendRedirect("hoadon?action=list");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("insert".equals(action)) {
                String maHD = req.getParameter("maHD");
                LocalDateTime ngayLap = LocalDateTime.parse(req.getParameter("ngayLap"));
                double tongTien = Double.parseDouble(req.getParameter("tongTien"));
                String trangThai = req.getParameter("trangThai");
                String maBan = req.getParameter("maBan");
                String maND = req.getParameter("maND");

                HoaDon hd = new HoaDon(maHD, ngayLap, tongTien, trangThai, maBan, maND);
                hoaDonDao.insert(hd);
                resp.sendRedirect("hoadon?action=list");

            } else if ("update".equals(action)) {
                String maBan = req.getParameter("maBan");
                int soGhe = Integer.parseInt(req.getParameter("soGhe"));
                boolean tinhTrang = "1".equals(req.getParameter("tinhTrang"));

                // Cập nhật trạng thái bàn
                Ban b = new Ban(maBan, soGhe, tinhTrang);
                banDao.update(b);

                // Lấy currentUser từ session
                HttpSession session = req.getSession();
                NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
                if (currentUser == null) {
                    resp.sendRedirect("login.jsp");
                    return;
                }

                // Tạo hóa đơn mới cho bàn này
                String maHD = "HD" + System.currentTimeMillis();
                HoaDon hd = new HoaDon(maHD, LocalDateTime.now(), 0.0, "Đang phục vụ", maBan, currentUser.getMaND());
                hoaDonDao.insert(hd);

                // Gắn dữ liệu vào request để JSP hiển thị lại tất cả bảng
                req.setAttribute("currentHD", hd);
                req.setAttribute("listBan", banDao.getAll());
                req.setAttribute("listMon", thucDonDao.getAll());
                req.setAttribute("listHD", hoaDonDao.getAll());
                req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD)); // ban đầu rỗng

                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req, resp);
            } else if ("pay".equals(action)) {
                String maHD = req.getParameter("maHD");
                String customerName = req.getParameter("customerName");
                String customerPhone = req.getParameter("customerPhone");
                String maVoucher = req.getParameter("maVoucher");
                String selectedVoucherCode = maVoucher != null ? maVoucher.trim() : "";
                String errorMessage = null;

                // Validate customer name (bắt buộc, không để trống)
                if (customerName == null || customerName.trim().isEmpty()) {
                    errorMessage = "Tên khách hàng không được để trống!";
                } else {
                    customerName = customerName.trim();
                }

                if (errorMessage == null) {
                    // Validate phone (optional, nhưng nếu nhập thì phải hợp lệ: 10-11 chữ số)
                    if (customerPhone != null && !customerPhone.trim().isEmpty()) {
                        customerPhone = customerPhone.trim();
                        if (!customerPhone.matches("^[0-9]{10,11}$")) {
                            errorMessage = "Số điện thoại không hợp lệ (phải là 10-11 chữ số)!";
                        }
                    }
                }

                double tienKhachDua = 0;
                if (errorMessage == null) {
                    // Validate tiền khách đưa
                    try {
                        tienKhachDua = Double.parseDouble(req.getParameter("tienKhachDua"));
                        if (tienKhachDua <= 0) {
                            errorMessage = "Tiền khách đưa phải lớn hơn 0!";
                        }
                    } catch (NumberFormatException e) {
                        errorMessage = "Tiền khách đưa phải là số hợp lệ!";
                    }
                } else {
                    tienKhachDua = 0;
                }

                HoaDon hd = hoaDonDao.findById(maHD);
                double orderTotal = ctDao.tinhTongTien(maHD);
                hd.setTongTien(orderTotal);
                hd.setMaVoucher(null);

                double finalAmount = orderTotal;
                double discountAmount = 0.0;
                Voucher selectedVoucher = null;
                if (errorMessage == null && !selectedVoucherCode.isEmpty()) {
                    selectedVoucher = voucherDao.findById(selectedVoucherCode);
                    if (selectedVoucher == null) {
                        errorMessage = "Voucher không tồn tại!";
                    } else if (!selectedVoucher.isConThe(LocalDateTime.now())) {
                        errorMessage = "Voucher không còn hiệu lực!";
                    } else if (selectedVoucher.getDonGiaTuoiNhap() > orderTotal) {
                        errorMessage = "Đơn hàng chưa đủ giá trị tối thiểu để dùng voucher này.";
                    } else {
                        if ("TienMat".equals(selectedVoucher.getLoaiGiamGia())) {
                            discountAmount = selectedVoucher.getGiaTriGiamGia();
                        } else {
                            discountAmount = orderTotal * selectedVoucher.getGiaTriGiamGia() / 100.0;
                        }
                        if (discountAmount > orderTotal) {
                            discountAmount = orderTotal;
                        }
                        finalAmount = orderTotal - discountAmount;
                        hd.setMaVoucher(selectedVoucherCode);
                    }
                }

                if (errorMessage != null) {
                    req.setAttribute("error", errorMessage);
                    req.setAttribute("currentHD", hd);
                    req.setAttribute("orderTotal", orderTotal);
                    req.setAttribute("selectedVoucherCode", selectedVoucherCode);
                    req.setAttribute("listBan", banDao.getPage(0, 10));
                    req.setAttribute("listMon", thucDonDao.getPage(0, 10));
                    req.setAttribute("listHD", hoaDonDao.getPage(0, 10, "DESC"));
                    req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));
                    req.setAttribute("listVoucher", voucherDao.getAvailable(LocalDateTime.now()));
                    RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                    rd.forward(req, resp);
                    return;
                }

                if (tienKhachDua >= finalAmount) {
                    hd.setTongTien(finalAmount);
                    hd.setTrangThai("Đã thanh toán");
                    if (customerName != null && !customerName.trim().isEmpty()) {
                        String maKH = "KH" + System.currentTimeMillis();
                        com.example.Entity.KhachHang kh = new com.example.Entity.KhachHang(maKH, customerName, customerPhone);
                        khachHangDao.insert(kh);
                        hd.setMaKH(maKH);
                    }
                    hoaDonDao.update(hd);

                    if (selectedVoucher != null) {
                        voucherDao.decrementUsage(selectedVoucher.getMaVoucher());
                    }

                    Ban b = banDao.findById(hd.getMaBan());
                    b.setTinhTrang(false);
                    banDao.update(b);

                    double tienThua = tienKhachDua - finalAmount;
                    hd.setTienThua(tienThua);
                    hoaDonDao.update(hd);

                    req.setAttribute("receiptCustomerName", customerName != null ? customerName : "");
                    req.setAttribute("receiptCustomerPhone", customerPhone != null ? customerPhone : "");
                    req.setAttribute("receiptMaHD", hd.getMaHD());
                    req.setAttribute("receiptTongTien", finalAmount);
                    req.setAttribute("receiptTienThua", tienThua);
                    req.setAttribute("currentHD", hd);
                } else {
                    req.setAttribute("error", "Khách đưa chưa đủ tiền!");
                    req.setAttribute("currentHD", hd);
                }

                req.setAttribute("orderTotal", orderTotal);
                req.setAttribute("selectedVoucherCode", selectedVoucherCode);
                req.setAttribute("listBan", banDao.getPage(0, 10));
                req.setAttribute("listMon", thucDonDao.getPage(0, 10));
                req.setAttribute("listHD", hoaDonDao.getPage(0, 10, "DESC"));
                req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));
                req.setAttribute("listVoucher", voucherDao.getAvailable(LocalDateTime.now()));

                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req, resp);
            }
            else if ("cancel".equals(action)) {
                String maHD = req.getParameter("maHD");

                HoaDon hd = hoaDonDao.findById(maHD);
                if (hd != null && !"Đã thanh toán".equals(hd.getTrangThai())) {
                    // Cập nhật trạng thái hóa đơn thành "Đã hủy"
                    hd.setTrangThai("Đã hủy");
                    hoaDonDao.update(hd);

                    // Giải phóng bàn
                    Ban b = banDao.findById(hd.getMaBan());
                    b.setTinhTrang(false);
                    banDao.update(b);
                }

                // Nạp lại dữ liệu
                req.setAttribute("listBan", banDao.getAll());
                req.setAttribute("listMon", thucDonDao.getAll());
                req.setAttribute("listHD", hoaDonDao.getAll());
                req.setAttribute("listCTHD", null);
                req.setAttribute("currentHD", null);

                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req, resp);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
