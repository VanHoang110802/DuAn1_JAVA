package com.example.Dao;

import com.example.Entity.HoaDon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    private Connection conn;

    public HoaDonDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả hóa đơn (sắp xếp theo NgayLap DESC - mới nhất trước)
    public List<HoaDon> getAll() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY NgayLap DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            HoaDon hd = new HoaDon(
                    rs.getString("MaHD"),
                    rs.getTimestamp("NgayLap").toLocalDateTime(),
                    rs.getDouble("TongTien"),
                    rs.getString("TrangThai"),
                    rs.getString("MaBan"),
                    rs.getString("MaND"),
                    rs.getString("MaKH"),
                    rs.getString("MaVoucher")
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

    // Đếm với tìm kiếm theo từ khoá (tìm trong MaHD, MaBan, MaND)
    public int countSearch(String q) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM HoaDon WHERE MaHD LIKE ? OR MaBan LIKE ? OR MaND LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + q + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        }
        return 0;
    }

    // Lấy 1 trang hóa đơn (offset, limit) - dành cho server-side pagination
    public List<HoaDon> getPage(int offset, int limit) throws SQLException {
        return getPage(offset, limit, "DESC"); // Mặc định sắp xếp mới nhất trước
    }

    // Lấy 1 trang hóa đơn với tuỳ chọn sắp xếp theo ngày (offset, limit, sort direction)
    public List<HoaDon> getPage(int offset, int limit, String sortDirection) throws SQLException {
        return getPageSearch(offset, limit, sortDirection, null);
    }

    // Lấy 1 trang hóa đơn với tùy chọn tìm kiếm (q): nếu q == null lấy tất cả
    public List<HoaDon> getPageSearch(int offset, int limit, String sortDirection, String q) throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        if (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC")) {
            sortDirection = "DESC";
        }
        String sql;
        if (q == null || q.trim().isEmpty()) {
            sql = "SELECT * FROM HoaDon ORDER BY NgayLap " + sortDirection + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, offset);
                ps.setInt(2, limit);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        HoaDon hd = new HoaDon(
                                rs.getString("MaHD"),
                                rs.getTimestamp("NgayLap").toLocalDateTime(),
                                rs.getDouble("TongTien"),
                                rs.getString("TrangThai"),
                                rs.getString("MaBan"),
                                rs.getString("MaND"),
                                rs.getString("MaKH"),
                                rs.getString("MaVoucher")
                        );
                        list.add(hd);
                    }
                }
            }
        } else {
            sql = "SELECT * FROM HoaDon WHERE MaHD LIKE ? OR MaBan LIKE ? OR MaND LIKE ? ORDER BY NgayLap " + sortDirection + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String like = "%" + q + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setInt(4, offset);
                ps.setInt(5, limit);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        HoaDon hd = new HoaDon(
                                rs.getString("MaHD"),
                                rs.getTimestamp("NgayLap").toLocalDateTime(),
                                rs.getDouble("TongTien"),
                                rs.getString("TrangThai"),
                                rs.getString("MaBan"),
                                rs.getString("MaND"),
                                rs.getString("MaKH"),
                                rs.getString("MaVoucher")
                        );
                        list.add(hd);
                    }
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
                    rs.getTimestamp("NgayLap").toLocalDateTime(),
                    rs.getDouble("TongTien"),
                    rs.getString("TrangThai"),
                    rs.getString("MaBan"),
                    rs.getString("MaND"),
                    rs.getString("MaKH"),
                    rs.getString("MaVoucher")
            );
        }
        return null;
    }

    // Thêm hóa đơn mới
    public void insert(HoaDon hd) throws SQLException {
        String sql = "INSERT INTO HoaDon (MaHD, NgayLap, TongTien, TrangThai, MaBan, MaND, MaKH, MaVoucher) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, hd.getMaHD());
        ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
        ps.setDouble(3, hd.getTongTien());
        ps.setString(4, hd.getTrangThai());
        ps.setString(5, hd.getMaBan());
        ps.setString(6, hd.getMaND());
        ps.setString(7, hd.getMaKH());
        ps.setString(8, hd.getMaVoucher());
        ps.executeUpdate();
    }

    // Cập nhật hóa đơn
    public void update(HoaDon hd) throws SQLException {
        String sql = "UPDATE HoaDon SET NgayLap=?, TongTien=?, TrangThai=?, MaBan=?, MaND=?, MaKH=?, MaVoucher=? WHERE MaHD=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(hd.getNgayLap()));
        ps.setDouble(2, hd.getTongTien());
        ps.setString(3, hd.getTrangThai());
        ps.setString(4, hd.getMaBan());
        ps.setString(5, hd.getMaND());
        ps.setString(6, hd.getMaKH());
        ps.setString(7, hd.getMaVoucher());
        ps.setString(8, hd.getMaHD());
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
