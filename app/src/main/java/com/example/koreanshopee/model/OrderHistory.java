package com.example.koreanshopee.model;
import java.io.Serializable;
import java.util.List;

public class OrderHistory implements Serializable {
    private String id;
    private String userId;
    private String paymentRecordId;
    private String shippingId;
    private double totalAmount;
    private String status;
    private double shippingFee;
    private String shippingAddress;
    private String createAt;
    private String updateAt;
    private Shipping shipping;
    private PaymentRecord paymentRecord;
    private List<OrderItem> orderItems;

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getPaymentRecordId() { return paymentRecordId; }
    public String getShippingId() { return shippingId; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public double getShippingFee() { return shippingFee; }
    public String getShippingAddress() { return shippingAddress; }
    public String getCreateAt() { return createAt; }
    public String getUpdateAt() { return updateAt; }
    public Shipping getShipping() { return shipping; }
    public PaymentRecord getPaymentRecord() { return paymentRecord; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    public static class Shipping implements Serializable {
        private String id;
        private String carrier;
        private String shippedAt;
        private String deliveredAt;
        private String shippingStatus;
        public String getId() { return id; }
        public String getCarrier() { return carrier; }
        public String getShippedAt() { return shippedAt; }
        public String getDeliveredAt() { return deliveredAt; }
        public String getShippingStatus() { return shippingStatus; }
    }
    public static class PaymentRecord implements Serializable {
        private String id;
        private String paymentRecordMethod;
        private double amountPaid;
        private String paidAt;
        private String paymentRecordStatus;
        public String getId() { return id; }
        public String getPaymentRecordMethod() { return paymentRecordMethod; }
        public double getAmountPaid() { return amountPaid; }
        public String getPaidAt() { return paidAt; }
        public String getPaymentRecordStatus() { return paymentRecordStatus; }
    }
    public static class OrderItem implements Serializable {
        private String id;
        private String orderId;
        private String productId;
        private String productName;
        private int quantity;
        private double priceAtTime;
        public String getId() { return id; }
        public String getOrderId() { return orderId; }
        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getPriceAtTime() { return priceAtTime; }
    }
} 