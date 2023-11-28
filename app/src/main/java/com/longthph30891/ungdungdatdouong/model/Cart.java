package com.longthph30891.ungdungdatdouong.model;

public class Cart {
    private String idGioHang, idDoUong, idKhachHang, productName, productImage;
    private int soLuong;
    private double productPrice;

    private boolean isChecked;

    public Cart() {
    }


    public Cart(String idGioHang, String idDoUong, String idKhachHang, String productName, String productImage, int soLuong, double productPrice, boolean isChecked) {
        this.idGioHang = idGioHang;
        this.idDoUong = idDoUong;
        this.idKhachHang = idKhachHang;
        this.productName = productName;
        this.productImage = productImage;
        this.soLuong = soLuong;
        this.productPrice = productPrice;
        this.isChecked = isChecked;
    }

    public String getIdGioHang() {
        return idGioHang;
    }

    public void setIdGioHang(String idGioHang) {
        this.idGioHang = idGioHang;
    }

    public String getIdDoUong() {
        return idDoUong;
    }

    public void setIdDoUong(String idDoUong) {
        this.idDoUong = idDoUong;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
