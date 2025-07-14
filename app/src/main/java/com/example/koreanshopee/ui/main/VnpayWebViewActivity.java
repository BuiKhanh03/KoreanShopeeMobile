package com.example.koreanshopee.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.koreanshopee.R;

public class VnpayWebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnpay_webview);

        WebView webView = findViewById(R.id.webView);
        String paymentUrl = getIntent().getStringExtra("paymentUrl");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("vnpay_return")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();
                    return true;
                }
                return false;
            }
        });
        webView.loadUrl(paymentUrl);
    }
} 