package com.myapp;

import com.myapp.menu.MenuHandler;
import com.myapp.service.ShopService;
import com.myapp.service.AnalyticsService;
import com.myapp.repository.ProductRepository;
import com.myapp.repository.OrderRepository;
import com.myapp.util.LoggerUtil;

import java.util.Scanner;

public class MainApp {
    // Deklaration av variabler och instansiering av objekten.
    // Deklarerar en statisk final Scanner, ProductRepository, OrderRepository, ShopService, AnalyticsService och MenuHandler.
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProductRepository productRepository = new ProductRepository();
    private static final OrderRepository orderRepository = new OrderRepository();
    private static final ShopService shopService = new ShopService(productRepository, orderRepository);
    private static final AnalyticsService analyticsService = new AnalyticsService(productRepository, orderRepository);
    private static final MenuHandler menuHandler = new MenuHandler(shopService, analyticsService, scanner);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            // Anropar metoden printMenu() på MenuHandler-klassen för att visa menyn.
                try {
                    printMenu();
                }
                catch(InterruptedException e){ // Hantera eventuella undantag som kan uppstå vid sleep-anropen
                    System.out.println(e.getMessage());
                }

            // Läser in användarens val från tangentbordet och anropar den motsvarande metoden på MenuHandler-klassen.
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> menuHandler.addProduct();
                case "2" -> menuHandler.addOrder();
                case "3" -> menuHandler.showAllProducts();
                case "4" -> menuHandler.showAllOrders();
                case "5" -> menuHandler.showOrdersByCustomer();
                case "6" -> menuHandler.showProductsByCategorySorted();
                case "7" -> menuHandler.showTotalSpentByCustomer();
                case "8" -> menuHandler.showTopThreeProducts();
                case "9" -> running = false;
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
        LoggerUtil.logInfo("du loggar ut nu från programmet");
        System.out.println("Programmet avslutas. Tack för att du använde systemet!\n");
        scanner.close(); // // Stänger ner Scanner när programmet har avslutats.
    }
    public static void printMenu() throws InterruptedException{
        System.out.flush();// Tömmer bufferten för att säkerställa att allt skrivna text visas innan nästa rad skrivs ut.
        System.out.println("\n"); // Skriver ut en tom rad för att separera menyn från tidigare utskrift.
        Thread.sleep(100); // Väntar några millisekunder för att se till att utskriften syns korrekt.
        System.out.println("--------Välkommen till butiken!----------------");
        System.out.println("Välj ett alternativ:");
        System.out.println("1. Lägg till produkt");
        System.out.println("2. Lägg till order");
        System.out.println("3. Visa alla produkter");
        System.out.println("4. Visa alla ordrar");
        System.out.println("5. Visa ordrar för kund");
        System.out.println("6. Visa produkter i kategori (sorterade)");
        System.out.println("7. Visa total spenderat av kund");
        System.out.println("8. Visa topp 3 mest köpta produkter");
        System.out.println("9. Avsluta");
    }
}
