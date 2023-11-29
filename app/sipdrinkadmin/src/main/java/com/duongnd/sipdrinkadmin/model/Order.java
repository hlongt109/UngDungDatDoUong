package com.duongnd.sipdrinkadmin.model;

public class Order {
    private String orderId;
    private String idUser;
    private String dateOrder, nameCustomer, phoneNumber, address,statusOrder;
    private double totalPrice;

    public Order(String orderId,String idUser, String dateOrder, String nameCustomer, String phoneNumber, String address,String status, double totalPrice) {
        this.orderId = orderId;
        this.idUser = idUser;
        this.dateOrder = dateOrder;
        this.nameCustomer = nameCustomer;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.statusOrder = status;
        this.totalPrice = totalPrice;
    }

    public String getIdUser() {
        return idUser;
    }

    public Order setIdUser(String idUser) {
        this.idUser = idUser;
        return this;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public Order setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
        return this;
    }

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public Order setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public Order setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
        return this;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public Order setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Order setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Order setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }
}
