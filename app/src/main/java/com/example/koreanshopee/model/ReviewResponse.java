package com.example.koreanshopee.model;

import com.example.koreanshopee.model.Product.Review;

public class ReviewResponse {
    private boolean success;
    private String message;
    private Review data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Review getData() {
        return data;
    }

    public void setData(Review data) {
        this.data = data;
    }
} 