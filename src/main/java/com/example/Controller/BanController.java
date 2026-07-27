package com.example.Controller;

import com.example.Dao.BanDAO;
import com.example.Dao.ChiTietHoaDonDAO;
import com.example.Dao.HoaDonDAO;
import com.example.Dao.ThucDonDAO;
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
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/ban")
public class BanController extends HttpServlet {
    private BanDAO banDao;
    private HoaDonDAO hoaDonDao;
    private ThucDonDAO thucDonDao;
    private ChiTietHoaDonDAO ctDao;
    private com.example.Dao.VoucherDAO voucherDao;

    @Override
    public void init() {
        Connection conn = DBConnect.getConnection();
        banDao = new BanDAO(conn);
        hoaDonDao = new HoaDonDAO(conn);
        thucDonDao = new ThucDonDAO(conn);
        ctDao = new ChiTietHoaDonDAO(conn);
        voucherDao = new com.example.Dao.VoucherDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            HttpSession session = req.getSession();
            com.example.Entity.NguoiDung currentUser = (com.example.Entity.NguoiDung) session.getAttribute("currentUser");

            switch (action) {
                case "list":
                    if (currentUser == null) {
                        resp.sendRedirect("login.jsp");
                        return;
                    }

                    // pagination for Ban
                    int page = 1;
                    int pageSize = 10;
                    // support both 'page'/'size' (used by quanlyBan.jsp) and
                    // 'banPage'/'banSize' (used by index.jsp dashboard links)
                    String pageParam = req.getParameter("page");
                    String sizeParam = req.getParameter("size");
                    String altPageParam = req.getParameter("banPage");
                    String altSizeParam = req.getParameter("banSize");
                    if (pageParam == null && altPageParam != null) pageParam = altPageParam;
                    if (sizeParam == null && altSizeParam != null) sizeParam = altSizeParam;
                    if (pageParam != null) {
                        try { page = Integer.parseInt(pageParam); if (page < 1) page = 1; } catch (NumberFormatException ignored) {}
                    }
                    if (sizeParam != null) {
                        try { pageSize = Integer.parseInt(sizeParam); if (pageSize < 1) pageSize = 10; } catch (NumberFormatException ignored) {}
                    }
                    int total = banDao.countAll();
                    int totalPages = (int) Math.ceil((double) total / pageSize);
                    if (page > totalPages && totalPages > 0) page = totalPages;
                    int offset = (page - 1) * pageSize;

                    req.setAttribute("listBan", banDao.getPage(offset, pageSize));
                    req.setAttribute("banTotal", total);
                    req.setAttribute("banTotalPages", totalPages);
                    req.setAttribute("banCurrentPage", page);
                    req.setAttribute("banPageSize", pageSize);

                    if (currentUser != null && "QuanLy".equals(currentUser.getVaiTro())) {
                        // Quản lý → về trang quản lý bàn
                        RequestDispatcher rd = req.getRequestDispatcher("quanlyBan.jsp");
                        rd.forward(req, resp);
                    } else {
                        // Nhân viên → về index
                        RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                        rd.forward(req, resp);
                    }
                    break;

                case "delete":
                    if (currentUser == null) {
                        resp.sendRedirect("login.jsp");
                        return;
                    }
                    if (!"QuanLy".equals(currentUser.getVaiTro())) {
                        resp.sendRedirect("index.jsp");
                        return;
                    }
                    banDao.delete(req.getParameter("maBan"));
                    resp.sendRedirect("ban?action=list");
                    break;

                case "edit":
                    if (currentUser == null) {
                        resp.sendRedirect("login.jsp");
                        return;
                    }
                    if (!"QuanLy".equals(currentUser.getVaiTro())) {
                        resp.sendRedirect("index.jsp");
                        return;
                    }
                    Ban ban = banDao.findById(req.getParameter("maBan"));
                    req.setAttribute("ban", ban);
                    RequestDispatcher rdEdit = req.getRequestDispatcher("editBan.jsp");
                    rdEdit.forward(req, resp);
                    break;

                default:
                    resp.sendRedirect("ban?action=list");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");

        if (currentUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            if ("insert".equals(action) || "update".equals(action)) {
                // Chỉ quản lý mới được CRUD bàn
                if (!"QuanLy".equals(currentUser.getVaiTro())) {
                    resp.sendRedirect("index.jsp");
                    return;
                }
                Ban ban = new Ban(req.getParameter("maBan"),
                        Integer.parseInt(req.getParameter("soGhe")),
                        Boolean.parseBoolean(req.getParameter("tinhTrang")));
                if ("insert".equals(action)) {
                    banDao.insert(ban);
                } else {
                    banDao.update(ban);
                }
                resp.sendRedirect("ban?action=list");
            } else if ("datban".equals(action)) {
                String maBan = req.getParameter("maBan");
                Ban b = banDao.findById(maBan);
                if (b != null && !b.isTinhTrang()) {
                    b.setTinhTrang(true);
                    banDao.update(b);

                    // Tạo hóa đơn mới
                    String maHD = "HD" + System.currentTimeMillis();
                    HoaDon hd = new HoaDon(maHD, LocalDateTime.now(),
                            0.0, "Đang phục vụ", maBan, currentUser.getMaND());
                    hoaDonDao.insert(hd);

                    // Gắn dữ liệu để hiển thị lại
                    // Bàn: trả trang đầu (10 mục) để dashboard ngắn gọn
                    int banPageSize = 10;
                    int banTotal = banDao.countAll();
                    int banTotalPages = (int) Math.ceil((double) banTotal / banPageSize);
                    req.setAttribute("listBan", banDao.getPage(0, banPageSize));
                    req.setAttribute("banTotal", banTotal);
                    req.setAttribute("banTotalPages", banTotalPages);
                    req.setAttribute("banCurrentPage", 1);
                    req.setAttribute("banPageSize", banPageSize);

                    // Thực đơn: phân trang trang đầu, pageSize = 10
                    int monPage = 1; // default page for menu on dashboard
                    int monPageSize = 10;
                    String monSearch = req.getParameter("monSearch");
                    int monTotal;
                    if (monSearch != null && !monSearch.trim().isEmpty()) {
                        monTotal = thucDonDao.countSearch(monSearch.trim());
                    } else {
                        monTotal = thucDonDao.countAll();
                    }
                    int monTotalPages = (int) Math.ceil((double) monTotal / monPageSize);
                    if (monPage > monTotalPages && monTotalPages > 0) monPage = monTotalPages; // ensure current page is within range
                    if (monSearch != null && !monSearch.trim().isEmpty()) {
                        req.setAttribute("listMon", thucDonDao.getPageSearch(0, monPageSize, monSearch.trim()));
                    } else {
                        req.setAttribute("listMon", thucDonDao.getPage(0, monPageSize));
                    }
                    req.setAttribute("monTotal", monTotal);
                    req.setAttribute("monTotalPages", monTotalPages);
                    req.setAttribute("monCurrentPage", 1);
                    req.setAttribute("monPageSize", monPageSize);
                    req.setAttribute("monSearch", monSearch != null ? monSearch : "");

                    // Hóa đơn: phân trang trang đầu
                    int hdPageSize = 10;
                    int hdTotal = hoaDonDao.countAll();
                    int hdTotalPages = (int) Math.ceil((double) hdTotal / hdPageSize);
                    req.setAttribute("listHD", hoaDonDao.getPage(0, hdPageSize, "DESC"));
                    req.setAttribute("totalHD", hdTotal);
                    req.setAttribute("totalPages", hdTotalPages);
                    req.setAttribute("currentPage", 1);
                    req.setAttribute("pageSize", hdPageSize);

                    req.setAttribute("listCTHD", ctDao.findByHoaDon(maHD));
                    req.setAttribute("currentHD", hd);
                    // Load all vouchers into UI (bao gồm cả quá hạn) cho nhân viên chọn
                    req.setAttribute("listVoucher", voucherDao.getAll());

                    RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                    rd.forward(req, resp);
                } else {
                    resp.sendRedirect("ban?action=list");
                }
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
