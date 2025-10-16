package com.myapp.service;

import com.myapp.exception.NoOrdersForCustomerException;
import com.myapp.exception.NoOrdersFoundException;
import com.myapp.exception.NoProductsInCategoryException;
import com.myapp.model.Order;
import com.myapp.model.Product;
import com.myapp.repository.OrderRepository;
import com.myapp.repository.ProductRepository;
import com.myapp.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public AnalyticsService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    // Stream-pipeline:
    // - filter → väljer produkter i rätt kategori.
    // - sorted → sorterar produkterna efter pris (stigande).
    // - collect → samlar resultatet i en lista.
    // Motivering:
    // Vi filtrerar först på kategori för att bara få relevanta produkter.
    // Sedan sorterar vi efter pris så att användaren ser de billigaste först.
    // Resultatet blir en tydlig och användbar lista.
    public List<Product> getProductsByCategorySorted(String category) throws NoProductsInCategoryException {
        List<Product> products = productRepository.getProductsByCategory(category);
        if (products.isEmpty()) {
            LoggerUtil.logError("Det gick inte att visa produkter i kategorin \"" + category + "\"");
            throw new NoProductsInCategoryException("Det finns inga produkter i den här kategorin \"");
        }
        List<Product> productsSorted = products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());

        LoggerUtil.logInfo("Hämtar produkter i kategorin: " + category+"  (sorterade efter pris) stigande...............");
        return productsSorted;
    }

    // Stream-pipeline:
    // - flatMap → plattar ut alla produktlistor från kundens ordrar.
    // - mapToDouble → hämtar priset från varje produkt.
    // - sum → summerar alla priser.
    //
    // Motivering:
    // Vi samlar alla produkter från kundens ordrar i en ström.
    // Sedan extraherar vi priserna och summerar dem.
    // Det ger oss det totala beloppet kunden har spenderat.
    public double CalculateTotalValueOfCustomer(String customerName) throws NoOrdersForCustomerException {
        List<Order> orders = orderRepository.getOrdersByCustomer(customerName);
        if (orders.isEmpty()) {
            LoggerUtil.logError("Beräkning av spenderad summa misslyckades för kunden \"" + customerName + "\".");
            throw new NoOrdersForCustomerException("Det finns inga ordrar för kunden :" + customerName + "\".");
        }
        double total = orders.stream()
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();

        LoggerUtil.logInfo("Hämtar totalt värde spenderat för kund............... ");
        return total;
    }

    // Stream-pipeline:
    // - flatMap → samlar alla produkter från alla ordrar.
    // - groupingBy + counting → räknar hur många gånger varje produkt köpts.
    // - sorted → sorterar produkterna efter antal (mest köpta först).
    // - limit → tar bara de tre mest köpta.
    // - map → hämtar produktnamnen.
    //- collect → samlar resultatet i en lista.
    //
    // Motivering:
    // Vi analyserar alla produkter som köpts och räknar hur ofta varje förekommer.
    // Sedan sorterar vi dem och tar de tre mest populära.
    // Resultatet är en lista med de mest köpta produkterna.
    public List<String> getTopThreeMostPurchasedProducts(List<Order> orders) throws NoOrdersFoundException {
        if (orders == null || orders.isEmpty()) {
            LoggerUtil.logError("Det gick inte att visa de tre mest köpta produkterna");
            throw new NoOrdersFoundException("Det finns inga ordrar att analysera.");
        }
        List<String> topThreeProducts = orders.stream()
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        LoggerUtil.logInfo("Hämtar de tre mest köpta produkterna.................." );
        return topThreeProducts;
    }


}
