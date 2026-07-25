package com.example.Entity;

import java.time.LocalDateTime;

public class HoaDon {
    private String maHD;
    private LocalDateTime ngayLap;
    private double tongTien;
    private String trangThai;
    private String maBan;
    private String maND;
    private String maKH;
    private String maVoucher;
    private double tienThua;

    public HoaDon() {
    }

    // Backwards-compatible constructor (maKH will be null)
    public HoaDon(String maHD, LocalDateTime ngayLap, double tongTien, String trangThai, String maBan, String maND) {
        this(maHD, ngayLap, tongTien, trangThai, maBan, maND, null, null);
    }

    // New constructor including MaKH
    public HoaDon(String maHD, LocalDateTime ngayLap, double tongTien, String trangThai, String maBan, String maND, String maKH) {
        this(maHD, ngayLap, tongTien, trangThai, maBan, maND, maKH, null);
    }

    public HoaDon(String maHD, LocalDateTime ngayLap, double tongTien, String trangThai, String maBan, String maND, String maKH, String maVoucher) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.maBan = maBan;
        this.maND = maND;
        this.maKH = maKH;
        this.maVoucher = maVoucher;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }

    public double getTienThua() { return tienThua; }
    public void setTienThua(double tienThua) { this.tienThua = tienThua; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getMaVoucher() { return maVoucher; }
    public void setMaVoucher(String maVoucher) { this.maVoucher = maVoucher; }
}
