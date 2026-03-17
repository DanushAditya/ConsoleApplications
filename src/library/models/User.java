package library.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private String name;
    private String role; // "admin" or "borrower"
    private double securityDeposit;
    
    // Borrower history
    private List<BorrowRecord> currentBorrows;
    private List<BorrowRecord> pastBorrows;
    private List<String> fineHistory;
    
    public User(String email, String password, String name, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role.toLowerCase();
        
        if (this.role.equals("borrower")) {
            this.securityDeposit = 1500; // Default caution deposit
        } else {
            this.securityDeposit = 0;
        }
        
        this.currentBorrows = new ArrayList<>();
        this.pastBorrows = new ArrayList<>();
        this.fineHistory = new ArrayList<>();
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public double getSecurityDeposit() { return securityDeposit; }
    
    public void deductDeposit(double amount) { this.securityDeposit -= amount; }
    public void addDeposit(double amount) { this.securityDeposit += amount; }
    
    public List<BorrowRecord> getCurrentBorrows() { return currentBorrows; }
    public List<BorrowRecord> getPastBorrows() { return pastBorrows; }
    public List<String> getFineHistory() { return fineHistory; }
    
    public void addCurrentBorrow(BorrowRecord record) { this.currentBorrows.add(record); }
    public void removeCurrentBorrow(BorrowRecord record) { this.currentBorrows.remove(record); }
    public void addPastBorrow(BorrowRecord record) { this.pastBorrows.add(record); }
    public void addFineHistory(String reason) { this.fineHistory.add(reason); }
    
    public void display() {
        System.out.printf("Email: %-20s | Name: %-15s | Role: %-10s | Deposit: %.2f\n", 
                          email, name, role, securityDeposit);
    }
}
