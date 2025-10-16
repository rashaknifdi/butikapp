package com.myapp.factory;

import com.myapp.model.Product;
import java.util.UUID;

public class ProductFactory {

    // Create a new product with the given parameters
    // The product ID is generated randomly By using UUID.
    public static Product createProduct( String name, String category, Double price) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        return new Product(id,name,category,price);
    }
}
