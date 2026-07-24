package com.example.Entity;

public class ChiTietHoaDon {
    private String maHD;
    private String maItem;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String maHD, String maItem, int soLuong, double donGia) {
        this.maHD = maHD;
        this.maItem = maItem;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaItem() {
        return maItem;
    }

    public void setMaItem(String maItem) {
        this.maItem = maItem;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }
}
