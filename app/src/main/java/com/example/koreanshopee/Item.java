package com.example.koreanshopee;
import java.io.Serializable;

public class Item implements Serializable {
    private String title;
    private String author;
    private int price;
    private String id;
    private int imageResId;
    private String name;
    private String image;
    private String description;
    private String categoryId;

    public Item(String title, String author, int price, int imageResId) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.imageResId = imageResId;
    }

    public Item() {
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

    public String getId() {
        return id;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name != null ? name : title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
