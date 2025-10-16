package com.myapp.repository;
import java.util.*;
import com.myapp.model.Order;
import com.myapp.util.LoggerUtil;

public class OrderRepository {
    // En lista som innehåller alla ordrar.
    private final List<Order> orders = new ArrayList<>();

    // Den här mappen används för att gruppera ordrar per kund.
    // Nyckeln är kundens namn, och värdet är en lista med alla ordrar från den kunden.
    // Det gör det enkelt att hämta alla ordrar för en specifik kund utan att behöva filtrera hela listan varje gång.
    private final Map<String, List<Order>> customerOrdersMap = new HashMap<>();

    // Metoden lägger till en ny order.
    // Använda loggerUtil för att logga när en order har lagts till.
    public void addOrder(Order order) {
        this.orders.add(order);
        this.customerOrdersMap.computeIfAbsent(order.getCustomerName(), k -> new ArrayList<>()).add(order);
        LoggerUtil.logInfo("Order med ID: " + order.getOrderId() + "har lagts till kund: " + order.getCustomerName());
        System.out.flush(); // Tömmer bufferten för att säkerställa ordning
        System.out.println("*** Order har tillagd! ***"); // Visas direkt till användaren
    }

    // Metoden hämtar alla ordrar.
    public List<Order> getAllOrders() {
        return this.orders;
    }

    // Metoden hämtar alla ordrar för en specifik kund.
    public List<Order> getOrdersByCustomer(String customerName) {
        return this.customerOrdersMap.getOrDefault(customerName, Collections.emptyList());
    }
}
