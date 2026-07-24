package com.example.Dao;

import com.example.Entity.ThucDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThucDonDAO {
    private Connection conn;

    public ThucDonDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả món trong thực đơn
    public List<ThucDon> getAll() throws SQLException {
        List<ThucDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ThucDon";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            ThucDon td = new ThucDon(
                    rs.getString("MaItem"),
                    rs.getString("TenItem"),
                    rs.getDouble("Gia"),
                    rs.getString("Loai"),
                    rs.getString("DonViTinh")
            );
            list.add(td);
        }
        return list;
    }

    // Đếm tổng số món
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM ThucDon";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("total");
        }
        return 0;
    }

    // Lấy 1 trang món
    public List<ThucDon> getPage(int offset, int limit) throws SQLException {
        List<ThucDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ThucDon ORDER BY MaItem OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThucDon td = new ThucDon(
                            rs.getString("MaItem"),
                            rs.getString("TenItem"),
                            rs.getDouble("Gia"),
                            rs.getString("Loai"),
                            rs.getString("DonViTinh")
                    );
                    list.add(td);
                }
            }
        }
        return list;
    }

    // Tìm món theo mã
    public ThucDon findById(String maItem) throws SQLException {
        String sql = "SELECT * FROM ThucDon WHERE MaItem=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maItem);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new ThucDon(
                    rs.getString("MaItem"),
                    rs.getString("TenItem"),
                    rs.getDouble("Gia"),
                    rs.getString("Loai"),
                    rs.getString("DonViTinh")
            );
        }
        return null;
    }

    // Thêm món mới
    public void insert(ThucDon td) throws SQLException {
        String sql = "INSERT INTO ThucDon VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, td.getMaItem());
        ps.setString(2, td.getTenItem());
        ps.setDouble(3, td.getGia());
        ps.setString(4, td.getLoai());
        ps.setString(5, td.getDonViTinh());
        ps.executeUpdate();
    }

    // Cập nhật món
    public void update(ThucDon td) throws SQLException {
        String sql = "UPDATE ThucDon SET TenItem=?, Gia=?, Loai=?, DonViTinh=? WHERE MaItem=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, td.getTenItem());
        ps.setDouble(2, td.getGia());
        ps.setString(3, td.getLoai());
        ps.setString(4, td.getDonViTinh());
        ps.setString(5, td.getMaItem());
        ps.executeUpdate();
    }

    // Xóa món
    public void delete(String maItem) throws SQLException {
        String sql = "DELETE FROM ThucDon WHERE MaItem=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maItem);
        ps.executeUpdate();
    }
}
