package library;

import library.models.User;
import library.service.AdminService;
import library.service.BorrowerService;
import library.service.LibraryData;

import java.util.Scanner;

public class Main {
    static LibraryData data = new LibraryData();
    static AdminService adminService = new AdminService(data);
    static BorrowerService borrowerService = new BorrowerService(data);
    static Scanner sc = new Scanner(System.in);
    // Initial Login Credentials:
    // Admin: Email admin@lib.com | Pass admin123
    // Borrower: Email student@lib.com | Pass pass123
    public static void main(String[] args) {
        System.out.println("====== Welcome to Library Management System ======");

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
                } else if (user.getRole().equals("borrower")) {
                    borrowerMenu(user);
                }
            } else {
                System.out.println("Invalid Credentials! Try again.");
            }

            System.out.print("\nExit System? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Thank you for using Library Management System. Goodbye!");
                break;
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n*** Administrator Portal ***");
            System.out.println("1. Add a Book");
            System.out.println("2. Modify Book Quantity");
            System.out.println("3. Delete a Book");
            System.out.println("4. Register a User (Admin/Borrower)");
            System.out.println("5. View All Books");
            System.out.println("6. Search a Book");
            System.out.println("7. View Reports");
            System.out.println("8. Logout");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 8) break;

                switch (choice) {
                    case 1:
                        System.out.print("Enter ISBN: "); String isbn = sc.nextLine();
                        System.out.print("Enter Name: "); String name = sc.nextLine();
                        System.out.print("Enter Author: "); String author = sc.nextLine();
                        System.out.print("Enter Quantity: "); int qty = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter Price: "); double price = Double.parseDouble(sc.nextLine());
                        adminService.addBook(isbn, name, author, qty, price);
                        break;
                    case 2:
                        System.out.print("Enter ISBN: "); String mIsbn = sc.nextLine();
                        System.out.print("Enter New Quantity: "); int mQty = Integer.parseInt(sc.nextLine());
                        adminService.modifyBookQuantity(mIsbn, mQty);
                        break;
                    case 3:
                        System.out.print("Enter ISBN: "); String dIsbn = sc.nextLine();
                        adminService.deleteBook(dIsbn);
                        break;
                    case 4:
                        System.out.print("Enter Email: "); String uEmail = sc.nextLine();
                        System.out.print("Enter Password: "); String uPass = sc.nextLine();
                        System.out.print("Enter Name: "); String uName = sc.nextLine();
                        System.out.print("Enter Role (admin/borrower): "); String uRole = sc.nextLine();
                        adminService.addUser(uEmail, uPass, uName, uRole);
                        break;
                    case 5:
                        System.out.print("Sort By (name/quantity): "); String sort = sc.nextLine();
                        adminService.viewAllBooks(sort);
                        break;
                    case 6:
                        System.out.print("Enter Search Term (ISBN/Name): "); String query = sc.nextLine();
                        adminService.searchBook(query);
                        break;
                    case 7:
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
        System.out.println("1. Books with Less Quantity (<2)");
        System.out.println("2. Not Borrowed Books");
        System.out.println("3. Heavily Borrowed Books (>3 times)");
        System.out.println("4. Outstanding Students");
        System.out.println("5. Search Book Status by ISBN");
        System.out.print("Select a report: ");
        int choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
            case 1: adminService.reportLowQuantity(); break;
            case 2: adminService.reportNotBorrowed(); break;
            case 3: adminService.reportHeavilyBorrowed(); break;
            case 4: adminService.reportOutstandingStudents(); break;
            case 5: 
                System.out.print("Enter ISBN: ");
                adminService.searchBookStatusByIsbn(sc.nextLine()); 
                break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void borrowerMenu(User user) {
        while (true) {
            System.out.println("\n*** Borrower Portal ***");
            System.out.println("1. View Available Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Return a Book / Fine Processing");
            System.out.println("4. Extend Tenure");
            System.out.println("5. Inform Book Lost");
            System.out.println("6. Inform Card Lost");
            System.out.println("7. View Fine History");
            System.out.println("8. View Borrow History");
            System.out.println("9. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 9) break;

                switch (choice) {
                    case 1: borrowerService.viewAllBooks(); break;
                    case 2: 
                        System.out.print("Enter ISBN to borrow: "); 
                        borrowerService.checkoutBook(user, sc.nextLine()); 
                        break;
                    case 3: borrowerService.returnBook(user); break;
                    case 4: borrowerService.extendTenure(user); break;
                    case 5: borrowerService.reportLostBook(user); break;
                    case 6: borrowerService.reportLostCard(user); break;
                    case 7: borrowerService.viewFineHistory(user); break;
                    case 8: borrowerService.viewBorrowHistory(user); break;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Input error. Please try again.");
            }
        }
    }
}
