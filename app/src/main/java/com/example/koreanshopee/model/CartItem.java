package com.example.koreanshopee.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String id;
    private String productId;
    private int quantity;
    private int priceAtTime;
    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getPriceAtTime() { return priceAtTime; }
    public void setPriceAtTime(int priceAtTime) { this.priceAtTime = priceAtTime; }
} 