package com.example.Controller;

import com.example.Dao.NguoiDungDAO;
import com.example.Entity.NguoiDung;
import com.example.JDBC.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private NguoiDungDAO dao;

    @Override
    public void init() {
        Connection conn = DBConnect.getConnection();
        dao = new NguoiDungDAO(conn);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            NguoiDung nd = dao.login(username, password);
            if (nd != null) {
                HttpSession session = req.getSession();
                session.setAttribute("currentUser", nd);

                // Lấy vai trò, loại bỏ khoảng trắng và so sánh không phân biệt hoa/thường
                String role = nd.getVaiTro() != null ? nd.getVaiTro().trim() : "";

                if ("QuanLy".equalsIgnoreCase(role)) {
                    resp.sendRedirect("quanly.jsp");
                } else if ("NhanVien".equalsIgnoreCase(role)) {
                    System.out.println("Đang vào đây\n");
                    resp.sendRedirect("index.jsp");
                } else {
                    resp.sendRedirect("login.jsp");
                }
                System.out.println("VaiTro từ DB: '" + nd.getVaiTro() + "'");
                System.out.println("MaND: " + nd.getMaND());
            } else {
                req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
