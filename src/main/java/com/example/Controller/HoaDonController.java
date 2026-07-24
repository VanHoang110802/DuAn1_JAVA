package com.example.Controller;

import com.example.Dao.HoaDonDAO;
import com.example.Dao.KhachHangDAO;
import com.example.Dao.BanDAO;
import com.example.Dao.ThucDonDAO;
import com.example.Dao.ChiTietHoaDonDAO;
import com.example.Entity.Ban;
import com.example.Entity.HoaDon;
import com.example.Entity.NguoiDung;
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
import java.time.LocalDate;
import java.util.List;

@WebServlet("/hoadon")
public class HoaDonController extends HttpServlet {
    private HoaDonDAO hoaDonDao;
    private KhachHangDAO khachHangDao;
    private BanDAO banDao;
    private ThucDonDAO thucDonDao;
    private ChiTietHoaDonDAO ctDao;
    private Connection conn;

    @Override
    public void init() {
        conn = DBConnect.getConnection();
        hoaDonDao = new HoaDonDAO(conn);
        khachHangDao = new KhachHangDAO(conn);
        banDao = new BanDAO(conn);
        thucDonDao = new ThucDonDAO(conn);
        ctDao = new ChiTietHoaDonDAO(conn);
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
                    if (pageParam != null) {
                        try { page = Integer.parseInt(pageParam); if (page < 1) page = 1; } catch (NumberFormatException ignored) {}
                    }
                    if (sizeParam != null) {
                        try { pageSize = Integer.parseInt(sizeParam); if (pageSize < 1) pageSize = 10; } catch (NumberFormatException ignored) {}
                    }

                    int totalHD = hoaDonDao.countAll();
                    int totalPages = (int) Math.ceil((double) totalHD / pageSize);
                    if (page > totalPages && totalPages > 0) page = totalPages;
                    int offset = (page - 1) * pageSize;

                    req.setAttribute("listHD", hoaDonDao.getPage(offset, pageSize));

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
                LocalDate ngayLap = LocalDate.parse(req.getParameter("ngayLap"));
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
                HoaDon hd = new HoaDon(maHD, LocalDate.now(), 0.0, "Đang phục vụ", maBan, currentUser.getMaND());
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

                // Validate customer name (bắt buộc, không để trống)
                if (customerName == null || customerName.trim().isEmpty()) {
                    req.setAttribute("error", "Tên khách hàng không được để trống!");
                    req.setAttribute("currentHD", hoaDonDao.findById(maHD));
                    req.setAttribute("listBan", banDao.getAll());
                    req.setAttribute("listMon", thucDonDao.getAll());
                    req.setAttribute("listHD", hoaDonDao.getAll());
                    req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));
                    RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                    rd.forward(req, resp);
                    return;
                }
                customerName = customerName.trim();

                // Validate phone (optional, nhưng nếu nhập thì phải hợp lệ: 10-11 chữ số)
                if (customerPhone != null && !customerPhone.trim().isEmpty()) {
                    customerPhone = customerPhone.trim();
                    if (!customerPhone.matches("^[0-9]{10,11}$")) {
                        req.setAttribute("error", "Số điện thoại không hợp lệ (phải là 10-11 chữ số)!");
                        req.setAttribute("currentHD", hoaDonDao.findById(maHD));
                        req.setAttribute("listBan", banDao.getAll());
                        req.setAttribute("listMon", thucDonDao.getAll());
                        req.setAttribute("listHD", hoaDonDao.getAll());
                        req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));
                        RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                        rd.forward(req, resp);
                        return;
                    }
                }

                // Validate tiền khách đưa
                double tienKhachDua;
                try {
                    tienKhachDua = Double.parseDouble(req.getParameter("tienKhachDua"));
                    if (tienKhachDua <= 0) {
                        req.setAttribute("error", "Tiền khách đưa phải lớn hơn 0!");
                        req.setAttribute("currentHD", hoaDonDao.findById(maHD));
                        req.setAttribute("listBan", banDao.getAll());
                        req.setAttribute("listMon", thucDonDao.getAll());
                        req.setAttribute("listHD", hoaDonDao.getAll());
                        req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));
                        RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                        rd.forward(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "Tiền khách đưa phải là số hợp lệ!");
                    req.setAttribute("currentHD", hoaDonDao.findById(maHD));
                    req.setAttribute("listBan", banDao.getAll());
                    req.setAttribute("listMon", thucDonDao.getAll());
                    req.setAttribute("listHD", hoaDonDao.getAll());
                    req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));
                    RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                    rd.forward(req, resp);
                    return;
                }

                HoaDon hd = hoaDonDao.findById(maHD);

                // Tính tổng tiền từ chi tiết
                double tongTien = ctDao.tinhTongTien(maHD);
                hd.setTongTien(tongTien);

                if (tienKhachDua >= tongTien) {
                    // Cập nhật trạng thái hóa đơn
                    hd.setTrangThai("Đã thanh toán");
                    // If customer info provided, create/find KhachHang and associate
                    if (customerName != null && !customerName.trim().isEmpty()) {
                        String maKH = "KH" + System.currentTimeMillis();
                        // Note: KhachHang schema has fields MaKH, TenKH, DiaChi. We store phone into DiaChi if provided.
                        com.example.Entity.KhachHang kh = new com.example.Entity.KhachHang(maKH, customerName, customerPhone);
                        khachHangDao.insert(kh);
                        hd.setMaKH(maKH);
                    }
                    hoaDonDao.update(hd);

                    // Giải phóng bàn
                    Ban b = banDao.findById(hd.getMaBan());
                    b.setTinhTrang(false);
                    banDao.update(b);

                    // Tính tiền thừa
                    double tienThua = tienKhachDua - tongTien;
                    hd.setTienThua(tienThua);   // gán vào hóa đơn
                    hoaDonDao.update(hd);       // lưu lại

                    // Đưa thông tin khách vào request để hiển thị biên lai
                    req.setAttribute("receiptCustomerName", customerName != null ? customerName : "");
                    req.setAttribute("receiptCustomerPhone", customerPhone != null ? customerPhone : "");
                    req.setAttribute("receiptMaHD", hd.getMaHD());
                    req.setAttribute("receiptTongTien", tongTien);
                    req.setAttribute("receiptTienThua", tienThua);

                    // giữ currentHD để JSP có thể hiển thị thông tin hóa đơn đã thanh toán
                    req.setAttribute("currentHD", hd);
                } else {
                    req.setAttribute("error", "Khách đưa chưa đủ tiền!");
                    // giữ currentHD để người dùng có thể sửa lại
                    req.setAttribute("currentHD", hd);
                }

                // Nạp lại dữ liệu cho index.jsp (dùng phân trang mặc định)
                req.setAttribute("listBan", banDao.getPage(0, 10));
                req.setAttribute("listMon", thucDonDao.getPage(0, 10));
                req.setAttribute("listHD", hoaDonDao.getPage(0, 10));
                req.setAttribute("listCTHD", null);

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
