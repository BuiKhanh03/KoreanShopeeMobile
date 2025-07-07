package com.example.koreanshopee.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private RegisterData data;

    public static class RegisterData {
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

        // Getters
        public String getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getUserName() { return userName; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }

        // Setters
        public void setId(String id) { this.id = id; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setUserName(String userName) { this.userName = userName; }
        public void setEmail(String email) { this.email = email; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public RegisterData getData() { return data; }

    // Setters
    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setData(RegisterData data) { this.data = data; }
} 