package com.example.Dao;

import com.example.Entity.ChiTietHoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    private Connection conn;
    public ChiTietHoaDonDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả chi tiết hóa đơn
    public List<ChiTietHoaDon> getAll() throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            ChiTietHoaDon ct = new ChiTietHoaDon(
                    rs.getString("MaHD"),
                    rs.getString("MaItem"),
                    rs.getInt("SoLuong"),
                    rs.getDouble("DonGia")
            );
            list.add(ct);
        }
        return list;
    }

    // Tìm chi tiết
    public List<ChiTietHoaDon> findByHoaDon(String maHD) throws Exception {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maHD);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ChiTietHoaDon ct = new ChiTietHoaDon(
                    rs.getString("maHD"),
                    rs.getString("maItem"),
                    rs.getInt("soLuong"),
                    rs.getDouble("donGia")
            );
            list.add(ct);
        }
        return list;
    }

    // Đếm số dòng chi tiết cho 1 hóa đơn (dùng phân trang)
    public int countByHoaDon(String maHD) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM ChiTietHoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        }
        return 0;
    }

    // Lấy 1 trang chi tiết hóa đơn
    public List<ChiTietHoaDon> getPageByHoaDon(String maHD, int offset, int limit) throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ? ORDER BY MaItem OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon(
                            rs.getString("MaHD"),
                            rs.getString("MaItem"),
                            rs.getInt("SoLuong"),
                            rs.getDouble("DonGia")
                    );
                    list.add(ct);
                }
            }
        }
        return list;
    }

    // Kiểm tra chi tiết hóa đơn có tồn tại không
    public boolean exists(String maHD, String maItem) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE MaHD=? AND MaItem=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maItem);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Thêm chi tiết hóa đơn mới
    public void insert(ChiTietHoaDon ct) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ct.getMaHD());
        ps.setString(2, ct.getMaItem());
        ps.setInt(3, ct.getSoLuong());
        ps.setDouble(4, ct.getDonGia());
        ps.executeUpdate();
    }

    // Cập nhật chi tiết hóa đơn
    public void update(ChiTietHoaDon ct) throws SQLException {
        String sql = "UPDATE ChiTietHoaDon SET SoLuong=?, DonGia=? WHERE MaHD=? AND MaItem=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ct.getSoLuong());
        ps.setDouble(2, ct.getDonGia());
        ps.setString(3, ct.getMaHD());
        ps.setString(4, ct.getMaItem());
        ps.executeUpdate();
    }

    // Xóa chi tiết hóa đơn
    public void delete(String maHD, String maItem) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD=? AND MaItem=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maHD);
        ps.setString(2, maItem);
        ps.executeUpdate();
    }

    // Tính tổng tiền của hóa đơn
    public double tinhTongTien(String maHD) throws SQLException {
        double tong = 0;
        String sql = "SELECT SUM(SoLuong * DonGia) AS TongTien FROM ChiTietHoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tong = rs.getDouble("TongTien");
            }
        }
        return tong;
    }
}
