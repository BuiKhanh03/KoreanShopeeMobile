package com.example.koreanshopee.OrderHistory;
public class OrderItem {
    private int imageResId;
    private String title;
    private String author;
    private String price;
    private String quantity;

    public OrderItem(int imageResId, String title, String author, String price, String quantity) {
        this.imageResId = imageResId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPrice() { return price; }
    public String getQuantity() { return quantity; }
}
