package com.example.Entity;

import java.time.LocalDateTime;

public class VoucherUsage {
    private String maVoucherUsage;
    private String maVoucher;
    private String maKhachHang;
    private LocalDateTime ngayDung;
    private String maHoaDon;
    private double giaTriGiam;

    public VoucherUsage() {
    }

    public VoucherUsage(String maVoucherUsage, String maVoucher, String maKhachHang,
                        LocalDateTime ngayDung, String maHoaDon, double giaTriGiam) {
        this.maVoucherUsage = maVoucherUsage;
        this.maVoucher = maVoucher;
        this.maKhachHang = maKhachHang;
        this.ngayDung = ngayDung;
        this.maHoaDon = maHoaDon;
        this.giaTriGiam = giaTriGiam;
    }

    public String getMaVoucherUsage() {
        return maVoucherUsage;
    }

    public void setMaVoucherUsage(String maVoucherUsage) {
        this.maVoucherUsage = maVoucherUsage;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public LocalDateTime getNgayDung() {
        return ngayDung;
    }

    public void setNgayDung(LocalDateTime ngayDung) {
        this.ngayDung = ngayDung;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public double getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(double giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }
}

