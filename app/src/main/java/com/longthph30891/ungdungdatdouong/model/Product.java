package com.longthph30891.ungdungdatdouong.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String idDoUong, maLoai, tenDoUong, image, moTa, trangThai;
    private double gia;

    public Product() {
    }

    public Product(String idDoUong, String maLoai, String tenDoUong, String image, String moTa, String trangThai, double gia) {
        this.idDoUong = idDoUong;
        this.maLoai = maLoai;
        this.tenDoUong = tenDoUong;
        this.image = image;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.gia = gia;
    }

    public String getIdDoUong() {
        return idDoUong;
    }

    public void setIdDoUong(String idDoUong) {
        this.idDoUong = idDoUong;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenDoUong() {
        return tenDoUong;
    }

    public void setTenDoUong(String tenDoUong) {
        this.tenDoUong = tenDoUong;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idDoUong='" + idDoUong + '\'' +
                ", maLoai='" + maLoai + '\'' +
                ", tenDoUong='" + tenDoUong + '\'' +
                ", image='" + image + '\'' +
                ", moTa='" + moTa + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", gia=" + gia +
                '}';
    }
}
