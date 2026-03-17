package supermarket.models;

public class Product {
    private String productId;
    private String name;
    private double price;
    private int quantity;
    private int soldQuantity;

    public Product(String productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.soldQuantity = 0;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getSoldQuantity() { return soldQuantity; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public void reduceQuantity(int qty) { this.quantity -= qty; }
    public void increaseSoldQuantity(int qty) { this.soldQuantity += qty; }

    public void display() {
        System.out.printf("ID: %-8s | Name: %-15s | Price: Rs %-8.2f | Stock: %-4d\n", 
                          productId, name, price, quantity);
    }
}
