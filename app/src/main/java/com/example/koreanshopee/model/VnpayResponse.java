package com.example.koreanshopee.model;
import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class VnpayResponse implements Serializable {
    @SerializedName("url")
    private String paymentUrl;

    public VnpayResponse(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() { return paymentUrl; }
    public void setPaymentUrl(String paymentUrl) { this.paymentUrl = paymentUrl; }
} 