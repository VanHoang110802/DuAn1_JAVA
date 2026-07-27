package com.example.Controller;

import com.example.Dao.ChiTietHoaDonDAO;
import com.example.Dao.HoaDonDAO;
import com.example.Dao.BanDAO;
import com.example.Dao.ThucDonDAO;
import com.example.Entity.ChiTietHoaDon;
import com.example.Entity.HoaDon;
import com.example.Entity.ThucDon;
import com.example.JDBC.DBConnect;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/chitiethoadon")
public class ChiTietHoaDonController extends HttpServlet {
    private ChiTietHoaDonDAO ctDao;
    private HoaDonDAO hoaDonDao;
    private BanDAO banDao;
    private ThucDonDAO thucDonDao;
    private Connection conn;

    @Override
    public void init() {
        conn = DBConnect.getConnection();
        ctDao = new ChiTietHoaDonDAO(conn);
        hoaDonDao = new HoaDonDAO(conn);
        banDao = new BanDAO(conn);
        thucDonDao = new ThucDonDAO(conn);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("insert".equals(action)) {
                String maHD = req.getParameter("maHD");
                String maItem = req.getParameter("maItem");
                int soLuong = Integer.parseInt(req.getParameter("soLuong"));

                // Lấy giá món
                ThucDon mon = thucDonDao.findById(maItem);
                double donGia = mon.getGia();

                // Kiểm tra xem món đó đã có trong hóa đơn chưa
                if (ctDao.exists(maHD, maItem)) {
                    // Nếu có rồi, lấy chi tiết hiện tại và cộng thêm số lượng
                    List<ChiTietHoaDon> list = ctDao.findByHoaDon(maHD);
                    for (ChiTietHoaDon item : list) {
                        if (item.getMaItem().equals(maItem)) {
                            int soLuongMoi = item.getSoLuong() + soLuong;
                            item.setSoLuong(soLuongMoi);
                            ctDao.update(item);
                            break;
                        }
                    }
                } else {
                    // Nếu chưa có, thì insert mới
                    ChiTietHoaDon ct = new ChiTietHoaDon(maHD, maItem, soLuong, donGia);
                    ctDao.insert(ct);
                }

                // Cập nhật tổng tiền hóa đơn
                double tongTien = ctDao.tinhTongTien(maHD);
                HoaDon hd = hoaDonDao.findById(maHD);
                hd.setTongTien(tongTien);
                hoaDonDao.update(hd);

                // Gắn lại toàn bộ dữ liệu cho JSP
                req.setAttribute("currentHD", hd);

                // Get pagination params if in edit mode
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

                req.setAttribute("listBan", banDao.getPage(0, 10));        // thêm lại danh sách bàn
                req.setAttribute("listMon", thucDonDao.getPage(monOffset, monPageSize));    // thêm lại thực đơn với phân trang
                req.setAttribute("listHD", hoaDonDao.getPage(0, 10, "DESC"));
                req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));

                // Set pagination info
                req.setAttribute("monTotal", monTotal);
                req.setAttribute("monTotalPages", monTotalPages);
                req.setAttribute("monCurrentPage", monPage);
                req.setAttribute("monPageSize", monPageSize);

                // Set ban pagination
                int banTotal = banDao.countAll();
                int banPageSize = 10;
                int banTotalPages = (int) Math.ceil((double) banTotal / banPageSize);
                req.setAttribute("banTotal", banTotal);
                req.setAttribute("banTotalPages", banTotalPages);
                req.setAttribute("banCurrentPage", 1);
                req.setAttribute("banPageSize", banPageSize);

                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req, resp);
            }else if ("update".equals(action)) {
                String maHD = req.getParameter("maHD");
                String maItem = req.getParameter("maItem");
                int soLuong = Integer.parseInt(req.getParameter("soLuong"));

                // Lấy giá món từ thực đơn
                ThucDon mon = thucDonDao.findById(maItem);
                double donGia = mon.getGia();

                // Cập nhật chi tiết hóa đơn
                ChiTietHoaDon ct = new ChiTietHoaDon(maHD, maItem, soLuong, donGia);
                ctDao.update(ct);

                // Cập nhật tổng tiền hóa đơn
                double tongTien = ctDao.tinhTongTien(maHD);
                HoaDon hd = hoaDonDao.findById(maHD);
                hd.setTongTien(tongTien);
                hoaDonDao.update(hd);

                // Nạp lại dữ liệu cho JSP
                req.setAttribute("currentHD", hd);

                // Get pagination params if in edit mode
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

                req.setAttribute("listBan", banDao.getPage(0, 10));
                req.setAttribute("listMon", thucDonDao.getPage(monOffset, monPageSize));
                req.setAttribute("listHD", hoaDonDao.getPage(0, 10, "DESC"));
                req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));

                // Set pagination info
                req.setAttribute("monTotal", monTotal);
                req.setAttribute("monTotalPages", monTotalPages);
                req.setAttribute("monCurrentPage", monPage);
                req.setAttribute("monPageSize", monPageSize);

                // Set ban pagination
                int banTotal = banDao.countAll();
                int banPageSize = 10;
                int banTotalPages = (int) Math.ceil((double) banTotal / banPageSize);
                req.setAttribute("banTotal", banTotal);
                req.setAttribute("banTotalPages", banTotalPages);
                req.setAttribute("banCurrentPage", 1);
                req.setAttribute("banPageSize", banPageSize);

                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req, resp);
            }else if ("delete".equals(action)) {
                String maHD = req.getParameter("maHD");
                String maItem = req.getParameter("maItem");

                ctDao.delete(maHD, maItem);

                HoaDon hd = hoaDonDao.findById(maHD);
                hd.setTongTien(ctDao.tinhTongTien(maHD));
                hoaDonDao.update(hd);

                req.setAttribute("currentHD", hd);

                // Get pagination params if in edit mode
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

                req.setAttribute("listBan", banDao.getPage(0, 10));
                req.setAttribute("listMon", thucDonDao.getPage(monOffset, monPageSize));
                req.setAttribute("listHD", hoaDonDao.getPage(0, 10, "DESC"));
                req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));

                // Set pagination info
                req.setAttribute("monTotal", monTotal);
                req.setAttribute("monTotalPages", monTotalPages);
                req.setAttribute("monCurrentPage", monPage);
                req.setAttribute("monPageSize", monPageSize);

                // Set ban pagination
                int banTotal = banDao.countAll();
                int banPageSize = 10;
                int banTotalPages = (int) Math.ceil((double) banTotal / banPageSize);
                req.setAttribute("banTotal", banTotal);
                req.setAttribute("banTotalPages", banTotalPages);
                req.setAttribute("banCurrentPage", 1);
                req.setAttribute("banPageSize", banPageSize);

                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req, resp);
            }


        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "list":
                    String maHD = req.getParameter("maHD");
                    HoaDon currentHD = hoaDonDao.findById(maHD);

                    // pagination for chi tiet hoa don
                    int page = 1;
                    int pageSize = 10;
                    String pageParam = req.getParameter("page");
                    String sizeParam = req.getParameter("size");
                    if (pageParam != null) {
                        try { page = Integer.parseInt(pageParam); if (page < 1) page = 1; } catch (NumberFormatException ignored) {}
                    }
                    if (sizeParam != null) {
                        try { pageSize = Integer.parseInt(sizeParam); if (pageSize < 1) pageSize = 10; } catch (NumberFormatException ignored) {}
                    }
                    int total = ctDao.countByHoaDon(maHD);
                    int totalPages = (int) Math.ceil((double) total / pageSize);
                    if (page > totalPages && totalPages > 0) page = totalPages;
                    int offset = (page - 1) * pageSize;

                    req.setAttribute("currentHD", currentHD);
                    req.setAttribute("listCTHD", ctDao.getPageByHoaDon(maHD, offset, pageSize));
                    req.setAttribute("listHD", hoaDonDao.getPage(0, 10, "DESC"));
                    req.setAttribute("listBan", banDao.getPage(0, 10));
                    req.setAttribute("listMon", thucDonDao.getPage(0, 10));
                    req.setAttribute("ctTotal", total);
                    req.setAttribute("ctTotalPages", totalPages);
                    req.setAttribute("ctCurrentPage", page);
                    req.setAttribute("ctPageSize", pageSize);
                    // When viewing from the invoice list, show a read-only view: no edit/delete actions or cancel
                    req.setAttribute("viewOnly", true);

                    // Set pagination info for ban and menu
                    int banTotal = banDao.countAll();
                    int banPageSize = 10;
                    int banTotalPages = (int) Math.ceil((double) banTotal / banPageSize);
                    req.setAttribute("banTotal", banTotal);
                    req.setAttribute("banTotalPages", banTotalPages);
                    req.setAttribute("banCurrentPage", 1);
                    req.setAttribute("banPageSize", banPageSize);

                    int monTotal = thucDonDao.countAll();
                    int monPageSize = 10;
                    int monTotalPages = (int) Math.ceil((double) monTotal / monPageSize);
                    req.setAttribute("monTotal", monTotal);
                    req.setAttribute("monTotalPages", monTotalPages);
                    req.setAttribute("monCurrentPage", 1);
                    req.setAttribute("monPageSize", monPageSize);

                    // If the invoice links to a customer (MaKH), load and expose basic customer info for display
                    if (currentHD != null && currentHD.getMaKH() != null) {
                        com.example.Dao.KhachHangDAO khDao = new com.example.Dao.KhachHangDAO(conn);
                        com.example.Entity.KhachHang kh = khDao.findById(currentHD.getMaKH());
                        if (kh != null) {
                            req.setAttribute("receiptCustomerName", kh.getTenKH());
                            req.setAttribute("receiptCustomerPhone", kh.getSoDienThoai());
                        }
                    }

                    RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                    rd.forward(req, resp);
                    break;

                case "delete":
                    String maHDDel = req.getParameter("maHD");
                    String maItemDel = req.getParameter("maItem");
                    ctDao.delete(maHDDel, maItemDel);

                    // Sau khi xóa, nạp lại dữ liệu
                    req.setAttribute("currentHD", hoaDonDao.findById(maHDDel));
                    req.setAttribute("listBan", banDao.getAll());
                    req.setAttribute("listMon", thucDonDao.getAll());
                    req.setAttribute("listHD", hoaDonDao.getAll());
                    req.setAttribute("listCTHD", ctDao.findByHoaDon(maHDDel));

                    RequestDispatcher rdDel = req.getRequestDispatcher("index.jsp");
                    rdDel.forward(req, resp);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
