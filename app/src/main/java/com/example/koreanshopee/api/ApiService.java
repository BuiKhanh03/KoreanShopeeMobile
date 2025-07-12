package com.example.koreanshopee.api;

import com.example.koreanshopee.model.LoginRequest;
import com.example.koreanshopee.model.LoginResponse;
import com.example.koreanshopee.model.RegisterRequest;
import com.example.koreanshopee.model.RegisterResponse;
import com.example.koreanshopee.model.UpdateProfileRequest;
import com.example.koreanshopee.model.UserProfileResponse;
import com.example.koreanshopee.model.CategoryListResponse;
import com.example.koreanshopee.model.CartResponse;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.VnpayRequest;
import com.example.koreanshopee.model.VnpayResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Path;
import retrofit2.http.DELETE;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface ApiService {
    @POST("api/authentication/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    
    @POST("api/authentication/register")
    Call<Void> register(@Body RegisterRequest registerRequest);
    
    @GET("api/user")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String authorization);
    
    @PUT("api/user")
    Call<UserProfileResponse> updateUserProfile(@Header("Authorization") String authorization, @Body UpdateProfileRequest updateRequest);

    @Multipart
    @POST("/api/user/image")
    Call<ResponseBody> uploadAvatar(
        @Header("Authorization") String authorization,
        @Part MultipartBody.Part image
    );

    @GET("api/category")
    Call<CategoryListResponse> getCategories();

    @GET("api/product")
    Call<com.example.koreanshopee.model.ProductListResponse> getProducts(
        @Query("PageNumber") int pageNumber,
        @Query("PageSize") int pageSize
    );

    @GET("api/product")
    Call<com.example.koreanshopee.model.ProductListResponse> searchProducts(
        @Query("Name") String searchQuery,
        @Query("PageNumber") int pageNumber,
        @Query("PageSize") int pageSize
    );

    @GET("api/product/{productId}")
    Call<com.example.koreanshopee.model.ProductDetailResponse> getProductDetail(@Path("productId") String productId);

    @GET("api/cart")
    Call<CartResponse> getCart(@Header("Authorization") String authorization);

    @POST("api/cartitem")
    Call<Void> addToCart(
        @Header("Authorization") String authorization,
        @Query("ProductId") String productId,
        @Query("Quantity") int quantity
    );

    @PUT("api/cartitem/{cartItemId}")
    Call<Void> updateCartItemQuantity(
        @Header("Authorization") String authorization,
        @Path("cartItemId") String cartItemId,
        @Query("Quantity") int quantity
    );

    @DELETE("api/cartitem/{cartItemId}")
    Call<Void> deleteCartItem(
        @Header("Authorization") String authorization,
        @Path("cartItemId") String cartItemId
    );

    @POST("api/order")
    Call<com.example.koreanshopee.model.OrderResponse> createOrder(
        @Header("Authorization") String authorization,
        @Body com.example.koreanshopee.model.OrderRequest orderRequest
    );

    @POST("api/vnpay/create-payment-url")
    Call<VnpayResponse> createVnpayPayment(
        @Header("Authorization") String authorization,
        @Query("OrderType") String orderType,
        @Query("Amount") double amount,
        @Query("OrderDescription") String orderDescription,
        @Query("PaymentIdR") String paymentIdR
    );

    @GET("api/order/{orderId}")
    Call<com.example.koreanshopee.model.OrderStatusResponse> getOrderStatus(
        @Header("Authorization") String authorization,
        @Path("orderId") String orderId
    );

    @GET("api/order")
    Call<com.example.koreanshopee.model.OrderListResponse> getOrderHistory(
        @Header("Authorization") String authorization
    );

    @GET("api/order")
    Call<com.example.koreanshopee.model.OrderListResponse> getOrderByPaymentRecordId(
        @Header("Authorization") String authorization,
        @Query("PaymentRecordId") String paymentRecordId
    );

    @POST("api/review")
    Call<Void> createReview(
        @Header("Authorization") String authorization,
        @Body com.example.koreanshopee.model.ReviewRequest reviewRequest
    );

    @PUT("api/review/{reviewId}")
    Call<Void> updateReview(
        @Header("Authorization") String authorization,
        @Path("reviewId") String reviewId,
        @Body com.example.koreanshopee.model.ReviewRequest reviewRequest
    );

    @GET("api/product")
    Call<com.example.koreanshopee.model.ProductListResponse> getProductsByCategory(
        @Query("CategoryId") String categoryId
    );

    // Seller APIs
    @GET("api/product/user")
    Call<com.example.koreanshopee.model.ProductListResponse> getSellerProducts(
        @Header("Authorization") String authorization
    );

    @POST("api/product")
    Call<com.example.koreanshopee.model.ProductResponse> createProduct(
        @Header("Authorization") String authorization,
        @Body com.example.koreanshopee.model.CreateProductRequest createProductRequest
    );

} 