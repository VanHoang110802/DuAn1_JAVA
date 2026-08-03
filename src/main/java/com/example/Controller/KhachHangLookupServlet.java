package com.example.Controller;

import com.example.Dao.KhachHangDAO;
import com.example.Entity.KhachHang;
import com.example.JDBC.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * Servlet phục vụ tra cứu Khách hàng theo số điện thoại dưới dạng JSON.
 * Trả về: {"found":true/false, "maKH":"...", "tenKH":"...", "soDienThoai":"..."}
 */
@WebServlet("/khachhang")
public class KhachHangLookupServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() {
        conn = DBConnect.getConnection();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phone = req.getParameter("phone");
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            if (phone == null || phone.trim().isEmpty()) {
                out.print("{\"found\":false}");
                return;
            }
            KhachHangDAO dao = new KhachHangDAO(conn);
            KhachHang kh = null;
            try {
                kh = dao.findBySoDienThoai(phone.trim());
            } catch (Exception e) {
                resp.setStatus(500);
                out.print("{\"found\":false}");
                return;
            }
            if (kh == null) {
                out.print("{\"found\":false}");
            } else {
                // simple JSON encoding (fields are simple; escape quotes if present)
                String ten = kh.getTenKH() != null ? kh.getTenKH().replace("\"", "\\\"") : "";
                String ma = kh.getMaKH() != null ? kh.getMaKH().replace("\"", "\\\"") : "";
                String sdt = kh.getSoDienThoai() != null ? kh.getSoDienThoai().replace("\"", "\\\"") : "";
                out.print("{\"found\":true,\"maKH\":\"" + ma + "\",\"tenKH\":\"" + ten + "\",\"soDienThoai\":\"" + sdt + "\"}");
            }
        }
    }
}

