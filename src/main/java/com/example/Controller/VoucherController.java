package com.example.Controller;

import com.example.Dao.VoucherDAO;
import com.example.Entity.Voucher;
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

@WebServlet("/voucher")
public class VoucherController extends HttpServlet {
    private VoucherDAO voucherDao;
    private Connection conn;

    @Override
    public void init() {
        conn = DBConnect.getConnection();
        voucherDao = new VoucherDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            HttpSession session = req.getSession();
            NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
            if (currentUser == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            switch (action) {
                case "listAvailable":
                    // Nhân viên xem voucher có thể dùng
                    List<Voucher> availableVouchers = voucherDao.getAvailable(LocalDateTime.now());
                    req.setAttribute("listVoucher", availableVouchers);
                    RequestDispatcher rdAvailable = req.getRequestDispatcher("voucherList.jsp");
                    rdAvailable.forward(req, resp);
                    break;

                case "list":
                    // Quản lý xem danh sách voucher (có phân trang)
                    if (!"QuanLy".equals(currentUser.getVaiTro())) {
                        resp.sendError(403, "Bạn không có quyền truy cập trang này");
                        return;
                    }

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

                    int totalVoucher = voucherDao.countAll();
                    int totalPages = (int) Math.ceil((double) totalVoucher / pageSize);
                    if (page > totalPages && totalPages > 0) page = totalPages;
                    int offset = (page - 1) * pageSize;

                    req.setAttribute("listVoucher", voucherDao.getPage(offset, pageSize));
                    req.setAttribute("totalVoucher", totalVoucher);
                    req.setAttribute("totalPages", totalPages);
                    req.setAttribute("currentPage", page);
                    req.setAttribute("pageSize", pageSize);

                    RequestDispatcher rdList = req.getRequestDispatcher("voucherManage.jsp");
                    rdList.forward(req, resp);
                    break;

                case "edit":
                    // Quản lý xem chi tiết voucher để sửa
                    if (!"QuanLy".equals(currentUser.getVaiTro())) {
                        resp.sendError(403, "Bạn không có quyền truy cập trang này");
                        return;
                    }

                    String maVoucher = req.getParameter("maVoucher");
                    Voucher voucher = voucherDao.findById(maVoucher);
                    req.setAttribute("voucher", voucher);
                    RequestDispatcher rdEdit = req.getRequestDispatcher("voucherEdit.jsp");
                    rdEdit.forward(req, resp);
                    break;

                case "add":
                    // Quản lý tạo voucher mới
                    if (!"QuanLy".equals(currentUser.getVaiTro())) {
                        resp.sendError(403, "Bạn không có quyền truy cập trang này");
                        return;
                    }
                    RequestDispatcher rdAdd = req.getRequestDispatcher("voucherAdd.jsp");
                    rdAdd.forward(req, resp);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            HttpSession session = req.getSession();
            NguoiDung currentUser = (NguoiDung) session.getAttribute("currentUser");
            if (currentUser == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            // Chỉ quản lý được thực hiện các thao tác CRUD
            if (!"QuanLy".equals(currentUser.getVaiTro())) {
                resp.sendError(403, "Bạn không có quyền thực hiện thao tác này");
                return;
            }

            if ("insert".equals(action)) {
                String maVoucher = req.getParameter("maVoucher");
                String tenVoucher = req.getParameter("tenVoucher");
                String loaiGiamGia = req.getParameter("loaiGiamGia");
                double giaTriGiamGia = Double.parseDouble(req.getParameter("giaTriGiamGia"));
                double donGiaTuoiNhap = Double.parseDouble(req.getParameter("donGiaTuoiNhap"));
                LocalDateTime ngayBatDau = LocalDateTime.parse(req.getParameter("ngayBatDau"));
                LocalDateTime ngayKetThuc = LocalDateTime.parse(req.getParameter("ngayKetThuc"));
                int soLanDung = Integer.parseInt(req.getParameter("soLanDung"));
                boolean trangThai = "1".equals(req.getParameter("trangThai"));

                Voucher v = new Voucher(maVoucher, tenVoucher, loaiGiamGia, giaTriGiamGia, donGiaTuoiNhap,
                        ngayBatDau, ngayKetThuc, soLanDung, soLanDung, trangThai);
                voucherDao.insert(v);
                resp.sendRedirect("voucher?action=list");

            } else if ("update".equals(action)) {
                String maVoucher = req.getParameter("maVoucher");
                String tenVoucher = req.getParameter("tenVoucher");
                String loaiGiamGia = req.getParameter("loaiGiamGia");
                double giaTriGiamGia = Double.parseDouble(req.getParameter("giaTriGiamGia"));
                double donGiaTuoiNhap = Double.parseDouble(req.getParameter("donGiaTuoiNhap"));
                LocalDateTime ngayBatDau = LocalDateTime.parse(req.getParameter("ngayBatDau"));
                LocalDateTime ngayKetThuc = LocalDateTime.parse(req.getParameter("ngayKetThuc"));
                int soLanDung = Integer.parseInt(req.getParameter("soLanDung"));
                int soLanConLai = Integer.parseInt(req.getParameter("soLanConLai"));
                boolean trangThai = "1".equals(req.getParameter("trangThai"));

                Voucher v = new Voucher(maVoucher, tenVoucher, loaiGiamGia, giaTriGiamGia, donGiaTuoiNhap,
                        ngayBatDau, ngayKetThuc, soLanDung, soLanConLai, trangThai);
                voucherDao.update(v);
                resp.sendRedirect("voucher?action=list");

            } else if ("delete".equals(action)) {
                String maVoucher = req.getParameter("maVoucher");
                voucherDao.delete(maVoucher);
                resp.sendRedirect("voucher?action=list");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

