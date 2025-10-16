package com.myapp.factory;
import java.util.List;
import com.myapp.model.Order;
import com.myapp.model.Product;
import java.util.UUID;

public class OrderFactory {

    // Create an order with the given products and customer name.
    // The order ID is generated randomly By using UUID.
    public static Order createOrder(List<Product> products, String customerName) {
        String orderId = UUID.randomUUID().toString().substring(0, 8);;
        return new Order(orderId, products, customerName);
    }
}
