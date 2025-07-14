package com.example.koreanshopee.model;
import java.io.Serializable;

public class OrderStatusResponse implements Serializable {
    private String status;
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 