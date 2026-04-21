package EqualsAndHashCode.Task4;

import java.util.Objects;

public class Product {
    private String code;
    private int price;

    // Constructors
    public Product() {}
    public Product(String code) { this.setCode(code); }
    public Product(String code, int price) {
        this.setCode(code);
        this.setPrice(price);
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    // equals() compares Product objects by code
    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().equals(obj.getClass()))
            return false;
        Product product = (Product) obj;
        return this.getCode().equals(product.getCode());
    }

    // hashCode() generates hash based on code
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getCode());
    }

    // toString() for readable output
    @Override
    public String toString() {
        return "(code: " + this.getCode() + ", price: " + this.getPrice() + ")";
    }
}
