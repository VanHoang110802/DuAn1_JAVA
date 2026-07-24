package com.example.Dao;

import com.example.Entity.Ban;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BanDAO {
    private Connection conn;

    public BanDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả bàn
    public List<Ban> getAll() throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Ban b = new Ban(
                    rs.getString("MaBan"),
                    rs.getInt("SoGhe"),
                    rs.getBoolean("TinhTrang") // BIT → boolean
            );
            list.add(b);
        }
        return list;
    }

    // Đếm tổng số bàn (dùng cho phân trang)
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Ban";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("total");
        }
        return 0;
    }

    // Lấy 1 trang bàn
    public List<Ban> getPage(int offset, int limit) throws SQLException {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban ORDER BY MaBan OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ban b = new Ban(
                            rs.getString("MaBan"),
                            rs.getInt("SoGhe"),
                            rs.getBoolean("TinhTrang")
                    );
                    list.add(b);
                }
            }
        }
        return list;
    }

    // Tìm bàn theo mã
    public Ban findById(String maBan) throws SQLException {
        String sql = "SELECT * FROM Ban WHERE MaBan=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maBan);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Ban(
                    rs.getString("MaBan"),
                    rs.getInt("SoGhe"),
                    rs.getBoolean("TinhTrang")
            );
        }
        return null;
    }

    // Thêm bàn mới
    public void insert(Ban b) throws SQLException {
        String sql = "INSERT INTO Ban VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, b.getMaBan());
        ps.setInt(2, b.getSoGhe());
        ps.setBoolean(3, b.isTinhTrang());
        ps.executeUpdate();
    }

    // Cập nhật bàn
    public void update(Ban b) throws SQLException {
        String sql = "UPDATE Ban SET SoGhe=?, TinhTrang=? WHERE MaBan=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, b.getSoGhe());
        ps.setBoolean(2, b.isTinhTrang());
        ps.setString(3, b.getMaBan());
        ps.executeUpdate();
    }

    // Xóa bàn
    public void delete(String maBan) throws SQLException {
        String sql = "DELETE FROM Ban WHERE MaBan=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maBan);
        ps.executeUpdate();
    }
}
