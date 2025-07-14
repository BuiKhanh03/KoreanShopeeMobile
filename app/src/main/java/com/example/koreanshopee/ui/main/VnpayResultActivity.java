package com.example.koreanshopee.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

public class VnpayResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri data = intent.getData();
        JSONObject obj = new JSONObject();
        try {
            if (data != null) {
                String responseCode = data.getQueryParameter("vnp_ResponseCode");
                String orderId = data.getQueryParameter("vnp_TxnRef");
                String amount = data.getQueryParameter("vnp_Amount");
                String transactionId = data.getQueryParameter("vnp_TransactionNo");
                String paymentMethod = "VnPay";
                boolean isSuccess = "00".equals(responseCode);
                obj.put("isSuccess", isSuccess);
                obj.put("orderId", orderId != null ? orderId : "");
                obj.put("paymentIdR", "");
                obj.put("paymentMethod", paymentMethod);
                obj.put("amount", amount != null ? amount : "");
                obj.put("transactionId", transactionId != null ? transactionId : "");
                obj.put("vnPayResponseCode", responseCode != null ? responseCode : "");
            } else {
                obj.put("isSuccess", false);
                obj.put("orderId", "");
                obj.put("paymentIdR", "");
                obj.put("paymentMethod", "VnPay");
                obj.put("amount", "");
                obj.put("transactionId", "");
                obj.put("vnPayResponseCode", "");
            }
        } catch (Exception e) {
            try {
                obj.put("isSuccess", false);
                obj.put("orderId", "");
                obj.put("paymentIdR", "");
                obj.put("paymentMethod", "VnPay");
                obj.put("amount", "");
                obj.put("transactionId", "");
                obj.put("vnPayResponseCode", "");
            } catch (Exception ignore) {}
        }
        Intent resultIntent = new Intent(this, PaymentResultActivity.class);
        resultIntent.putExtra("paymentResultJson", obj.toString());
        startActivity(resultIntent);
        finish();
    }
} 