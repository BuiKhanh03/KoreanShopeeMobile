package com.example.koreanshopee.model;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final List<CartItem> cartItems = new ArrayList<>();

    public static void addToCart(CartItem item) {
        for (CartItem existing : cartItems) {
            if (existing.getTitle().equals(item.getTitle())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static int getCartCount() {
        return cartItems.size();
    }
}
