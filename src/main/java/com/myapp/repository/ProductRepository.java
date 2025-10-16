package com.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.myapp.model.Product;
import com.myapp.util.LoggerUtil;

public class ProductRepository {
    // En lista som innehåller alla produkter.
    private final List<Product> products = new ArrayList<>();

    // Metoden lägger till en produkt i listan.
    // Använda LoggerUtil för att logga när en produkt har lagts till.
    public void addProduct(Product product) {
        this.products.add(product);
        // Använda LoggerUtil för att logga när en produkt har lagts till.
        LoggerUtil.logInfo("Produkt: " + product.getName() + ", har tillagd: " + "i kategorin: " + product.getCategory()+", med priset: "+ product.getPrice() + " kr" );
        System.out.flush(); // Tömmer bufferten för att säkerställa ordning
        System.out.println("*** Produkten har tillagd! ***"); // Visas direkt till användaren
    }

    // Metoden returnerar en lista med alla produkter.
    public List<Product> getAllProducts() {
        return this.products;
    }

    // Metoden returnerar en lista med produkterna i en viss kategori.
    // Använda Stream API för att filtrera listan.
    public List<Product> getProductsByCategory(String category){
        return this.products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

}
