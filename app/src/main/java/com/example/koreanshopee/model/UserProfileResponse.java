package com.example.koreanshopee.model;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {
    @SerializedName("value")
    private UserProfile value;
    
    @SerializedName("paging")
    private Object paging;
    
    @SerializedName("errors")
    private Object errors;

    // Constructor
    public UserProfileResponse() {}

    public UserProfileResponse(UserProfile value, Object paging, Object errors) {
        this.value = value;
        this.paging = paging;
        this.errors = errors;
    }

    // Getters
    public UserProfile getValue() { return value; }
    public Object getPaging() { return paging; }
    public Object getErrors() { return errors; }

    // Setters
    public void setValue(UserProfile value) { this.value = value; }
    public void setPaging(Object paging) { this.paging = paging; }
    public void setErrors(Object errors) { this.errors = errors; }
} 