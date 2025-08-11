package com.example.smartcanteen.chefFoodPanel;

public class Order {

    private String orderId;
    private String customerName;
    private String orderDetails;
    private String status; // "Pending", "Completed", etc.
    private long timestamp;
    // Constructor
    public Order() {}

    public Order(String orderId, String customerName, String orderDetails, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderDetails = orderDetails;
        this.status = status;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
