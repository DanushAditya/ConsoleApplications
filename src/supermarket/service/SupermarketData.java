package supermarket.service;

import supermarket.models.Product;
import supermarket.models.User;
import supermarket.models.Bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupermarketData {
    private HashMap<String, Product> products;
    private HashMap<String, User> users; // Key: Email
    private HashMap<String, List<Bill>> userBills; // Key: User Email, Value: List of Bills

    public SupermarketData() {
        this.products = new HashMap<>();
        this.users = new HashMap<>();
        this.userBills = new HashMap<>();
        
        // Seed initial mock data
        User admin = new User("admin@supermarket.com", "admin123", "Super Admin", "admin");
        users.put(admin.getEmail(), admin);
        
        User customer = new User("customer@supermarket.com", "pass123", "John Doe", "customer");
        users.put(customer.getEmail(), customer);
        userBills.put(customer.getEmail(), new ArrayList<>());
        
        products.put("P001", new Product("P001", "Apple 1kg", 200.0, 50));
        products.put("P002", new Product("P002", "Whole Wheat Bread", 50.0, 30));
        products.put("P003", new Product("P003", "Milk 1L", 65.0, 100));
        products.put("P004", new Product("P004", "Premium Rice 5kg", 450.0, 10));
    }

    public HashMap<String, Product> getProducts() { return products; }
    public HashMap<String, User> getUsers() { return users; }
    public HashMap<String, List<Bill>> getUserBills() { return userBills; }
    
    public void addBillForUser(String email, Bill bill) {
        userBills.putIfAbsent(email, new ArrayList<>());
        userBills.get(email).add(bill);
    }
}
