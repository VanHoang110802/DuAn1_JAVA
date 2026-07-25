package com.example.Entity;

import java.time.LocalDateTime;

public class Voucher {
    private String maVoucher;
    private String tenVoucher;
    private String loaiGiamGia;  // 'TienMat' hoặc 'PhanTram'
    private double giaTriGiamGia;
    private double donGiaTuoiNhap;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private int soLanDung;
    private int soLanConLai;
    private boolean trangThai;  // true: hoạt động, false: vô hiệu hóa

    public Voucher() {
    }

    public Voucher(String maVoucher, String tenVoucher, String loaiGiamGia, double giaTriGiamGia,
                   double donGiaTuoiNhap, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc,
                   int soLanDung, int soLanConLai, boolean trangThai) {
        this.maVoucher = maVoucher;
        this.tenVoucher = tenVoucher;
        this.loaiGiamGia = loaiGiamGia;
        this.giaTriGiamGia = giaTriGiamGia;
        this.donGiaTuoiNhap = donGiaTuoiNhap;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.soLanDung = soLanDung;
        this.soLanConLai = soLanConLai;
        this.trangThai = trangThai;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getTenVoucher() {
        return tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public String getLoaiGiamGia() {
        return loaiGiamGia;
    }

    public void setLoaiGiamGia(String loaiGiamGia) {
        this.loaiGiamGia = loaiGiamGia;
    }

    public double getGiaTriGiamGia() {
        return giaTriGiamGia;
    }

    public void setGiaTriGiamGia(double giaTriGiamGia) {
        this.giaTriGiamGia = giaTriGiamGia;
    }

    public double getDonGiaTuoiNhap() {
        return donGiaTuoiNhap;
    }

    public void setDonGiaTuoiNhap(double donGiaTuoiNhap) {
        this.donGiaTuoiNhap = donGiaTuoiNhap;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getSoLanDung() {
        return soLanDung;
    }

    public void setSoLanDung(int soLanDung) {
        this.soLanDung = soLanDung;
    }

    public int getSoLanConLai() {
        return soLanConLai;
    }

    public void setSoLanConLai(int soLanConLai) {
        this.soLanConLai = soLanConLai;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    // Phương thức kiểm tra voucher còn có thể sử dụng được không
    public boolean isConThe(LocalDateTime ngayHienTai) {
        return trangThai && soLanConLai > 0 &&
               !ngayHienTai.isBefore(ngayBatDau) &&
               !ngayHienTai.isAfter(ngayKetThuc);
    }
}

