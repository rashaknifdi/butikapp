package com.myapp.service;
import com.myapp.exception.*;
import com.myapp.model.Order;
import com.myapp.model.Product;
import com.myapp.repository.OrderRepository;
import com.myapp.repository.ProductRepository;
import com.myapp.factory.ProductFactory;
import com.myapp.factory.OrderFactory;
import com.myapp.util.LoggerUtil;

import java.util.Collections;
import java.util.List;


public class ShopService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ShopService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    // Metoden för att skapa och lägga till produkter.
    // Kollar om priset är negativt och kastar ett undantag om det är så.
    // Om priset är positivt, skapas och lagras produkt.
    // Loggar när produkten har lagts till.
    public void createAndAddProduct(String name, String category, double price) throws InvalidPriceException {
        if (price < 0) {
            LoggerUtil.logError("Felaktigt pris: " + price);
            throw new InvalidPriceException("Pris får inte vara negativt.");
        }
        productRepository.addProduct(ProductFactory.createProduct(name, category, price));
    }

    // Metoden för att skapa och lägga till en order.
    // Kollar om ordern är tom och kastar ett undantag om det är så.
    // Om ordern inte är tom, skapas och lagras order.
    // Loggar när ordern har lagts till.
    public void createAndAddOrder(List<Product> products, String customerName) throws EmptyOrderException {
        if (products.isEmpty()) {
            LoggerUtil.logError("Tom order för kund: " + customerName);
            throw new EmptyOrderException("Ordern är tom.");
        }
        orderRepository.addOrder(OrderFactory.createOrder(products, customerName));
    }

    // Metoden för att hämta produkter av en viss kategori sorterade efter pris.
    // Kollar om produkterna är tomma och kastar ett undantag om det är så.
    public List<Product> getAllProducts() throws NoProductsFoundException {
        List<Product> products = productRepository.getAllProducts();
        if (products.isEmpty()) {
            LoggerUtil.logError("Inga produkter hittades.");
            throw new NoProductsFoundException("Det finns inga produkter i systemet.");
        }
        return products;
    }

    // Metoden för att hämta produkter av en viss kategori sorterade efter pris.
    // Kollar om produkterna är tomma och kastar ett undantag om det är så.
    public List<Order> getAllOrders() throws NoOrdersFoundException {
        List<Order> orders = orderRepository.getAllOrders();
        if (orders.isEmpty()) {
            LoggerUtil.logError("Inga ordrar hittades.");
            throw new NoOrdersFoundException("Det finns inga ordrar i systemet.");
        }
        return orders;
    }

    // Metoden för att hämta lista av order av en kund.
    public List<Order> getOrdersByCustomer(String customerName) throws NoOrdersForCustomerException {
        List<Order> orders = orderRepository.getOrdersByCustomer(customerName);
        if (orders.isEmpty()) {
            LoggerUtil.logError("Inga ordrar hittades för kund: " + customerName);
            throw new NoOrdersForCustomerException("Det finns inga ordrar för kunden \"" + customerName + "\".");
        }
        LoggerUtil.logInfo("Hämtar ordrar för kund: " + customerName +".................");
        return orders;
    }

}


