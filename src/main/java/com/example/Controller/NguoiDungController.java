package com.example.Controller;

import com.example.Dao.NguoiDungDAO;
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
import java.util.List;

@WebServlet("/nguoidung")
public class NguoiDungController extends HttpServlet {
    private NguoiDungDAO dao;

    @Override
    public void init() {
        Connection conn = DBConnect.getConnection();
        dao = new NguoiDungDAO(conn);
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
                case "list":
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

                    int total = dao.countAll();
                    int totalPages = (int) Math.ceil((double) total / pageSize);
                    if (page > totalPages && totalPages > 0) page = totalPages;
                    int offset = (page - 1) * pageSize;

                    List<NguoiDung> list = dao.getPage(offset, pageSize);
                    req.setAttribute("listNV", list);
                    req.setAttribute("nvTotal", total);
                    req.setAttribute("nvTotalPages", totalPages);
                    req.setAttribute("nvCurrentPage", page);
                    req.setAttribute("nvPageSize", pageSize);
                    RequestDispatcher rd = req.getRequestDispatcher("quanlyNhanVien.jsp");
                    rd.forward(req, resp);
                    break;

                case "edit":
                    String maND = req.getParameter("maND");
                    NguoiDung nv = dao.findById(maND);
                    req.setAttribute("nv", nv);
                    RequestDispatcher rdEdit = req.getRequestDispatcher("editNhanVien.jsp");
                    rdEdit.forward(req, resp);
                    break;

                case "delete":
                    String maNDDel = req.getParameter("maND");
                    dao.delete(maNDDel);
                    resp.sendRedirect("nguoidung?action=list");
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
            if (!isQuanLy(req, resp)) {
                return;
            }

            if ("insert".equals(action)) {
                String maND = req.getParameter("maND");
                String tenND = req.getParameter("tenND");
                String vaiTro = req.getParameter("vaiTro");
                String username = req.getParameter("username");
                String password = req.getParameter("password");

                NguoiDung nv = new NguoiDung(maND, tenND, vaiTro, username, password);
                dao.insert(nv);
                resp.sendRedirect("nguoidung?action=list");

            } else if ("update".equals(action)) {
                String maND = req.getParameter("maND");
                String tenND = req.getParameter("tenND");
                String vaiTro = req.getParameter("vaiTro");
                String username = req.getParameter("username");
                String password = req.getParameter("password");

                NguoiDung nv = new NguoiDung(maND, tenND, vaiTro, username, password);
                dao.update(nv);
                resp.sendRedirect("nguoidung?action=list");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean isQuanLy(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Object currentUser = session.getAttribute("currentUser");
        if (!(currentUser instanceof NguoiDung user)) {
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
