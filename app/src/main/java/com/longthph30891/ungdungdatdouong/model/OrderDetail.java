package com.longthph30891.ungdungdatdouong.model;

public class OrderDetail {
    private String idOrderDetail, orderId, idProduct, imageProduct, nameProduct;
    private double price;
    private int quantity;

    public OrderDetail() {
    }

    public OrderDetail(String idOrderDetail, String orderId, String idProduct, String imageProduct, String nameProduct, double price, int quantity) {
        this.idOrderDetail = idOrderDetail;
        this.orderId = orderId;
        this.idProduct = idProduct;
        this.imageProduct = imageProduct;
        this.nameProduct = nameProduct;
        this.price = price;
        this.quantity = quantity;
    }

    public String getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(String idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
