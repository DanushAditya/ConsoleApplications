package library.models;

public class Book {
    private String isbn;
    private String name;
    private String author;
    private int quantity;
    private double price;
    private int borrowCount;

    public Book(String isbn, String name, String author, int quantity, double price) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
        this.borrowCount = 0;
    }

    public String getIsbn() { return isbn; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public int getBorrowCount() { return borrowCount; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
    
    public void incrementBorrowCount() { this.borrowCount++; }
    
    public void decreaseQuantity() { this.quantity--; }
    public void increaseQuantity() { this.quantity++; }

    public void display() {
        System.out.printf("ISBN: %-10s | Name: %-20s | Author: %-15s | Qty: %-3d | Price: %.2f\n", 
                isbn, name, author, quantity, price);
    }
}
