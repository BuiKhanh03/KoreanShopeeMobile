package com.example.koreanshopee.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.GridLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.Category;
import com.example.koreanshopee.model.CategoryListResponse;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductListResponse;
import com.example.koreanshopee.ui.main.BannerAdapter;
import com.example.koreanshopee.ui.main.ChatActivity;
import com.example.koreanshopee.ui.main.ProductByCategoryFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;

public class HomeFragment extends Fragment {
    private ApiService apiService;
    private RecyclerView recyclerCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private int currentPage = 1;
    private int pageSize = 4; // Số sản phẩm mỗi trang (2 cột x 2 dòng)
    private EditText searchBar;
    private GridLayout gridProducts;
    private ViewPager2 bannerViewPager;
    private Handler bannerHandler;
    private Runnable bannerRunnable;
    private int currentBannerIndex = 0;
    private View btnPrev, btnNext, sectionBooks;
    private TextView tvBookTitle;
    private ImageView iconChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        iconChat = view.findViewById(R.id.icon_chat);
        sectionBooks = view.findViewById(R.id.section_top_books);
        tvBookTitle = sectionBooks.findViewById(R.id.tvSectionTitle);
        gridProducts = sectionBooks.findViewById(R.id.grid_products);
        btnPrev = sectionBooks.findViewById(R.id.btnPrevPage);
        btnNext = sectionBooks.findViewById(R.id.btnNextPage);
        searchBar = view.findViewById(R.id.search_bar);
        recyclerCategories = view.findViewById(R.id.recycler_categories);
        bannerViewPager = view.findViewById(R.id.banner_view_pager);

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        });


        tvBookTitle.setText("Phổ biến");

        if (btnPrev != null && btnNext != null) {
            btnPrev.setOnClickListener(v -> {
                if (currentPage > 1) {
                    currentPage--;
                    String searchQuery = searchBar.getText().toString().trim();
                    if (searchQuery.isEmpty()) {
                        fetchProducts();
                    } else {
                        searchProducts(searchQuery);
                    }
                }
            });
            btnNext.setOnClickListener(v -> {
                currentPage++;
                String searchQuery = searchBar.getText().toString().trim();
                if (searchQuery.isEmpty()) {
                    fetchProducts();
                } else {
                    searchProducts(searchQuery);
                }
            });
        }

        categoryAdapter = new CategoryAdapter(categoryList, this); // 👈 truyền Fragment hiện tại
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCategories.setAdapter(categoryAdapter);
        fetchCategories();
        fetchProducts();

        searchBar.addTextChangedListener(new TextWatcher() {
            private android.os.Handler handler = new android.os.Handler();
            private Runnable searchRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel previous search request
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                // Create new search request with 500ms delay
                searchRunnable = () -> filterProducts(s.toString());
                handler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        List<Integer> banners = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        );

        BannerAdapter adapter = new BannerAdapter(banners);
        bannerViewPager.setAdapter(adapter);

        bannerHandler = new Handler(Looper.getMainLooper());
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                currentBannerIndex = (currentBannerIndex + 1) % banners.size();
                bannerViewPager.setCurrentItem(currentBannerIndex, true);
                bannerHandler.postDelayed(this, 3000);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);

        return view;
    }

    private void fetchCategories() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryListResponse> call, @NonNull Response<CategoryListResponse> response) {
                Log.d("CategoryAPI", "onResponse: code=" + response.code() + ", body=" + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    if (response.body().getValue() != null) {
                        categoryList.addAll(response.body().getValue());
                    }
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.e("CategoryAPI", "Response not successful: code=" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryListResponse> call, @NonNull Throwable t) {
                Log.e("CategoryAPI", "onFailure: ", t);
            }
        });
    }

    private void fetchProducts() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProducts(currentPage, pageSize).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                Log.d("ProductAPI", "onResponse: code=" + response.code() + ", body=" + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    if (response.body().getValue() != null) {
                        productList.addAll(response.body().getValue());
                    }
                    Log.d("ProductAPI", "Số sản phẩm nhận được: " + productList.size());
                    updateTopBooks();
                } else {
                    Log.e("ProductAPI", "Response not successful: code=" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                Log.e("ProductAPI", "onFailure: ", t);
            }
        });
    }

    private void updateTopBooks() {
        if (getView() == null) return;
        Log.d("ProductAPI", "updateTopBooks: productList.size=" + productList.size());

        // Clear existing views
        gridProducts.removeAllViews();

        // Add product views to grid
        for (int i = 0; i < Math.min(productList.size(), 4); i++) {
            Product product = productList.get(i);
            View productView = createProductView(product);
            gridProducts.addView(productView);
        }
    }

    private View createProductView(Product product) {
        View productView = LayoutInflater.from(getContext()).inflate(R.layout.item_product_grid, gridProducts, false);

        ImageView imgProduct = productView.findViewById(R.id.img_product);
        TextView tvProductName = productView.findViewById(R.id.tv_product_name);
        TextView tvProductPrice = productView.findViewById(R.id.tv_product_price);
        TextView tvProductStatus = productView.findViewById(R.id.tv_product_status);

        tvProductName.setText(product.getName());
        tvProductPrice.setText(product.getPrice() + " VNĐ");
        tvProductStatus.setText(product.getStatus());
        imgProduct.setImageResource(R.drawable.avt1);
        productView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(getContext(), com.example.koreanshopee.ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            getContext().startActivity(intent);
        });

        return productView;
    }

    private void filterProducts(String query) {
        if (query.trim().isEmpty()) {
            // If search is empty, show all products
            currentPage = 1;
            fetchProducts();
        } else {
            // If search has text, search for products
            currentPage = 1;
            searchProducts(query.trim());
        }
    }

    private void searchProducts(String searchQuery) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.searchProducts(searchQuery, currentPage, pageSize).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                Log.d("ProductSearchAPI", "onResponse: code=" + response.code() + ", body=" + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    if (response.body().getValue() != null) {
                        productList.addAll(response.body().getValue());
                    }
                    Log.d("ProductSearchAPI", "Số sản phẩm tìm được: " + productList.size());
                    updateTopBooks();
                } else {
                    Log.e("ProductSearchAPI", "Response not successful: code=" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                Log.e("ProductSearchAPI", "onFailure: ", t);
            }
        });
    }

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
        private final List<Category> categories;
        private final Fragment parentFragment;

        public CategoryAdapter(List<Category> categories, Fragment parentFragment) {
            this.categories = categories;
            this.parentFragment = parentFragment; // 🔥 GÁN GIÁ TRỊ
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            Category category = categories.get(position);
            holder.tvCategoryName.setText(category.getName());

            // Set màu nền khác nhau cho từng category
            int[] colors = {
                    0xFFE3F2FD, // Light Blue
                    0xFFF3E5F5, // Light Purple
                    0xFFE8F5E8, // Light Green
                    0xFFFFF3E0, // Light Orange
                    0xFFFCE4EC, // Light Pink
                    0xFFF1F8E9, // Light Lime
                    0xFFE0F2F1, // Light Teal
                    0xFFFFF8E1  // Light Yellow
            };

            int colorIndex = position % colors.length;
            holder.ivCategoryIcon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(colors[colorIndex]));
            holder.itemView.setOnClickListener(v -> {
                ProductByCategoryFragment fragment = ProductByCategoryFragment.newInstance(category.getId());

                parentFragment.requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });

        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        static class CategoryViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCategoryIcon;
            TextView tvCategoryName;

            public CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
                tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            }
        }
    }


}

