package supermarket.models;

import java.util.List;

public class Bill {
    private String billId;
    private String date;
    private List<CartItem> items;
    private double totalAmount;
    private int pointsEarned;
    private double discountApplied;

    public Bill(String billId, String date, List<CartItem> items, double totalAmount, int pointsEarned, double discountApplied) {
        this.billId = billId;
        this.date = date;
        this.items = items;
        this.totalAmount = totalAmount;
        this.pointsEarned = pointsEarned;
        this.discountApplied = discountApplied;
    }

    public String getBillId() { return billId; }
    public String getDate() { return date; }
    public double getTotalAmount() { return totalAmount; }

    public void display() {
        System.out.println("-------------------------------------------------");
        System.out.println("Bill ID: " + billId + " | Date: " + date);
        System.out.println("-------------------------------------------------");
        for (CartItem item : items) {
            item.display();
        }
        System.out.println("-------------------------------------------------");
        System.out.printf("Discount Applied : Rs %.2f\n", discountApplied);
        System.out.printf("Total Amount Paid: Rs %.2f\n", totalAmount);
        System.out.printf("Reward Pts Earned: %d\n", pointsEarned);
        System.out.println("-------------------------------------------------");
    }
}
