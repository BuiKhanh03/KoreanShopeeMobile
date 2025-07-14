package com.example.koreanshopee.model;
import java.io.Serializable;

public class VnpayRequest implements Serializable {
    private String orderType;
    private double amount;
    private String orderDescription;
    private String orderId;

    public VnpayRequest(String orderType, double amount, String orderDescription, String orderId) {
        this.orderType = orderType;
        this.amount = amount;
        this.orderDescription = orderDescription;
        this.orderId = orderId;
    }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getOrderDescription() { return orderDescription; }
    public void setOrderDescription(String orderDescription) { this.orderDescription = orderDescription; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
} 