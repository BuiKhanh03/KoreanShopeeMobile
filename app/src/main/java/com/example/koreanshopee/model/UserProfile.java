package com.example.koreanshopee.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("id")
    private String id;
    
    @SerializedName("firstName")
    private String firstName;
    
    @SerializedName("lastName")
    private String lastName;
    
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("imageLink")
    private String imageLink;

    // Constructor
    public UserProfile() {}

    public UserProfile(String id, String firstName, String lastName, String userName, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getImageLink() { return imageLink; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setImageLink(String imageLink) { this.imageLink = imageLink; }

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
} 