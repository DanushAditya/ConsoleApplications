package supermarket.service;

import supermarket.models.Product;
import supermarket.models.User;
import supermarket.models.CartItem;
import supermarket.models.Bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerService {
    private SupermarketData data;
    private Scanner sc = new Scanner(System.in);
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CustomerService(SupermarketData data) {
        this.data = data;
    }

    public void viewAvailableProducts() {
        System.out.println("--- Available Products ---");
        for (Product p : data.getProducts().values()) {
            if (p.getQuantity() > 0) {
                p.display();
            }
        }
    }

    public void startShopping(User customer) {
        List<CartItem> cart = new ArrayList<>();
        boolean shopping = true;

        while (shopping) {
            System.out.println("\n-- Shopping Cart Menu --");
            System.out.println("1. Add Product to Cart");
            System.out.println("2. Edit Cart Item Quantity");
            System.out.println("3. Delete Item from Cart");
            System.out.println("4. View Cart");
            System.out.println("5. Proceed to Payment");
            System.out.println("6. Cancel Shopping");
            System.out.print("Select an option: ");
            
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    String pid = sc.nextLine();
                    Product p = data.getProducts().get(pid);
                    if (p == null || p.getQuantity() <= 0) {
                        System.out.println("Invalid Product or Out of Stock.");
                        break;
                    }
                    System.out.print("Enter Quantity: ");
                    int qty = Integer.parseInt(sc.nextLine());
                    if (qty > p.getQuantity()) {
                        System.out.println("Insufficient stock. Only " + p.getQuantity() + " available.");
                        break;
                    }
                    
                    // Check if already in cart
                    boolean found = false;
                    for (CartItem item : cart) {
                        if (item.getProduct().getProductId().equals(pid)) {
                            if (item.getCartQuantity() + qty > p.getQuantity()) {
                                System.out.println("Cannot add more. Exceeds available stock.");
                            } else {
                                item.setCartQuantity(item.getCartQuantity() + qty);
                                System.out.println("Cart updated.");
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        cart.add(new CartItem(p, qty));
                        System.out.println("Product added to cart.");
                    }
                    break;
                case 2:
                    viewCart(cart);
                    if (cart.isEmpty()) break;
                    System.out.print("Enter Product ID to Edit: ");
                    String eId = sc.nextLine();
                    for (CartItem item : cart) {
                        if (item.getProduct().getProductId().equals(eId)) {
                            System.out.print("Enter new quantity: ");
                            int newQty = Integer.parseInt(sc.nextLine());
                            if (newQty > item.getProduct().getQuantity()) {
                                System.out.println("Insufficient stock.");
                            } else if (newQty <= 0) {
                                cart.remove(item);
                                System.out.println("Item removed from cart.");
                            } else {
                                item.setCartQuantity(newQty);
                                System.out.println("Quantity updated.");
                            }
                            break;
                        }
                    }
                    break;
                case 3:
                    viewCart(cart);
                    if (cart.isEmpty()) break;
                    System.out.print("Enter Product ID to Remove: ");
                    String dId = sc.nextLine();
                    cart.removeIf(item -> item.getProduct().getProductId().equals(dId));
                    System.out.println("Removed if it existed.");
                    break;
                case 4:
                    viewCart(cart);
                    break;
                case 5:
                    if (cart.isEmpty()) {
                        System.out.println("Cart is empty.");
                        break;
                    }
                    shopping = !processPayment(customer, cart);
                    break;
                case 6:
                    System.out.println("Shopping Cancelled.");
                    shopping = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void viewCart(List<CartItem> cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("--- Current Cart ---");
        double total = 0;
        for (CartItem item : cart) {
            item.display();
            total += item.getTotalPrice();
        }
        System.out.printf("Estimated Subtotal: Rs %.2f\n", total);
    }

    private boolean processPayment(User customer, List<CartItem> cart) {
        double subtotal = 0;
        for (CartItem item : cart) {
            subtotal += item.getTotalPrice();
        }

        if (subtotal > customer.getWalletBalance()) {
            System.out.printf("Insufficient Credit Limit. Required: Rs %.2f | Available: Rs %.2f\n", subtotal, customer.getWalletBalance());
            System.out.println("Please remove some items or ask Admin to increase credit.");
            return false;
        }

        double discount = 0;
        int pointsEarned = 0;

        // Apply rules
        // a) If 5000 rupees spent in a bill we will discount 100 Rs [...] and not add any reward points
        if (subtotal >= 5000) {
            discount = 100;
            System.out.println("Offer Applied! Rs 100 discount on bills over Rs 5000.");
        } else {
            // b) Every 100 rupees spent adds 1 Point. If score reaches 50, give Rs 100 discount next immediate bill.
            // Accumulate first
            pointsEarned = (int) (subtotal / 100);
            
            // Apply loyalty discount if previously accumulated 50+
            if (customer.getLoyaltyPoints() >= 50) {
                discount = 100;
                customer.deductLoyaltyPoints(50);
                System.out.println("Loyalty Offer Applied! Rs 100 discount for 50 accumulated points.");
            }
        }

        double finalAmount = subtotal - discount;
        if (finalAmount < 0) finalAmount = 0; // Edge case safeguard

        // Execute transactions
        customer.deductWalletBalance(finalAmount);
        customer.addLoyaltyPoints(pointsEarned);
        customer.addTotalSpent(finalAmount);

        // Update inventory and find admin to award sales to (we'll just use the first admin found)
        for (CartItem item : cart) {
            Product p = item.getProduct();
            p.reduceQuantity(item.getCartQuantity());
            p.increaseSoldQuantity(item.getCartQuantity());
        }

        grantAdminSales(finalAmount);

        // Generate Bill
        String billId = "BLL" + System.currentTimeMillis() % 10000;
        String date = dtf.format(LocalDate.now());
        
        // Deep copy items for the bill to freeze state
        List<CartItem> billItems = new ArrayList<>(cart);
        Bill bill = new Bill(billId, date, billItems, finalAmount, pointsEarned, discount);
        
        data.addBillForUser(customer.getEmail(), bill);

        System.out.println("\nPayment Successful!");
        bill.display();
        
        return true; // Shopping done
    }

    private void grantAdminSales(double amount) {
        // Find any admin to increment their totalSalesMade for report F.d
        for (User u : data.getUsers().values()) {
            if (u.getRole().equals("admin")) {
                u.addTotalSalesMade(amount);
                break; // Just adding to first admin found for simplicity
            }
        }
    }

    public void viewPurchaseHistory(User customer) {
        List<Bill> bills = data.getUserBills().get(customer.getEmail());
        if (bills == null || bills.isEmpty()) {
            System.out.println("No purchase history found.");
            return;
        }

        System.out.println("--- Purchase History ---");
        for (Bill b : bills) {
            b.display();
            System.out.println();
        }
    }
}
