package com.longthph30891.ungdungdatdouong.model;

public class Cart {
    private String idGioHang, idDoUong, idKhachHang;
    private int soLuong;

    public Cart() {
    }

    public Cart(String idGioHang, String idDoUong, String idKhachHang, int soLuong) {
        this.idGioHang = idGioHang;
        this.idDoUong = idDoUong;
        this.idKhachHang = idKhachHang;
        this.soLuong = soLuong;
    }

    public String getidGioHang() {
        return idGioHang;
    }

    public void setidGioHang(String idGioHang) {
        this.idGioHang = idGioHang;
    }

    public String getidDoUong() {
        return idDoUong;
    }

    public void setidDoUong(String idDoUong) {
        this.idDoUong = idDoUong;
    }

    public String getidKhachHang() {
        return idKhachHang;
    }

    public void setidKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public int getsoLuong() {
        return soLuong;
    }

    public void setsoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idGioHang='" + idGioHang + '\'' +
                ", idDoUong='" + idDoUong + '\'' +
                ", idKhachHang='" + idKhachHang + '\'' +
                ", soLuong=" + soLuong +
                '}';
    }
}
