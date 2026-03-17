package supermarket.models;

public class User {
    private String email;
    private String password;
    private String name;
    private String role; // "admin" or "customer"
    
    // Customer specific
    private double walletBalance; // Preloaded with 1000
    private int loyaltyPoints;
    private double totalSpent;
    
    // Admin specific
    private double totalSalesMade;

    public User(String email, String password, String name, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role.toLowerCase();
        
        if (this.role.equals("customer")) {
            this.walletBalance = 1000.0;
        } else {
            this.walletBalance = 0.0;
        }
        this.loyaltyPoints = 0;
        this.totalSpent = 0.0;
        this.totalSalesMade = 0.0;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getRole() { return role; }
    
    public double getWalletBalance() { return walletBalance; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public double getTotalSpent() { return totalSpent; }
    public double getTotalSalesMade() { return totalSalesMade; }

    public void addWalletBalance(double amount) { this.walletBalance += amount; }
    public void deductWalletBalance(double amount) { this.walletBalance -= amount; }
    
    public void addLoyaltyPoints(int points) { this.loyaltyPoints += points; }
    public void deductLoyaltyPoints(int points) { this.loyaltyPoints -= points; }
    
    public void addTotalSpent(double amount) { this.totalSpent += amount; }
    public void addTotalSalesMade(double amount) { this.totalSalesMade += amount; }

    public void display() {
        if (role.equals("customer")) {
            System.out.printf("Email: %-20s | Name: %-15s | Credit: Rs %.2f | Loyalty Points: %d\n", 
                              email, name, walletBalance, loyaltyPoints);
        } else {
            System.out.printf("Email: %-20s | Name: %-15s | Total Sales: Rs %.2f\n", 
                              email, name, totalSalesMade);
        }
    }
}
