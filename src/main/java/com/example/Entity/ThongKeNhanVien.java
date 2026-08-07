package com.example.Entity;

public class ThongKeNhanVien {
    private String maND;
    private String tenND;
    private double tongDoanhThu;

    public ThongKeNhanVien(String maND, String tenND, double tongDoanhThu) {
        this.maND = maND;
        this.tenND = tenND;
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getMaND() { return maND; }
    public String getTenND() { return tenND; }
    public double getTongDoanhThu() { return tongDoanhThu; }
}

