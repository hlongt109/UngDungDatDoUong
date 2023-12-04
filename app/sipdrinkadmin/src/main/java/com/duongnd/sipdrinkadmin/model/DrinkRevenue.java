package com.duongnd.sipdrinkadmin.model;

public class DrinkRevenue {
    private String drinkName;
    private int quantyti;
    private double totalPrice;

    public DrinkRevenue(String drinkName, int quantyti, double totalPrice) {
        this.drinkName = drinkName;
        this.quantyti = quantyti;
        this.totalPrice = totalPrice;
    }

    public DrinkRevenue() {
    }

    public String getDrinkName() {
        return drinkName;
    }

    public DrinkRevenue setDrinkName(String drinkName) {
        this.drinkName = drinkName;
        return this;
    }

    public int getQuantyti() {
        return quantyti;
    }

    public DrinkRevenue setQuantyti(int quantyti) {
        this.quantyti = quantyti;
        return this;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public DrinkRevenue setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }
}
