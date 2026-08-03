package com.example.Controller;

import com.example.Dao.VoucherDAO;
import com.example.Entity.Voucher;
import com.example.JDBC.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Trả về danh sách voucher dưới dạng JSON để client có thể cập nhật combobox mà không reload toàn bộ trang.
 */
@WebServlet("/vouchers")
public class VoucherListServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() {
        conn = DBConnect.getConnection();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            VoucherDAO dao = new VoucherDAO(conn);
            List<Voucher> list = dao.getAll();
            LocalDateTime now = LocalDateTime.now();
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            boolean first = true;
            for (Voucher v : list) {
                if (!first) sb.append(',');
                first = false;
                boolean active = v.isTrangThai();
                boolean expired = v.getNgayKetThuc().isBefore(now);
                boolean notStarted = v.getNgayBatDau().isAfter(now);
                sb.append('{')
                  .append("\"maVoucher\":\"").append(escape(v.getMaVoucher())).append("\"")
                  .append(",\"tenVoucher\":\"").append(escape(v.getTenVoucher())).append("\"")
                  .append(",\"loai\":\"").append(escape(v.getLoaiGiamGia())).append("\"")
                  .append(",\"giaTri\":").append((long) v.getGiaTriGiamGia())
                  .append(",\"min\":").append((long) v.getDonGiaTuoiNhap())
                  .append(",\"ngayBatDau\":\"").append(v.getNgayBatDau().toString()).append("\"")
                  .append(",\"ngayKetThuc\":\"").append(v.getNgayKetThuc().toString()).append("\"")
                  .append(",\"remaining\":").append(v.getSoLanConLai())
                  .append(",\"active\":").append(active)
                  .append(",\"expired\":").append(expired)
                  .append(",\"notStarted\":").append(notStarted)
                  .append('}');
            }
            sb.append(']');
            out.print(sb.toString());
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print("[]");
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

