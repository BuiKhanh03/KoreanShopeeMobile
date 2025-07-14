package com.example.koreanshopee.model;
import java.util.List;

public class OrderRequest {
    private String shippingAddress;
    private PaymentRecord paymentRecord;
    private int shippingFee = 10000;
    private Shipping shipping;
    private List<OrderItemRequest> orderItems;

    public OrderRequest(String shippingAddress, List<OrderItemRequest> orderItems) {
        this.shippingAddress = shippingAddress;
        this.shippingFee = 10000;
        this.paymentRecord = new PaymentRecord();
        this.shipping = new Shipping();
        this.orderItems = orderItems;
    }
    public String getShippingAddress() { return shippingAddress; }
    public List<OrderItemRequest> getOrderItems() { return orderItems; }
    public PaymentRecord getPaymentRecord() { return paymentRecord; }
    public Shipping getShipping() { return shipping; }

    public static class PaymentRecord {
        private String paymentRecordMethod = "Momo";
        public String getPaymentRecordMethod() { return paymentRecordMethod; }
    }
    public static class Shipping {
        private String carrier = "ViettelPost";
        public String getCarrier() { return carrier; }
    }
    public static class OrderItemRequest {
        private String productId;
        private int quantity;
        public OrderItemRequest(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
        public String getProductId() { return productId; }
        public int getQuantity() { return quantity; }
    }
} 