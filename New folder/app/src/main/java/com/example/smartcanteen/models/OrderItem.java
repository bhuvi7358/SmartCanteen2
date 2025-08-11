package com.example.smartcanteen.models;

public class OrderItem {
    private String orderId;
    private String foodName;
    private int quantity;
    private String status;
    private double totalPrice;
    public OrderItem() {
        // Default constructor required for calls to DataSnapshot.getValue(OrderItem.class)
    }

    public OrderItem(String orderId, String foodName, int quantity, String status) {
        this.orderId = orderId;
        this.foodName = foodName;
        this.quantity = quantity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void Order(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
