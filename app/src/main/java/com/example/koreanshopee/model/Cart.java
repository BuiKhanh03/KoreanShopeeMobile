package com.example.koreanshopee.model;

import java.util.List;

public class Cart {
    private String id;
    private String userId;
    private List<CartItem> cartItems;
    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }
} 