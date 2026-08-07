package com.example.Entity;

public class ThongKeNgay {
    private String ngay; // formatted date (yyyy-MM-dd or yyyy-MM)
    private double tongDoanhThu;

    public ThongKeNgay(String ngay, double tongDoanhThu) {
        this.ngay = ngay;
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getNgay() { return ngay; }
    public double getTongDoanhThu() { return tongDoanhThu; }
}

