package com.example.smartcanteen.customerFoodPanel;

public class OrderItem {
    private String name;
    private int quantity;
    private String price;
    // Change from double to String

    public OrderItem() {
        // Default constructor required for Firebase
    }

    public OrderItem(String name, int quantity, String price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
