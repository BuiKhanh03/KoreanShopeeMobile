package com.example.koreanshopee.model;

import com.google.gson.annotations.SerializedName;

public class ProductDetailResponse {
    @SerializedName("value")
    private Product value;

    public Product getValue() {
        return value;
    }

    public void setValue(Product value) {
        this.value = value;
    }
} 