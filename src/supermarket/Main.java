package supermarket;

import supermarket.models.User;
import supermarket.service.AdminService;
import supermarket.service.CustomerService;
import supermarket.service.SupermarketData;

import java.util.Scanner;

public class Main {
    static SupermarketData data = new SupermarketData();
    static AdminService adminService = new AdminService(data);
    static CustomerService customerService = new CustomerService(data);
    static Scanner sc = new Scanner(System.in);

    // Initial Login Credentials:
    // Admin: admin@supermarket.com | admin123
    // Customer: customer@supermarket.com | pass123

    public static void main(String[] args) {
        System.out.println("====== Welcome to Supermarket Billing System ======");

        while (true) {
            System.out.println("\n--- Login ---");
            System.out.print("Enter Email    : ");
            String email = sc.nextLine();
            System.out.print("Enter Password : ");
            String password = sc.nextLine();

            User user = data.getUsers().get(email);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println("Login Successful! Welcome, " + user.getName());
                if (user.getRole().equals("admin")) {
                    adminMenu();
                } else if (user.getRole().equals("customer")) {
                    customerMenu(user);
                }
            } else {
                System.out.println("Invalid Credentials! Try again.");
            }

            System.out.print("\nExit System? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Thank you for shopping with us. Goodbye!");
                break;
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n*** Administrator Portal ***");
            System.out.println("1. Add a Product");
            System.out.println("2. Modify a Product");
            System.out.println("3. Delete a Product");
            System.out.println("4. Register User (Admin/Customer)");
            System.out.println("5. View All Products");
            System.out.println("6. Search a Product");
            System.out.println("7. Increase Customer Credit");
            System.out.println("8. View Reports");
            System.out.println("9. Logout");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 9) break;

                switch (choice) {
                    case 1:
                        System.out.print("Enter ID: "); String id = sc.nextLine();
                        System.out.print("Enter Name: "); String name = sc.nextLine();
                        System.out.print("Enter Price: "); double price = Double.parseDouble(sc.nextLine());
                        System.out.print("Enter Quantity: "); int qty = Integer.parseInt(sc.nextLine());
                        adminService.addProduct(id, name, price, qty);
                        break;
                    case 2:
                        System.out.print("Enter ID to Modify: "); String mId = sc.nextLine();
                        System.out.print("Enter New Name: "); String mName = sc.nextLine();
                        System.out.print("Enter New Price: "); double mPrice = Double.parseDouble(sc.nextLine());
                        System.out.print("Enter New Quantity: "); int mQty = Integer.parseInt(sc.nextLine());
                        adminService.modifyProduct(mId, mName, mPrice, mQty);
                        break;
                    case 3:
                        System.out.print("Enter ID to Delete: "); String dId = sc.nextLine();
                        adminService.deleteProduct(dId);
                        break;
                    case 4:
                        System.out.print("Enter Email: "); String uEmail = sc.nextLine();
                        System.out.print("Enter Password: "); String uPass = sc.nextLine();
                        System.out.print("Enter Name: "); String uName = sc.nextLine();
                        System.out.print("Enter Role (admin/customer): "); String uRole = sc.nextLine();
                        adminService.addUser(uEmail, uPass, uName, uRole);
                        break;
                    case 5:
                        System.out.print("Sort By (name/price): "); String sort = sc.nextLine();
                        adminService.viewAllProducts(sort);
                        break;
                    case 6:
                        System.out.print("Enter Search Term (Name): "); String query = sc.nextLine();
                        adminService.searchProductByName(query);
                        break;
                    case 7:
                        System.out.print("Enter Customer Email: "); String cEmail = sc.nextLine();
                        System.out.print("Enter Amount to Add: "); double cCredit = Double.parseDouble(sc.nextLine());
                        adminService.increaseCustomerCredit(cEmail, cCredit);
                        break;
                    case 8:
                        adminReportsMenu();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Input error. Please try again.");
            }
        }
    }

    private static void adminReportsMenu() {
        System.out.println("\n-- Admin Reports --");
        System.out.println("1. Products with Less Quantity (<5)");
        System.out.println("2. Never Bought Products");
        System.out.println("3. High Value Customers");
        System.out.println("4. Top Selling Admins");
        System.out.print("Select a report: ");
        int choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
            case 1: adminService.reportLowQuantity(); break;
            case 2: adminService.reportNotBought(); break;
            case 3: adminService.reportHighValueCustomers(); break;
            case 4: adminService.reportTopSellingAdmins(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void customerMenu(User user) {
        while (true) {
            System.out.println("\n*** Customer Portal ***");
            System.out.println("1. View Available Products");
            System.out.println("2. Start Shopping / Manage Cart / Checkout");
            System.out.println("3. View Purchase history");
            System.out.println("4. View Profile details (Points, Credit)");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 5) break;

                switch (choice) {
                    case 1: customerService.viewAvailableProducts(); break;
                    case 2: customerService.startShopping(user); break;
                    case 3: customerService.viewPurchaseHistory(user); break;
                    case 4: user.display(); break;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Input error. Please try again.");
            }
        }
    }
}
