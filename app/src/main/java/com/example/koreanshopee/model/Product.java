package com.example.koreanshopee.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String sellerId;
    @SerializedName("sellerName")
    private String sellerName;
    private String categoryId;
    @SerializedName("categoryname")
    private String categoryname;
    private String name;
    private String description;
    private int price;
    private int stockQuantity;
    @SerializedName("brand")
    private String brand;
    private String createdAt;
    private String updatedAt;
    private String status;
    private List<Object> reviews;
    private List<Object> productImages;
    private List<ProductSize> productSizes;

    public String getId() { return id; }
    public String getSellerId() { return sellerId; }
    public String getSellerName() { return sellerName; }
    public String getCategoryId() { return categoryId; }
    public String getCategoryname() { return categoryname; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getBrand() { return brand; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getStatus() { return status; }
    public List<Object> getReviews() { return reviews; }
    public List<Object> getProductImages() { return productImages; }
    public List<ProductSize> getProductSizes() { return productSizes; }

    public static class ProductSize implements Serializable {
        private String id;
        private String size;
        public String getId() { return id; }
        public String getSize() { return size; }
    }

    public static class Review implements Serializable {
        private String id;
        private String userId;
        private int rating;
        private String comment;
        private String userName;
        private String userImageUrl;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getUserImageUrl() { return userImageUrl; }
        public void setUserImageUrl(String userImageUrl) { this.userImageUrl = userImageUrl; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
} 