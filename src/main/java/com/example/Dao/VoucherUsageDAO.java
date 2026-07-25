package com.example.Dao;

import com.example.Entity.VoucherUsage;
import com.example.Entity.Voucher;
import com.example.Entity.KhachHang;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VoucherUsageDAO {
    private Connection conn;

    public VoucherUsageDAO(Connection conn) {
        this.conn = conn;
    }

    // Thêm lịch sử sử dụng voucher
    public void insert(VoucherUsage vu) throws SQLException {
        String sql = "INSERT INTO VoucherUsage (MaVoucherUsage, MaVoucher, MaKhachHang, NgayDung, MaHoaDon, GiaTriGiam) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vu.getMaVoucherUsage());
            ps.setString(2, vu.getMaVoucher());
            ps.setString(3, vu.getMaKhachHang());
            ps.setTimestamp(4, Timestamp.valueOf(vu.getNgayDung()));
            ps.setString(5, vu.getMaHoaDon());
            ps.setDouble(6, vu.getGiaTriGiam());
            ps.executeUpdate();
        }
    }

    // Lấy danh sách voucher đã dùng bởi khách hàng
    public List<VoucherUsage> getByKhachHang(String maKhachHang) throws SQLException {
        List<VoucherUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM VoucherUsage WHERE MaKhachHang = ? ORDER BY NgayDung DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VoucherUsage vu = mapResultSetToVoucherUsage(rs);
                    list.add(vu);
                }
            }
        }
        return list;
    }

    // Kiểm tra khách hàng đó đã dùng voucher này bao nhiêu lần
    public int countUsageByKhachHangAndVoucher(String maKhachHang, String maVoucher) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM VoucherUsage WHERE MaKhachHang = ? AND MaVoucher = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            ps.setString(2, maVoucher);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        }
        return 0;
    }

    // Lấy lịch sử sử dụng voucher (phân trang)
    public List<VoucherUsage> getPage(int offset, int limit) throws SQLException {
        List<VoucherUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM VoucherUsage ORDER BY NgayDung DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VoucherUsage vu = mapResultSetToVoucherUsage(rs);
                    list.add(vu);
                }
            }
        }
        return list;
    }

    // Tính tổng số lịch sử sử dụng voucher
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM VoucherUsage";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Helper: Map ResultSet to VoucherUsage object
    private VoucherUsage mapResultSetToVoucherUsage(ResultSet rs) throws SQLException {
        return new VoucherUsage(
                rs.getString("MaVoucherUsage"),
                rs.getString("MaVoucher"),
                rs.getString("MaKhachHang"),
                rs.getTimestamp("NgayDung").toLocalDateTime(),
                rs.getString("MaHoaDon"),
                rs.getDouble("GiaTriGiam")
        );
    }
}

