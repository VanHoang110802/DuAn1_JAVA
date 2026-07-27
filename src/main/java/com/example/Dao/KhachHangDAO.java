package com.example.Dao;

import com.example.Entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    private Connection conn;

    public KhachHangDAO(Connection conn) {
        this.conn = conn;
    }

    public List<KhachHang> getAll() throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new KhachHang(
                        rs.getString("MaKH"),
                        rs.getString("TenKH"),
                                        rs.getString("SoDienThoai")
                ));
            }
        }
        return list;
    }

    public KhachHang findBySoDienThoai(String soDienThoai) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soDienThoai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("MaKH"),
                            rs.getString("TenKH"),
                            rs.getString("SoDienThoai")
                    );
                }
            }
        }
        return null;
    }

    public KhachHang findById(String maKH) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("MaKH"),
                            rs.getString("TenKH"),
                                    rs.getString("SoDienThoai")
                    );
                }
            }
        }
        return null;
    }

    public void insert(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSoDienThoai());
            ps.executeUpdate();
        }
    }

    public void update(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET TenKH=?, SoDienThoai=? WHERE MaKH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getMaKH());
            ps.executeUpdate();
        }
    }

    public void delete(String maKH) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE MaKH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            ps.executeUpdate();
        }
    }
}

