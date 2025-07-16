package com.example.koreanshopee.model;

import java.io.Serializable;
import java.util.Date;

public class CartItem implements Serializable {
    private String id;
    private String productId;
    private int quantity;
    private int priceAtTime;
    private String title;
    private int originalPrice;
    private int discountedPrice;
    private boolean isChecked;
    private Date deliveryDate;

    public CartItem(String title, int originalPrice, int quantity, int discountedPrice, boolean isChecked, Date deliveryDate) {
        this.title = title;
        this.originalPrice = originalPrice;
        this.quantity = quantity;
        this.discountedPrice = discountedPrice;
        this.isChecked = isChecked;
        this.deliveryDate = deliveryDate;
    }

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getPriceAtTime() { return priceAtTime; }
    public void setPriceAtTime(int priceAtTime) { this.priceAtTime = priceAtTime; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}