package supermarket.service;

import supermarket.models.Product;
import supermarket.models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminService {
    private SupermarketData data;

    public AdminService(SupermarketData data) {
        this.data = data;
    }

    // Module B: Inventory Management
    public void addProduct(String productId, String name, double price, int quantity) {
        if (data.getProducts().containsKey(productId)) {
            System.out.println("Product with ID " + productId + " already exists.");
            return;
        }
        data.getProducts().put(productId, new Product(productId, name, price, quantity));
        System.out.println("Product Added Successfully.");
    }

    public void modifyProduct(String productId, String newName, double newPrice, int newQuantity) {
        Product p = data.getProducts().get(productId);
        if (p != null) {
            p.setName(newName);
            p.setPrice(newPrice);
            p.setQuantity(newQuantity);
            System.out.println("Product Modified Successfully.");
        } else {
            System.out.println("Product Not Found.");
        }
    }

    public void deleteProduct(String productId) {
        if (data.getProducts().remove(productId) != null) {
            System.out.println("Product Deleted Successfully.");
        } else {
            System.out.println("Product Not Found.");
        }
    }

    public void addUser(String email, String password, String name, String role) {
        if (data.getUsers().containsKey(email)) {
            System.out.println("User exists with this Email.");
            return;
        }
        data.getUsers().put(email, new User(email, password, name, role));
        System.out.println(role.substring(0, 1).toUpperCase() + role.substring(1) + " Added Successfully.");
    }

    public void viewAllProducts(String sortBy) {
        List<Product> products = new ArrayList<>(data.getProducts().values());
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        if (sortBy.equalsIgnoreCase("name")) {
            products.sort(Comparator.comparing(Product::getName));
        } else if (sortBy.equalsIgnoreCase("price")) {
            products.sort(Comparator.comparingDouble(Product::getPrice));
        }

        System.out.println("--- All Products ---");
        for (Product p : products) {
            p.display();
        }
    }

    public void searchProductByName(String nameQuery) {
        boolean found = false;
        System.out.println("--- Search Results ---");
        for (Product p : data.getProducts().values()) {
            if (p.getName().toLowerCase().contains(nameQuery.toLowerCase())) {
                p.display();
                found = true;
            }
        }
        if (!found) System.out.println("No matching products found.");
    }

    public void increaseCustomerCredit(String email, double amount) {
        User u = data.getUsers().get(email);
        if (u != null && u.getRole().equals("customer")) {
            u.addWalletBalance(amount);
            System.out.println("Credit increased. New Balance: Rs " + u.getWalletBalance());
        } else {
            System.out.println("Customer Not Found.");
        }
    }

    // Module F: Reports
    public void reportLowQuantity() {
        System.out.println("--- Products with Less Quantity (< 5) ---");
        boolean found = false;
        for (Product p : data.getProducts().values()) {
            if (p.getQuantity() < 5) {
                p.display();
                found = true;
            }
        }
        if (!found) System.out.println("No low quantity products.");
    }

    public void reportNotBought() {
        System.out.println("--- Products Never Bought ---");
        boolean found = false;
        for (Product p : data.getProducts().values()) {
            if (p.getSoldQuantity() == 0) {
                p.display();
                found = true;
            }
        }
        if (!found) System.out.println("All products have been bought at least once.");
    }

    public void reportHighValueCustomers() {
        System.out.println("--- Customers Sorted by Total Spent ---");
        List<User> customers = new ArrayList<>();
        for (User u : data.getUsers().values()) {
            if (u.getRole().equals("customer")) {
                customers.add(u);
            }
        }
        customers.sort(Comparator.comparingDouble(User::getTotalSpent).reversed());
        for (User u : customers) {
            if(u.getTotalSpent() > 0) {
                System.out.printf("Name: %-15s | Email: %-20s | Total Spent: Rs %.2f\n", 
                                  u.getName(), u.getEmail(), u.getTotalSpent());
            }
        }
    }

    public void reportTopSellingAdmins() {
        System.out.println("--- Admins Sorted by Total Sales Made ---");
        List<User> admins = new ArrayList<>();
        for (User u : data.getUsers().values()) {
            if (u.getRole().equals("admin")) {
                admins.add(u);
            }
        }
        admins.sort(Comparator.comparingDouble(User::getTotalSalesMade).reversed());
        for (User u : admins) {
            System.out.printf("Name: %-15s | Email: %-20s | Total Sales Made: Rs %.2f\n", 
                              u.getName(), u.getEmail(), u.getTotalSalesMade());
        }
    }
}
