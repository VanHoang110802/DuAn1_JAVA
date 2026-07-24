package com.example.Dao;

import com.example.Entity.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    private Connection conn;

    public HoaDonDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả hóa đơn
    public List<HoaDon> getAll() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            HoaDon hd = new HoaDon(
                    rs.getString("MaHD"),
                    rs.getDate("NgayLap").toLocalDate(),
                    rs.getDouble("TongTien"),
                    rs.getString("TrangThai"),
                    rs.getString("MaBan"),
                    rs.getString("MaND"),
                    rs.getString("MaKH")
            );
            list.add(hd);
        }
        return list;
    }

    // Lấy tổng số hóa đơn (dùng cho phân trang)
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM HoaDon";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Lấy 1 trang hóa đơn (offset, limit) - dành cho server-side pagination
    public List<HoaDon> getPage(int offset, int limit) throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        // SQL Server: cần ORDER BY khi dùng OFFSET/FETCH
        String sql = "SELECT * FROM HoaDon ORDER BY NgayLap DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon(
                            rs.getString("MaHD"),
                            rs.getDate("NgayLap").toLocalDate(),
                            rs.getDouble("TongTien"),
                            rs.getString("TrangThai"),
                            rs.getString("MaBan"),
                            rs.getString("MaND"),
                            rs.getString("MaKH")
                    );
                    list.add(hd);
                }
            }
        }
        return list;
    }

    // Tìm hóa đơn theo mã
    public HoaDon findById(String maHD) throws SQLException {
        String sql = "SELECT * FROM HoaDon WHERE MaHD=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maHD);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new HoaDon(
                    rs.getString("MaHD"),
                    rs.getDate("NgayLap").toLocalDate(),
                    rs.getDouble("TongTien"),
                    rs.getString("TrangThai"),
                    rs.getString("MaBan"),
                    rs.getString("MaND"),
                    rs.getString("MaKH")
            );
        }
        return null;
    }

    // Thêm hóa đơn mới
    public void insert(HoaDon hd) throws SQLException {
        String sql = "INSERT INTO HoaDon (MaHD, NgayLap, TongTien, TrangThai, MaBan, MaND, MaKH) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, hd.getMaHD());
        ps.setDate(2, Date.valueOf(hd.getNgayLap()));
        ps.setDouble(3, hd.getTongTien());
        ps.setString(4, hd.getTrangThai());
        ps.setString(5, hd.getMaBan());
        ps.setString(6, hd.getMaND());
        ps.setString(7, hd.getMaKH());
        ps.executeUpdate();
    }

    // Cập nhật hóa đơn
    public void update(HoaDon hd) throws SQLException {
        String sql = "UPDATE HoaDon SET NgayLap=?, TongTien=?, TrangThai=?, MaBan=?, MaND=?, MaKH=? WHERE MaHD=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDate(1, Date.valueOf(hd.getNgayLap()));
        ps.setDouble(2, hd.getTongTien());
        ps.setString(3, hd.getTrangThai());
        ps.setString(4, hd.getMaBan());
        ps.setString(5, hd.getMaND());
        ps.setString(6, hd.getMaKH());
        ps.setString(7, hd.getMaHD());
        ps.executeUpdate();
    }

    // Xóa hóa đơn
    public void delete(String maHD) throws SQLException {
        String sql = "DELETE FROM HoaDon WHERE MaHD=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maHD);
        ps.executeUpdate();
    }
}
