package com.example.Dao;

import com.example.Entity.Voucher;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {
    private Connection conn;

    public VoucherDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả voucher
    public List<Voucher> getAll() throws SQLException {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher ORDER BY NgayBatDau DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Voucher v = mapResultSetToVoucher(rs);
            list.add(v);
        }
        return list;
    }

    // Lấy voucher còn có thể sử dụng (hoạt động, còn lượt, chưa hết hạn)
    public List<Voucher> getAvailable(LocalDateTime ngayHienTai) throws SQLException {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher WHERE TrangThai = 1 AND SoLanConLai > 0 " +
                     "AND NgayBatDau <= ? AND NgayKetThuc >= ? ORDER BY NgayBatDau DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(ngayHienTai));
            ps.setTimestamp(2, Timestamp.valueOf(ngayHienTai));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Voucher v = mapResultSetToVoucher(rs);
                    list.add(v);
                }
            }
        }
        return list;
    }

    // Lấy 1 trang voucher (dành cho quản lý)
    public List<Voucher> getPage(int offset, int limit) throws SQLException {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher ORDER BY NgayBatDau DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Voucher v = mapResultSetToVoucher(rs);
                    list.add(v);
                }
            }
        }
        return list;
    }

    // Tính tổng số voucher
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Voucher";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Tìm voucher theo mã
    public Voucher findById(String maVoucher) throws SQLException {
        String sql = "SELECT * FROM Voucher WHERE MaVoucher = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVoucher);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVoucher(rs);
                }
            }
        }
        return null;
    }

    // Thêm voucher mới
    public void insert(Voucher v) throws SQLException {
        String sql = "INSERT INTO Voucher (MaVoucher, TenVoucher, LoaiGiamGia, GiaTriGiamGia, " +
                     "DonGiaTuoiNhap, NgayBatDau, NgayKetThuc, SoLanDung, SoLanConLai, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMaVoucher());
            ps.setString(2, v.getTenVoucher());
            ps.setString(3, v.getLoaiGiamGia());
            ps.setDouble(4, v.getGiaTriGiamGia());
            ps.setDouble(5, v.getDonGiaTuoiNhap());
            ps.setTimestamp(6, Timestamp.valueOf(v.getNgayBatDau()));
            ps.setTimestamp(7, Timestamp.valueOf(v.getNgayKetThuc()));
            ps.setInt(8, v.getSoLanDung());
            ps.setInt(9, v.getSoLanConLai());
            ps.setBoolean(10, v.isTrangThai());
            ps.executeUpdate();
        }
    }

    // Cập nhật voucher
    public void update(Voucher v) throws SQLException {
        String sql = "UPDATE Voucher SET TenVoucher = ?, LoaiGiamGia = ?, GiaTriGiamGia = ?, " +
                     "DonGiaTuoiNhap = ?, NgayBatDau = ?, NgayKetThuc = ?, SoLanDung = ?, " +
                     "SoLanConLai = ?, TrangThai = ? WHERE MaVoucher = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getTenVoucher());
            ps.setString(2, v.getLoaiGiamGia());
            ps.setDouble(3, v.getGiaTriGiamGia());
            ps.setDouble(4, v.getDonGiaTuoiNhap());
            ps.setTimestamp(5, Timestamp.valueOf(v.getNgayBatDau()));
            ps.setTimestamp(6, Timestamp.valueOf(v.getNgayKetThuc()));
            ps.setInt(7, v.getSoLanDung());
            ps.setInt(8, v.getSoLanConLai());
            ps.setBoolean(9, v.isTrangThai());
            ps.setString(10, v.getMaVoucher());
            ps.executeUpdate();
        }
    }

    // Xóa voucher
    public void delete(String maVoucher) throws SQLException {
        String sql = "DELETE FROM Voucher WHERE MaVoucher = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVoucher);
            ps.executeUpdate();
        }
    }

    // Giảm số lượt sử dụng còn lại (khi dùng voucher)
    public void decrementUsage(String maVoucher) throws SQLException {
        String sql = "UPDATE Voucher SET SoLanConLai = SoLanConLai - 1 WHERE MaVoucher = ? AND SoLanConLai > 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVoucher);
            ps.executeUpdate();
        }
    }

    // Helper: Map ResultSet to Voucher object
    private Voucher mapResultSetToVoucher(ResultSet rs) throws SQLException {
        return new Voucher(
                rs.getString("MaVoucher"),
                rs.getString("TenVoucher"),
                rs.getString("LoaiGiamGia"),
                rs.getDouble("GiaTriGiamGia"),
                rs.getDouble("DonGiaTuoiNhap"),
                rs.getTimestamp("NgayBatDau").toLocalDateTime(),
                rs.getTimestamp("NgayKetThuc").toLocalDateTime(),
                rs.getInt("SoLanDung"),
                rs.getInt("SoLanConLai"),
                rs.getBoolean("TrangThai")
        );
    }

    // Cập nhật trạng thái voucher theo ngày hết hạn
    public void updateExpiredStatus() throws SQLException {
        String sql = "UPDATE Voucher SET TrangThai = 0 WHERE NgayKetThuc < ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }
    }

}

