package com.example.smartcanteen.customerFoodPanel;

import java.util.List;

public class Order extends OrderItem{

    private String orderId;           // Unique ID for the order
    private String customerId;        // ID of the customer who placed the order
    private String itemsDescription;  // Description of items in the order
    private double totalPrice;        // Total price of the order
    private String status;            // Status of the order (e.g., "Placed", "Ready", "Completed")
    private long timestamp;// Timestamp when the order was placed
    private String customerPhone;
    private List<OrderItem> items;    // List of individual items in the order

    // Default constructor required for Firebase
    public Order() {}

    // Constructor to initialize order object
    public Order(String orderId, String customerId, String itemsDescription, double totalPrice, String status, long timestamp, List<OrderItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.itemsDescription = itemsDescription;
        this.totalPrice = totalPrice;
        this.status = status;
        this.timestamp = timestamp;
        this.items = items;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemsDescription() {
        return itemsDescription;
    }

    public void setItemsDescription(String itemsDescription) {
        this.itemsDescription = itemsDescription;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}