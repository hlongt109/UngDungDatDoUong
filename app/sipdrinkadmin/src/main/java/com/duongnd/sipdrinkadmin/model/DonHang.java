package com.duongnd.sipdrinkadmin.model;

public class DonHang {
    private String idDonHang, idGioHang, idKH, ngayMua, trangThai;
    private double tongTien;

    public DonHang() {
    }

    public DonHang(String idDonHang, String idGioHang, String idKH, String ngayMua, String trangThai, double tongTien) {
        this.idDonHang = idDonHang;
        this.idGioHang = idGioHang;
        this.idKH = idKH;
        this.ngayMua = ngayMua;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public DonHang setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
        return this;
    }

    public String getIdGioHang() {
        return idGioHang;
    }

    public DonHang setIdGioHang(String idGioHang) {
        this.idGioHang = idGioHang;
        return this;
    }

    public String getIdKH() {
        return idKH;
    }

    public DonHang setIdKH(String idKH) {
        this.idKH = idKH;
        return this;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public DonHang setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
        return this;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public DonHang setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        return this;
    }

    public double getTongTien() {
        return tongTien;
    }

    public DonHang setTongTien(double tongTien) {
        this.tongTien = tongTien;
        return this;
    }
}
