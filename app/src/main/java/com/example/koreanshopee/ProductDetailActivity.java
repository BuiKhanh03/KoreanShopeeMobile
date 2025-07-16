package com.example.koreanshopee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.CartManager;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductDetailResponse;
import com.example.koreanshopee.ui.main.CartActivity;
import com.example.koreanshopee.ui.main.ChatActivity;
import com.example.koreanshopee.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.graphics.Color;
import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import com.example.koreanshopee.model.ReviewRequest;
import com.example.koreanshopee.model.Category;
import com.example.koreanshopee.model.CategoryListResponse;

public class ProductDetailActivity extends AppCompatActivity {
    private String productId;
    private TokenManager tokenManager;
    private Product currentProduct;
    private Product.Review myReview = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tokenManager = new TokenManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar_product_detail);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chi tiết sản phẩm");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        ImageView imgProduct = findViewById(R.id.img_detail_product);
        TextView tvName = findViewById(R.id.tv_detail_product_name);
        TextView tvPrice = findViewById(R.id.tv_detail_product_price);
        TextView tvStatus = findViewById(R.id.tv_detail_product_status);
        TextView tvDescription = findViewById(R.id.tv_detail_product_description);
        TextView tvBrand = findViewById(R.id.tvBrand);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvSeller = findViewById(R.id.tvSeller);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);
        ImageView iconChat = findViewById(R.id.btn_chat);
//        Button btnReviewProduct = findViewById(R.id.btnReviewProduct);

        LinearLayout layoutProductSizes = findViewById(R.id.layoutProductSizes);
        LinearLayout layoutProductReviews = findViewById(R.id.layoutProductReviews);

        this.productId = getIntent().getStringExtra("product_id");
        if (this.productId == null || this.productId.isEmpty()) {
            finish();
            return;
        }

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductDetail(this.productId).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    currentProduct = response.body().getValue();
                    Log.d("API_DETAIL", "brand=" + currentProduct.getBrand());
                    // Hiển thị tên
                    tvName.setText(currentProduct.getName() != null ? currentProduct.getName() : "Không rõ tên");
                    // Hiển thị giá
                    tvPrice.setText(currentProduct.getPrice() + " ₫");
                    // Hiển thị trạng thái
                    String status = currentProduct.getStatus();
                    int stockQuantity = currentProduct.getStockQuantity();

                    if (status == null || status.isEmpty()) {
                        tvStatus.setText("Không rõ trạng thái");
                        tvStatus.setBackgroundResource(R.drawable.bg_outofstock); // hoặc tạo file `bg_unknown.xml` nếu muốn
                    } else if (status.equalsIgnoreCase("Inactive") || stockQuantity == 0) {
                        tvStatus.setText("Hết hàng");
                        tvStatus.setBackgroundResource(R.drawable.bg_outofstock);
                    } else {
                        tvStatus.setText("Còn hàng");
                        tvStatus.setBackgroundResource(R.drawable.bg_instock);
                    }

                    // Hiển thị mô tả
                    String desc = currentProduct.getDescription();
                    tvDescription.setText("Thông tin mô tả: " + (desc != null && !desc.isEmpty() ? desc : "Không có mô tả"));
                    // Hiển thị brand, seller, category với kiểm tra rỗng
                    tvBrand.setText("Thương hiệu: " + (currentProduct.getBrand() != null && !currentProduct.getBrand().isEmpty() ? currentProduct.getBrand() : "Không rõ"));
                    tvSeller.setText(currentProduct.getSellerName() != null && !currentProduct.getSellerName().isEmpty() ? currentProduct.getSellerName() : "Không rõ");
                    // Gọi API lấy danh sách category và map sang tên
                    apiService.getCategories().enqueue(new Callback<CategoryListResponse>() {
                        @Override
                        public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                                String categoryName = "Không rõ";
                                for (Category cat : response.body().getValue()) {
                                    if (cat.getId().equals(currentProduct.getCategoryId())) {
                                        categoryName = cat.getName();
                                        break;
                                    }
                                }
                                tvCategory.setText("Danh mục: " + categoryName);
                            } else {
                                tvCategory.setText("Danh mục: Không rõ");
                            }
                        }
                        @Override
                        public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                            tvCategory.setText("Danh mục: Không rõ");
                        }
                    });
                    // Hiển thị ảnh (nếu có)
                    if (currentProduct.getProductImages() != null && !currentProduct.getProductImages().isEmpty()) {
                        // TODO: Load ảnh từ URL nếu có, ví dụ dùng Glide/Picasso
                        // Glide.with(ProductDetailActivity.this).load(currentProduct.getProductImages().get(0)).into(imgProduct);
                    } else {
                        imgProduct.setImageResource(R.drawable.bg2); // Ảnh mặc định
                    }

                    // Hiển thị sizes
                    layoutProductSizes.removeAllViews();
                    TextView tvLabel = new TextView(ProductDetailActivity.this);
                    tvLabel.setText("Kích thước sản phẩm:");
                    tvLabel.setTextColor(Color.BLACK);
                    tvLabel.setTextSize(16f);
                    tvLabel.setPadding(0, 0, 0, 8);
                    layoutProductSizes.addView(tvLabel);

                    if (currentProduct.getProductSizes() != null && !currentProduct.getProductSizes().isEmpty()) {
                        for (Product.ProductSize size : currentProduct.getProductSizes()) {
                            TextView tvSize = new TextView(ProductDetailActivity.this);
                            tvSize.setText(size.getSize());
                            tvSize.setTextSize(14f);
                            tvSize.setTextColor(Color.WHITE);
                            tvSize.setBackgroundResource(R.drawable.bg_pink); // sử dụng bg_pink hoặc drawable bo góc
                            tvSize.setPadding(24, 8, 24, 8);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(8, 0, 8, 0);
                            tvSize.setLayoutParams(params);
                            layoutProductSizes.addView(tvSize);
                        }
                    } else {
                        TextView tvNone = new TextView(ProductDetailActivity.this);
                        tvNone.setText("Không có kích cỡ");
                        tvNone.setTextColor(Color.GRAY);
                        layoutProductSizes.addView(tvNone);
                    }

                    // Hiển thị reviews
                    layoutProductReviews.removeAllViews();
                    myReview = null;
                    String myUserId = tokenManager.isLoggedIn() ? tokenManager.getUserId() : null;
                    if (currentProduct.getReviews() != null && !currentProduct.getReviews().isEmpty()) {
                        for (Object reviewObj : currentProduct.getReviews()) {
                            com.example.koreanshopee.model.Product.Review review = null;
                            if (reviewObj instanceof com.example.koreanshopee.model.Product.Review) {
                                review = (com.example.koreanshopee.model.Product.Review) reviewObj;
                            } else if (reviewObj instanceof java.util.Map) {
                                java.util.Map map = (java.util.Map) reviewObj;
                                review = new com.example.koreanshopee.model.Product.Review();
                                review.setId((String) map.get("id"));
                                review.setUserId((String) map.get("userId"));
                                Object ratingObj = map.get("rating");
                                int rating = 0;
                                if (ratingObj instanceof Number) {
                                    rating = ((Number) ratingObj).intValue();
                                }
                                review.setRating(rating);
                                review.setComment((String) map.get("comment"));
                                review.setUserName((String) map.get("userName"));
                                review.setUserImageUrl((String) map.get("userImageUrl"));
                            }
                            if (review != null) {
                                if (myUserId != null && myUserId.equals(review.getUserId())) {
                                    myReview = review;
                                }
                                LinearLayout reviewLayout = new LinearLayout(ProductDetailActivity.this);
                                reviewLayout.setOrientation(LinearLayout.HORIZONTAL);
                                reviewLayout.setPadding(0, 8, 0, 8);
                                ImageView avatar = new ImageView(ProductDetailActivity.this);
                                avatar.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
                                avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                if (review.getUserImageUrl() != null && !review.getUserImageUrl().isEmpty()) {
                                    // Glide.with(ProductDetailActivity.this).load(review.getUserImageUrl()).into(avatar);
                                } else {
                                    avatar.setImageResource(R.drawable.ic_userr);
                                }
                                LinearLayout infoLayout = new LinearLayout(ProductDetailActivity.this);
                                infoLayout.setOrientation(LinearLayout.VERTICAL);
                                infoLayout.setPadding(16, 0, 0, 0);
                                TextView tvUser = new TextView(ProductDetailActivity.this);
                                tvUser.setText(review.getUserName() != null ? review.getUserName() : "Ẩn danh");
                                tvUser.setTextColor(Color.BLACK);
                                tvUser.setTextSize(15f);
                                TextView tvRating = new TextView(ProductDetailActivity.this);
                                tvRating.setText("★ " + review.getRating());
                                tvRating.setTextColor(Color.parseColor("#FFD600"));
                                tvRating.setTextSize(14f);
                                TextView tvComment = new TextView(ProductDetailActivity.this);
                                tvComment.setText(review.getComment());
                                tvComment.setTextColor(Color.DKGRAY);
                                tvComment.setTextSize(14f);
                                infoLayout.addView(tvUser);
                                infoLayout.addView(tvRating);
                                infoLayout.addView(tvComment);
                                reviewLayout.addView(avatar);
                                reviewLayout.addView(infoLayout);
                                layoutProductReviews.addView(reviewLayout);
                            }
                        }
                    } else {
                        TextView tvNone = new TextView(ProductDetailActivity.this);
                        tvNone.setText("Chưa có đánh giá");
                        tvNone.setTextColor(Color.GRAY);
                        layoutProductReviews.addView(tvNone);
                    }

                    // Setup button click listeners
                    setupButtonListeners();

//                    if (myReview != null) {
//                        btnReviewProduct.setText("Chỉnh sửa đánh giá");
//                    } else {
//                        btnReviewProduct.setText("Đánh giá sản phẩm");
//                    }
//                    btnReviewProduct.setOnClickListener(v -> showReviewDialog());
                } else {
                    Log.d("API_DETAIL", "response not successful or body null");
                }
            }
            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Log.e("API_DETAIL", "API call failed", t);
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupButtonListeners() {
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tokenManager.isLoggedIn()) {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentProduct == null) {
                    Toast.makeText(ProductDetailActivity.this, "Không thể thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                addToCart(1); // Default quantity = 1
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tokenManager.isLoggedIn()) {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentProduct == null) {
                    Toast.makeText(ProductDetailActivity.this, "Không thể mua sản phẩm này", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add to cart first, then navigate to cart
                addToCart(1);
            }
        });
    }

    private void addToCart(int quantity) {
        String token = tokenManager.getAuthorizationHeader();
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.addToCart(token, productId, quantity).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();

                    // If it was "Buy Now", navigate to cart
                    Button btnBuyNow = findViewById(R.id.btnBuyNow);
                    if (btnBuyNow.isPressed()) {
                        Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không thể thêm vào giỏ hàng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ADD_TO_CART", "API call failed", t);
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showReviewDialog() {
        if (!tokenManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(myReview != null ? "Chỉnh sửa đánh giá" : "Đánh giá sản phẩm");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);
        final EditText edtRating = new EditText(this);
        edtRating.setHint("Số sao (1-5)");
        edtRating.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (myReview != null) edtRating.setText(String.valueOf(myReview.getRating()));
        layout.addView(edtRating);
        final EditText edtComment = new EditText(this);
        edtComment.setHint("Bình luận");
        if (myReview != null) edtComment.setText(myReview.getComment());
        layout.addView(edtComment);
        builder.setView(layout);
        builder.setPositiveButton("Gửi", (dialog, which) -> {
            int rating = 0;
            try { rating = Integer.parseInt(edtRating.getText().toString().trim()); } catch (Exception e) {}
            String comment = edtComment.getText().toString().trim();
            if (rating < 1 || rating > 5) {
                Toast.makeText(this, "Số sao phải từ 1 đến 5", Toast.LENGTH_SHORT).show();
                return;
            }
            if (comment.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show();
                return;
            }
            if (myReview != null && myReview.getId() != null && !myReview.getId().isEmpty()) {
                updateReview(myReview.getId(), rating, comment);
            } else {
                createReview(rating, comment);
            }
        });
        builder.setNegativeButton("Huỷ", null);
        builder.show();
    }

    private void createReview(int rating, String comment) {
        String token = tokenManager.getAuthorizationHeader();
        if (token == null || currentProduct == null) {
            Toast.makeText(this, "Thiếu thông tin để gửi đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewRequest request = new ReviewRequest(currentProduct.getId(), rating, comment);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.createReview(token, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    recreate(); // Reload lại activity để cập nhật review
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Bạn đã bình luận sản phẩm này rồi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối khi gửi đánh giá.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateReview(String reviewId, int rating, String comment) {
        String token = tokenManager.getAuthorizationHeader();
        if (token == null || currentProduct == null) {
            Toast.makeText(this, "Thiếu thông tin để cập nhật đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewRequest request = new ReviewRequest(currentProduct.getId(), rating, comment);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.updateReview(token, reviewId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Cập nhật đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    recreate();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không thể cập nhật đánh giá. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối khi cập nhật đánh giá.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_book, menu);

        MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();

        TextView cartBadge = actionView.findViewById(R.id.cart_badge);
        ImageView cartIcon = actionView.findViewById(R.id.ic_cart);

        int cartCount = CartManager.getCartCount(); //
        if (cartCount > 0) {
            cartBadge.setText(String.valueOf(cartCount));
            cartBadge.setVisibility(View.VISIBLE);
        } else {
            cartBadge.setVisibility(View.GONE);
        }

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            Intent intent = new Intent(this, LayoutScreen.class);
            intent.putExtra("navigateTo", "cart");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.cart_container).getVisibility() == View.VISIBLE) {
            getSupportFragmentManager().popBackStack();
            findViewById(R.id.cart_container).setVisibility(View.GONE);
            findViewById(R.id.main).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
} 