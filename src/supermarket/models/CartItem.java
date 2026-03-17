package supermarket.models;

public class CartItem {
    private Product product;
    private int cartQuantity;

    public CartItem(Product product, int cartQuantity) {
        this.product = product;
        this.cartQuantity = cartQuantity;
    }

    public Product getProduct() { return product; }
    public int getCartQuantity() { return cartQuantity; }

    public void setCartQuantity(int cartQuantity) { this.cartQuantity = cartQuantity; }
    
    public double getTotalPrice() {
        return product.getPrice() * cartQuantity;
    }

    public void display() {
        System.out.printf("Product: %-15s | Qty: %-3d | Unit Price: Rs %-6.2f | Total: Rs %.2f\n", 
                          product.getName(), cartQuantity, product.getPrice(), getTotalPrice());
    }
}
