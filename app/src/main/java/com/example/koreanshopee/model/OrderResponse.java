package com.example.koreanshopee.model;
import java.io.Serializable;

public class OrderResponse implements Serializable {
    private OrderValue value;
    private Object paging;
    private Object errors;
    
    public OrderValue getValue() { return value; }
    public void setValue(OrderValue value) { this.value = value; }
    
    public Object getPaging() { return paging; }
    public void setPaging(Object paging) { this.paging = paging; }
    
    public Object getErrors() { return errors; }
    public void setErrors(Object errors) { this.errors = errors; }
    
    public static class OrderValue implements Serializable {
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
        private OrderHistory.PaymentRecord paymentRecord;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public String getPaymentRecordId() { return paymentRecordId; }
        public void setPaymentRecordId(String paymentRecordId) { this.paymentRecordId = paymentRecordId; }
        
        public String getShippingId() { return shippingId; }
        public void setShippingId(String shippingId) { this.shippingId = shippingId; }
        
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public double getShippingFee() { return shippingFee; }
        public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }
        
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
        
        public String getCreateAt() { return createAt; }
        public void setCreateAt(String createAt) { this.createAt = createAt; }
        
        public String getUpdateAt() { return updateAt; }
        public void setUpdateAt(String updateAt) { this.updateAt = updateAt; }
        
        public OrderHistory.PaymentRecord getPaymentRecord() { return paymentRecord; }
        public void setPaymentRecord(OrderHistory.PaymentRecord paymentRecord) { this.paymentRecord = paymentRecord; }
    }
} 