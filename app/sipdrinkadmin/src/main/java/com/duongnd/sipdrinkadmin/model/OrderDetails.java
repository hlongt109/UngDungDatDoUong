package com.duongnd.sipdrinkadmin.model;

public class OrderDetails {
    private String idOrderDetails;
    private String orderId;
    private String idProduct;
    private int quantity;
    private double price;
    private String nameProduct, imageProduct;

    public OrderDetails(String idOrderDetails, String orderId, String idProduct, int quantity, double price, String nameProduct, String imageProduct) {
        this.idOrderDetails = idOrderDetails;
        this.orderId = orderId;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
        this.nameProduct = nameProduct;
        this.imageProduct = imageProduct;
    }

    public OrderDetails() {
    }

    public String getIdOrderDetails() {
        return idOrderDetails;
    }

    public OrderDetails setIdOrderDetails(String idOrderDetails) {
        this.idOrderDetails = idOrderDetails;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderDetails setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public OrderDetails setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderDetails setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public OrderDetails setPrice(double price) {
        this.price = price;
        return this;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public OrderDetails setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
        return this;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public OrderDetails setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
        return this;
    }
}
