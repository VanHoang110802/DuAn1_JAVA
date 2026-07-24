package com.example.Controller;

import com.example.Dao.BanDAO;
import com.example.Dao.ThucDonDAO;
import com.example.Dao.HoaDonDAO;
import com.example.Dao.ChiTietHoaDonDAO;
import com.example.Entity.Ban;
import com.example.Entity.ThucDon;
import com.example.Entity.HoaDon;
import com.example.Entity.ChiTietHoaDon;
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

@WebServlet("/index")
public class IndexController extends HttpServlet{
    private BanDAO banDao;
    private ThucDonDAO thucDonDao;
    private HoaDonDAO hoaDonDao;
    private ChiTietHoaDonDAO chiTietHoaDonDao;

    @Override
    public void init() {
        Connection conn = DBConnect.getConnection();
        banDao = new BanDAO(conn);
        thucDonDao = new ThucDonDAO(conn);
        hoaDonDao = new HoaDonDAO(conn);
        chiTietHoaDonDao = new ChiTietHoaDonDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Ban> dsBan = banDao.getAll();
            List<ThucDon> dsMon = thucDonDao.getAll();
            List<HoaDon> dsHD = hoaDonDao.getAll();
            List<ChiTietHoaDon> dsCTHD = chiTietHoaDonDao.getAll();

            req.setAttribute("listBan", dsBan);
            req.setAttribute("listMon", dsMon);
            req.setAttribute("listHD", dsHD);
            req.setAttribute("listCTHD", dsCTHD);

            RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
