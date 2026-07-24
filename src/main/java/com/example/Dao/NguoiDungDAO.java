package com.example.Dao;

import com.example.Entity.NguoiDung;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {
    private Connection conn;

    public NguoiDungDAO(Connection conn) {
        this.conn = conn;
    }

    // Đăng nhập: kiểm tra username + password
    public NguoiDung login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM NguoiDung WHERE Username=? AND Password=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new NguoiDung(
                        rs.getString("MaND"),
                        rs.getString("TenND"),
                        rs.getString("VaiTro"),
                        rs.getString("Username"),
                        rs.getString("Password")
                );
            }
        }
        return null;
    }

    // Lấy tất cả người dùng
    public List<NguoiDung> getAll() throws SQLException {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM NguoiDung";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new NguoiDung(
                        rs.getString("MaND"),
                        rs.getString("TenND"),
                        rs.getString("VaiTro"),
                        rs.getString("Username"),
                        rs.getString("Password")
                ));
            }
        }
        return list;
    }

    // Đếm tổng số người dùng (dùng cho phân trang)
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM NguoiDung";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("total");
        }
        return 0;
    }

    // Lấy 1 trang người dùng (offset, limit)
    public List<NguoiDung> getPage(int offset, int limit) throws SQLException {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM NguoiDung ORDER BY MaND OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new NguoiDung(
                            rs.getString("MaND"),
                            rs.getString("TenND"),
                            rs.getString("VaiTro"),
                            rs.getString("Username"),
                            rs.getString("Password")
                    ));
                }
            }
        }
        return list;
    }

    // Thêm người dùng
    public void insert(NguoiDung nd) throws SQLException {
        String sql = "INSERT INTO NguoiDung (MaND, TenND, VaiTro, Username, Password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nd.getMaND());
            ps.setString(2, nd.getTenND());
            ps.setString(3, nd.getVaiTro());
            ps.setString(4, nd.getUsername());
            ps.setString(5, nd.getPassword());
            ps.executeUpdate();
        }
    }

    // Cập nhật người dùng
    public void update(NguoiDung nd) throws SQLException {
        String sql = "UPDATE NguoiDung SET TenND=?, VaiTro=?, Username=?, Password=? WHERE MaND=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nd.getTenND());
            ps.setString(2, nd.getVaiTro());
            ps.setString(3, nd.getUsername());
            ps.setString(4, nd.getPassword());
            ps.setString(5, nd.getMaND());
            ps.executeUpdate();
        }
    }

    // Xóa người dùng
    public void delete(String maND) throws SQLException {
        String sql = "DELETE FROM NguoiDung WHERE MaND=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maND);
            ps.executeUpdate();
        }
    }

    public NguoiDung findById(String maND) throws SQLException {
        String sql = "SELECT * FROM NguoiDung WHERE MaND = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maND);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NguoiDung(
                            rs.getString("MaND"),
                            rs.getString("TenND"),
                            rs.getString("VaiTro"),
                            rs.getString("Username"),
                            rs.getString("Password")
                    );
                }
            }
        }
        return null;
    }

}
