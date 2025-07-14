package com.example.koreanshopee.model;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("value")
    private Product value;
    
    @SerializedName("paging")
    private Object paging;
    
    @SerializedName("errors")
    private Object errors;

    public Product getValue() {
        return value;
    }

    public void setValue(Product value) {
        this.value = value;
    }

    public Object getPaging() {
        return paging;
    }

    public void setPaging(Object paging) {
        this.paging = paging;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
} 