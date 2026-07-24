package com.example.Entity;

public class Ban {
    private String maBan;
    private int soGhe;
    private boolean tinhTrang; // false = bàn trống, true = đang phục vụ

    public Ban() {
    }

    public Ban(String maBan, int soGhe, boolean tinhTrang) {
        this.maBan = maBan;
        this.soGhe = soGhe;
        this.tinhTrang = tinhTrang;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
