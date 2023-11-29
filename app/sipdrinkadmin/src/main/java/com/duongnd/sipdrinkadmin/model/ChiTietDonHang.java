package com.duongnd.sipdrinkadmin.model;

public class ChiTietDonHang {
    private String idDonHang, idDoUong;
    private int soLuong;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(String idDonHang, String idDoUong, int soLuong) {
        this.idDonHang = idDonHang;
        this.idDoUong = idDoUong;
        this.soLuong = soLuong;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public ChiTietDonHang setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
        return this;
    }

    public String getIdDoUong() {
        return idDoUong;
    }

    public ChiTietDonHang setIdDoUong(String idDoUong) {
        this.idDoUong = idDoUong;
        return this;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public ChiTietDonHang setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        return this;
    }
}
