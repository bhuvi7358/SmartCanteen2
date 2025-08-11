package com.example.smartcanteen.customerFoodPanel;

public class CartItem {

    private String name;
    private String price;
    private int quantity;
    private double totalPrice;

    public CartItem() {
        // Default constructor
    }

    public CartItem(String name, String price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = quantity * Double.parseDouble(price);  // Initial total price
    }

    public CartItem(String name, String price, int quantity, double totalPrice) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = quantity * Double.parseDouble(price);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
