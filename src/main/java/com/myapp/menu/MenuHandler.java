package com.myapp.menu;

import com.myapp.exception.*;
import com.myapp.model.Product;
import com.myapp.model.Order;
import com.myapp.service.ShopService;
import com.myapp.service.AnalyticsService;
import com.myapp.util.LoggerUtil;

import java.util.*;

// MenuHandler ansvarar för att hantera användarens menyval och utföra motsvarande logik.
//Klassen kommunicerar med ShopService och AnalyticsService för att hantera produkter och ordrar.
//Den använder Scanner för att läsa in användarens input från konsolen.
public class MenuHandler {
    private final ShopService shopService;
    private final AnalyticsService analyticsService;
    private final Scanner scanner;

    //Konstruktor som tar emot tjänster och scanner för användarinteraktion.
    public MenuHandler(ShopService shopService, AnalyticsService analyticsService, Scanner scanner) {
        this.shopService = shopService;
        this.analyticsService = analyticsService;
        this.scanner = scanner;
    }

    // Lägger till en ny produkt genom att fråga användaren om namn, kategori och pris.
    // Validerar priset och hanterar fel om formatet är fel eller priset är ogiltigt.
    public void addProduct() {
        System.out.print("Ange produktnamn: ");
        String name = scanner.nextLine();
        System.out.print("Ange kategori: ");
        String category = scanner.nextLine();
        System.out.print("Ange pris: ");

        double price;
        try {
            price = Double.parseDouble(scanner.nextLine()); // Försöker konvertera priset till double
            shopService.createAndAddProduct(name, category, price);// Skapar och lägger till produkten via ShopService
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt prisformat."); // Om användaren skriver något som inte är ett tal
        } catch (InvalidPriceException e) {
            System.out.println(e.getMessage()); // Om priset är negativt eller noll
        }
    }

     //Skapar en ny order genom att låta användaren välja produkter.
     // Hämtar alla produkter, låter användaren välja via index, och skapar ordern.
    public void addOrder() {
        List<Product> allProducts;
        try {
            allProducts = shopService.getAllProducts(); // Hämtar alla produkter, kan kasta undantag om listan är tom
        } catch (NoProductsFoundException e) {
            System.out.println(e.getMessage());
            return; // Avbryt metoden om inga produkter finns
        }

        System.out.print("Ange kundnamn: ");
        String customerName = scanner.nextLine();

        // Visar alla produkter med index så användaren kan välja produkterna
        List<Product> selectedProducts = new ArrayList<>();
        System.out.println("Välj produkt nummer (skriv 'klar' när du är färdig):");
        for (int i = 0; i < allProducts.size(); i++) {
            System.out.println(i + ": " + allProducts.get(i).getName() + " - " + allProducts.get(i).getPrice() + " kr");
        }

        // Loop för att låta användaren välja flera produkter
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("klar")) break;
            try {
                int index = Integer.parseInt(input);
                selectedProducts.add(allProducts.get(index));
            } catch (Exception e) {
                System.out.println("Ogiltigt val."); // Felaktigt index eller format
            }
        }

        try {
            // Skapar ordern med valda produkter
            shopService.createAndAddOrder(selectedProducts, customerName);
        } catch (EmptyOrderException e) {
            System.out.println(e.getMessage()); // Om användaren inte valde några produkter
        }
    }

    // Visar alla produkter i systemet.
    //Hanterar fallet där inga produkter finns.
    public void showAllProducts() {
        try {
            List<Product> products = shopService.getAllProducts();
            LoggerUtil.logInfo("Hämtar alla produkter...............");
            System.out.println("Lista över alla produkter i systemet:");
            System.out.printf("%-10s %-25s %-10s %-10s%n", "ProduktID", "Namn", "Kategori", "Pris");
            System.out.println("---------------------------------------------------------------");
            for (Product product : products) {
                System.out.printf("%-10s %-25s %-10s %-10.2f%n",
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice());
            }

        } catch (NoProductsFoundException e) {
            System.out.println( e.getMessage());
        }
    }

     // Visar alla ordrar i systemet.
     // Hanterar fallet där inga ordrar finns.
     public void showAllOrders() {
         try {
             List<Order> orders = shopService.getAllOrders();
             LoggerUtil.logInfo("Hämtar alla ordrar...............");
             System.out.println("Lista över alla ordrar i systemet:\n");
             System.out.printf("%-15s %-20s %-10s %-10s%n", "Order-ID", "Kundnamn", "Antal", "Totalt");
             System.out.println("---------------------------------------------------------------");
             for (Order order : orders) {
                 System.out.printf("%-15s %-20s %-10d %-10.2f%n",
                     order.getOrderId(),
                     order.getCustomerName(),
                     order.getProducts().size(),
                     order.getTotalAmount());
             }

         } catch (NoOrdersFoundException e) {
             System.out.println(e.getMessage());
         }
     }

    // Visar alla ordrar för en viss kund.
    // Hanterar fallet där kunden inte har några ordrar.
    public void showOrdersByCustomer() {
        System.out.print("Ange kundnamn: ");
        String name = scanner.nextLine();

        try {
            List<Order> orders = shopService.getOrdersByCustomer(name);
            System.out.println("\n Ordrar för kund: " + name + "\n");
            System.out.printf("%-15s %-15s %-10s%n", "Order-ID", "Antal produkter", "Totalt amount");
            System.out.println("----------------------------------------");

            for (Order order : orders) {
                System.out.printf("%-15s %-15d %-10.2f%n",
                        order.getOrderId(),
                        order.getProductCount(),
                        order.getTotalAmount());
            }

        } catch (NoOrdersForCustomerException e) {
            System.out.println( e.getMessage());
        }
    }

     // Visar produkter i en viss kategori, sorterade efter pris.
     // Hanterar fallet där inga produkter finns i kategorin.
    public void showProductsByCategorySorted() {
        System.out.print("Ange kategori: ");
        String category = scanner.nextLine();

        try {
            List<Product> sorted = analyticsService.getProductsByCategorySorted(category);

            System.out.println("\n Produkter i kategori: " + category + "\n");
            System.out.printf("%-10s %-25s %-10s %-10s%n", "ProduktID", "Namn", "Kategori", "Pris");
            System.out.println("---------------------------------------------------------------");

            for (Product p : sorted) {
                System.out.printf("%-10s %-25s %-10s %-10.2f%n",
                        p.getId(),
                        p.getName(),
                        p.getCategory(),
                        p.getPrice());
            }

        } catch (NoProductsInCategoryException e) {
            System.out.println(e.getMessage());
        }
    }

     // Visar hur mycket en kund har spenderat totalt.
     // Hanterar fallet där kunden inte har några ordrar.
    public void showTotalSpentByCustomer() {
        System.out.print("Ange kundnamn: ");
        String name = scanner.nextLine();

        try {
            double total = analyticsService.CalculateTotalValueOfCustomer(name);
            System.out.println("\n Totalt spenderat av kund \"" + name + "\" är " + "\""+ total +"\" kr");
        }
        catch (NoOrdersForCustomerException e) {
            System.out.println(e.getMessage());
        }
    }

    // Visar de tre mest köpta produkterna totalt.
    // Hanterar fallet där inga ordrar finns.
    public void showTopThreeProducts() {
        try {
            List<String> top = analyticsService.getTopThreeMostPurchasedProducts(shopService.getAllOrders());

            System.out.println("\nTopp 3 mest köpta produkter:\n");
            System.out.printf("%-5s %-30s%n", "Nr", "Produktnamn");
            System.out.println("----------------------------------------");

            for (int i = 0; i < top.size(); i++) {
                System.out.printf("%-5d %-30s%n", i + 1, top.get(i));
            }

        } catch (NoOrdersFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
