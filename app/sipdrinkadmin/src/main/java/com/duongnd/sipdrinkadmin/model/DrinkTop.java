package com.duongnd.sipdrinkadmin.model;

public class DrinkTop {
    private String name;
    private int quantity;
    private double totalRevenue;
    private String img;

    public DrinkTop(String name, int quantity, double totalRevenue,String img) {
        this.name = name;
        this.quantity = quantity;
        this.totalRevenue = totalRevenue;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public DrinkTop setImg(String img) {
        this.img = img;
        return this;
    }

    public String getName() {
        return name;
    }

    public DrinkTop setName(String name) {
        this.name = name;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public DrinkTop setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public DrinkTop setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }
}
