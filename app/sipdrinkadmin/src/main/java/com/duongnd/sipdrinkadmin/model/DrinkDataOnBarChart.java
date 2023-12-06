package com.duongnd.sipdrinkadmin.model;

public class DrinkDataOnBarChart {
    private String name;
    private int totalQuantity;

    public DrinkDataOnBarChart(String name, int totalQuantity) {
        this.name = name;
        this.totalQuantity = totalQuantity;
    }

    public DrinkDataOnBarChart() {
    }

    public String getName() {
        return name;
    }

    public DrinkDataOnBarChart setName(String name) {
        this.name = name;
        return this;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public DrinkDataOnBarChart setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }
    public void addQuantity(int quantity) {
        totalQuantity += quantity;
    }
}
