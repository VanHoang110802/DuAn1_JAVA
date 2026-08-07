package com.example.Entity;

public class ThongKeMon {
    private String maItem;
    private String tenItem;
    private int soLuongBan;
    private double doanhThu;

    public ThongKeMon(String maItem, String tenItem, int soLuongBan, double doanhThu) {
        this.maItem = maItem;
        this.tenItem = tenItem;
        this.soLuongBan = soLuongBan;
        this.doanhThu = doanhThu;
    }

    public String getMaItem() { return maItem; }
    public String getTenItem() { return tenItem; }
    public int getSoLuongBan() { return soLuongBan; }
    public double getDoanhThu() { return doanhThu; }
}

