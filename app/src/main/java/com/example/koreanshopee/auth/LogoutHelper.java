package com.example.koreanshopee.auth;

import android.content.Context;
import android.content.Intent;

import com.example.koreanshopee.ui.auth.LoginActivity;
import com.example.koreanshopee.utils.TokenManager;

public class LogoutHelper {
    
    public static void logout(Context context) {
        // Clear tokens
        TokenManager tokenManager = new TokenManager(context);
        tokenManager.clearToken();
        
        // Navigate back to login screen
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
} 