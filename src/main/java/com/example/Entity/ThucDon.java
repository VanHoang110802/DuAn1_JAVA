package com.example.Entity;

public class ThucDon {
    private String maItem;
    private String tenItem;
    private double gia;
    private String loai;
    private String donViTinh;
    private String imagePath; // tên file ảnh hoặc đường dẫn tương đối trong /images

    public ThucDon() {
    }

    public ThucDon(String maItem, String tenItem, double gia, String loai, String donViTinh) {
        this.maItem = maItem;
        this.tenItem = tenItem;
        this.gia = gia;
        this.loai = loai;
        this.donViTinh = donViTinh;
    }

    public ThucDon(String maItem, String tenItem, double gia, String loai, String donViTinh, String imagePath) {
        this.maItem = maItem;
        this.tenItem = tenItem;
        this.gia = gia;
        this.loai = loai;
        this.donViTinh = donViTinh;
        this.imagePath = imagePath;
    }

    public String getMaItem() {
        return maItem;
    }

    public void setMaItem(String maItem) {
        this.maItem = maItem;
    }

    public String getTenItem() {
        return tenItem;
    }

    public void setTenItem(String tenItem) {
        this.tenItem = tenItem;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
