package com.example.Controller;

import com.example.Dao.ThucDonDAO;
import com.example.Entity.ThucDon;
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
import java.util.List;

@WebServlet("/thucdon")
public class ThucDonController extends HttpServlet {
    private ThucDonDAO dao;

    @Override
    public void init() {
        Connection conn = DBConnect.getConnection();
        dao = new ThucDonDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            if (!isQuanLy(req, resp)) {
                return;
            }

            switch (action) {
                case "list": {
                    // pagination for ThucDon
                    int page = 1;
                    int pageSize = 10;
                    // support both 'page'/'size' (used by quanlyMon.jsp) and
                    // 'monPage'/'monSize' (used by index.jsp dashboard links)
                    String pageParam = req.getParameter("page");
                    String sizeParam = req.getParameter("size");
                    String altPageParam = req.getParameter("monPage");
                    String altSizeParam = req.getParameter("monSize");
                    if (pageParam == null && altPageParam != null) pageParam = altPageParam;
                    if (sizeParam == null && altSizeParam != null) sizeParam = altSizeParam;
                    if (pageParam != null) {
                        try { page = Integer.parseInt(pageParam); if (page < 1) page = 1; } catch (NumberFormatException ignored) {}
                    }
                    if (sizeParam != null) {
                        try { pageSize = Integer.parseInt(sizeParam); if (pageSize < 1) pageSize = 10; } catch (NumberFormatException ignored) {}
                    }
                    int total = dao.countAll();
                    int totalPages = (int) Math.ceil((double) total / pageSize);
                    if (page > totalPages && totalPages > 0) page = totalPages;
                    int offset = (page - 1) * pageSize;

                    req.setAttribute("listMon", dao.getPage(offset, pageSize));
                    req.setAttribute("monTotal", total);
                    req.setAttribute("monTotalPages", totalPages);
                    req.setAttribute("monCurrentPage", page);
                    req.setAttribute("monPageSize", pageSize);

                    RequestDispatcher rd = req.getRequestDispatcher("quanlyMon.jsp");
                    rd.forward(req, resp);
                    break;
                }
                case "delete": {
                    String maItem = req.getParameter("maItem");
                    dao.delete(maItem);
                    resp.sendRedirect("thucdon?action=list");
                    break;
                }
                case "edit": {
                    String maItemEdit = req.getParameter("maItem");
                    ThucDon td = dao.findById(maItemEdit);
                    req.setAttribute("mon", td);
                    RequestDispatcher rdEdit = req.getRequestDispatcher("editThucDon.jsp");
                    rdEdit.forward(req, resp);
                    break;
                }
                default: {
                    // mặc định quay về danh sách
                    resp.sendRedirect("thucdon?action=list");
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
        if (action == null) action = "";

        try {
            if (!isQuanLy(req, resp)) {
                return;
            }

            switch (action) {
                case "insert": {
                    String maItem = req.getParameter("maItem");
                    String tenItem = req.getParameter("tenItem");
                    double gia = Double.parseDouble(req.getParameter("gia"));
                    String loai = req.getParameter("loai");
                    String donViTinh = req.getParameter("donViTinh");

                    ThucDon td = new ThucDon(maItem, tenItem, gia, loai, donViTinh);
                    dao.insert(td);
                    resp.sendRedirect("thucdon?action=list");
                    break;
                }
                case "update": {
                    String maItem = req.getParameter("maItem");
                    String tenItem = req.getParameter("tenItem");
                    double gia = Double.parseDouble(req.getParameter("gia"));
                    String loai = req.getParameter("loai");
                    String donViTinh = req.getParameter("donViTinh");

                    ThucDon td = new ThucDon(maItem, tenItem, gia, loai, donViTinh);
                    dao.update(td);
                    resp.sendRedirect("thucdon?action=list");
                    break;
                }
                default: {
                    resp.sendRedirect("thucdon?action=list");
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean isQuanLy(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Object currentUser = session.getAttribute("currentUser");
        if (!(currentUser instanceof com.example.Entity.NguoiDung user)) {
            resp.sendRedirect("login.jsp");
            return false;
        }
        if (!"QuanLy".equals(user.getVaiTro())) {
            resp.sendRedirect("index.jsp");
            return false;
        }
        return true;
    }
}
