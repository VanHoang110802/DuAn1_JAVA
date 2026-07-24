package com.example.Entity;

public class NguoiDung {
    private String maND;
    private String tenND;
    private String vaiTro;     // 'QuanLy' hoặc 'NhanVien'
    private String username;
    private String password;

    public NguoiDung() {
    }

    public NguoiDung(String maND, String tenND, String vaiTro, String username, String password) {
        this.maND = maND;
        this.tenND = tenND;
        this.vaiTro = vaiTro;
        this.username = username;
        this.password = password;
    }

    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }

    public String getTenND() {
        return tenND;
    }

    public void setTenND(String tenND) {
        this.tenND = tenND;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
