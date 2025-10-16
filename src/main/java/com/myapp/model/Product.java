package com.myapp.model;

public class Product {
    private final String id;
    private final String name;
    private final String category;
    private final double price;

    public Product(final String id, final String name, final String category,
                   final double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    //<editor-fold desc="Getters">

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
    //</editor-fold>


    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }
}

