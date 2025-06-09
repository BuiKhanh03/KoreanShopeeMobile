package com.example.koreanshopee;
import java.io.Serializable;

public class Item implements Serializable {
    private String title;
    private String author;
    private int price;
    private int id;
    private int imageResId;

    public Item(String title, String author, int price, int imageResId) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public int getImageResId() {
        return imageResId;
    }
}
