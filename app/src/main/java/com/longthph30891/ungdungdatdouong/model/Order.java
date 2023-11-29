package com.longthph30891.ungdungdatdouong.model;

public class Order {
    private String orderId, idUser, nameCustomer, phoneNumber, address, statusOrder, dateOrder;
    private double totalPrice;

    public Order() {
    }

    public Order(String orderId, String idUser, String nameCustomer, String phoneNumber, String address, String statusOrder, String dateOrder, double totalPrice) {
        this.orderId = orderId;
        this.idUser = idUser;
        this.nameCustomer = nameCustomer;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.statusOrder = statusOrder;
        this.dateOrder = dateOrder;
        this.totalPrice = totalPrice;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getorderId() {
        return orderId;
    }

    public void setorderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getnameCustomer() {
        return nameCustomer;
    }

    public void setnameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getphoneNumber() {
        return phoneNumber;
    }

    public void setphoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
