package com.myapp.model;

import java.util.Collections;
import java.util.List;

public class Order {
    private final String orderId;
    private final List<Product> products; // List chosen for ordered access
    private final String customerName;

    public Order(String orderId, List<Product> products, String customerName) {
        this.orderId = orderId;
        this.products = products;
        this.customerName = customerName;
    }

    //<editor-fold desc="Getters">
    public String getOrderId() {
        return orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getCustomerName() {
        return customerName;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", products=" + products +
                ", customerName='" + customerName + '\'' +
                '}';
    }

    // Method to calculate total amount of order
    public double getTotalAmount() {
        return products.stream()// Stream the list of products
                .mapToDouble(Product::getPrice) // Extract the price of each product and convert it to a stream of doubles
                .sum(); // Sum up all the prices
    }

    public int getProductCount() {
        return products != null ? products.size() : 0; // Return number of products in order. If no products are present, return 0
    }


}

