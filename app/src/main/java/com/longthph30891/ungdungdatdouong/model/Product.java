package com.longthph30891.ungdungdatdouong.model;

public class Product {
    private String idDoUong, maLoai, tenDoUong, image, gia, moTa, trangThai;

    public Product() {
    }

    public Product(String idDoUong, String maLoai, String tenDoUong, String image, String gia, String moTa, String trangThai) {
        this.idDoUong = idDoUong;
        this.maLoai = maLoai;
        this.tenDoUong = tenDoUong;
        this.image = image;
        this.gia = gia;
        this.moTa = moTa;
        this.trangThai = trangThai;
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

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
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

    @Override
    public String toString() {
        return "Product{" +
                "idDoUong='" + idDoUong + '\'' +
                ", maLoai='" + maLoai + '\'' +
                ", tenDoUong='" + tenDoUong + '\'' +
                ", image='" + image + '\'' +
                ", gia='" + gia + '\'' +
                ", moTa='" + moTa + '\'' +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
