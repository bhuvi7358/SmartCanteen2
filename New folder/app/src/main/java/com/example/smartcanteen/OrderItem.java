package com.example.smartcanteen;

public class OrderItem {

    private String orderId;
    private String foodName;
    private int quantity;
    private double totalPrice;
    private String status;

    public OrderItem() {
        // Default constructor required for Firebase
    }

    public OrderItem(String orderId, String foodName, int quantity, double totalPrice, String status) {
        this.orderId = orderId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
