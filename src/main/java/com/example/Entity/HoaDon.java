package com.example.Entity;

import java.time.LocalDate;

public class HoaDon {
    private String maHD;
    private LocalDate ngayLap;
    private double tongTien;
    private String trangThai;
    private String maBan;
    private String maND;
    private String maKH;
    private double tienThua;

    public HoaDon() {
    }

    // Backwards-compatible constructor (maKH will be null)
    public HoaDon(String maHD, LocalDate ngayLap, double tongTien, String trangThai, String maBan, String maND) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.maBan = maBan;
        this.maND = maND;
        this.maKH = null;
    }

    // New constructor including MaKH
    public HoaDon(String maHD, LocalDate ngayLap, double tongTien, String trangThai, String maBan, String maND, String maKH) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.maBan = maBan;
        this.maND = maND;
        this.maKH = maKH;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
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
}
