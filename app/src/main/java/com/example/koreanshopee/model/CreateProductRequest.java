package com.example.koreanshopee.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CreateProductRequest implements Serializable {
    @SerializedName("categoryId")
    private String categoryId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("price")
    private int price;
    
    @SerializedName("brand")
    private String brand;
    
    @SerializedName("productSizes")
    private List<ProductSizeRequest> productSizes;

    public CreateProductRequest() {}

    public CreateProductRequest(String categoryId, String name, String description, int price, String brand, List<ProductSizeRequest> productSizes) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.productSizes = productSizes;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<ProductSizeRequest> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSizeRequest> productSizes) {
        this.productSizes = productSizes;
    }

    public static class ProductSizeRequest implements Serializable {
        @SerializedName("size")
        private String size;

        public ProductSizeRequest() {}

        public ProductSizeRequest(String size) {
            this.size = size;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
} 