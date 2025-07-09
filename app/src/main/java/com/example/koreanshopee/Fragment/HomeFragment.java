package com.example.koreanshopee.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.koreanshopee.Item;
import com.example.koreanshopee.ItemAdapter;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.Category;
import com.example.koreanshopee.model.CategoryListResponse;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import android.util.Log;

public class HomeFragment extends Fragment {
    public HomeFragment() {}
    private ApiService apiService;
    private TextView textGreeting;
    private RecyclerView recyclerCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private int currentPage = 1;
    private int pageSize = 4; // Số sản phẩm mỗi trang (2 cột x 2 dòng)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView iconChat = view.findViewById(R.id.icon_chat);
        iconChat.setOnClickListener(v -> {
            Fragment chatFragment = new ChatFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });

        textGreeting = view.findViewById(R.id.text_greeting);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour < 12) {
            greeting = "Chào buổi sáng!";
        } else if (hour < 18) {
            greeting = "Chào buổi chiều!";
        } else {
            greeting = "Chào buổi tối!";
        }

        textGreeting.setText(greeting);

        View sectionBooks = view.findViewById(R.id.section_top_books);
        TextView tvBookTitle = sectionBooks.findViewById(R.id.tvSectionTitle);
        tvBookTitle.setText("Phổ biến");

        RecyclerView recyclerBooks = sectionBooks.findViewById(R.id.recyclerSectionList);
        // Hiển thị sản phẩm phổ biến dạng lưới 2 cột
        TopBookAdapter topBookAdapter = new TopBookAdapter(productList);
        recyclerBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerBooks.setAdapter(topBookAdapter);

        // Thêm nút chuyển trang
        View btnPrev = sectionBooks.findViewById(R.id.btnPrevPage);
        View btnNext = sectionBooks.findViewById(R.id.btnNextPage);
        if (btnPrev != null && btnNext != null) {
            btnPrev.setOnClickListener(v -> {
                if (currentPage > 1) {
                    currentPage--;
                    fetchProducts();
                }
            });
            btnNext.setOnClickListener(v -> {
                currentPage++;
                fetchProducts();
            });
        }

        recyclerCategories = view.findViewById(R.id.recycler_categories);
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCategories.setAdapter(categoryAdapter);
        fetchCategories();
        fetchProducts();

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
        View sectionBooks = getView().findViewById(R.id.section_top_books);
        RecyclerView recyclerBooks = sectionBooks.findViewById(R.id.recyclerSectionList);
        TopBookAdapter topBookAdapter = new TopBookAdapter(productList);
        recyclerBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerBooks.setAdapter(topBookAdapter);
    }

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
        private final List<Category> categories;
        public CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }
        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setPadding(32, 16, 32, 16);
            tv.setTextSize(16);
            tv.setTextColor(0xFF1976D2); // blue800
            return new CategoryViewHolder(tv);
        }
        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            holder.textView.setText(categories.get(position).getName());
        }
        @Override
        public int getItemCount() {
            return categories.size();
        }
        static class CategoryViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private final List<Product> products;
        public ProductAdapter(List<Product> products) {
            this.products = products;
        }
        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Product product = products.get(position);
            holder.textProductName.setText(product.getName());
            holder.textProductPrice.setText(product.getPrice() + "đ");
            holder.textProductStatus.setText(product.getStatus());
            // Nếu có ảnh, load ảnh đầu tiên nếu có
            if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
                // TODO: Lấy link ảnh từ productImages nếu có
                // Glide.with(holder.imageProduct.getContext()).load(product.getProductImages().get(0)).into(holder.imageProduct);
                holder.imageProduct.setImageResource(R.drawable.ic_launcher_foreground); // placeholder
            } else {
                holder.imageProduct.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
        @Override
        public int getItemCount() {
            return products.size();
        }
        class ProductViewHolder extends RecyclerView.ViewHolder {
            ImageView imageProduct;
            TextView textProductName, textProductPrice, textProductStatus;
            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                imageProduct = itemView.findViewById(R.id.imageProduct);
                textProductName = itemView.findViewById(R.id.textProductName);
                textProductPrice = itemView.findViewById(R.id.textProductPrice);
                textProductStatus = itemView.findViewById(R.id.textProductStatus);
            }
        }
    }

    private class TopBookAdapter extends RecyclerView.Adapter<TopBookAdapter.TopBookViewHolder> {
        private final List<Product> books;
        public TopBookAdapter(List<Product> books) {
            this.books = books;
        }
        @NonNull
        @Override
        public TopBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_book, parent, false);
            return new TopBookViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull TopBookViewHolder holder, int position) {
            Product book = books.get(position);
            Log.d("AdapterBind", "Bind sản phẩm: " + book.getName() + ", giá: " + book.getPrice());
            holder.tvTitle.setText(book.getName());
            holder.tvPrice.setText(book.getPrice() + "đ");
            if (book.getProductImages() != null && !book.getProductImages().isEmpty()) {
                holder.imgBook.setImageResource(R.drawable.ic_launcher_foreground);
            } else {
                holder.imgBook.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
        @Override
        public int getItemCount() {
            Log.d("AdapterBind", "getItemCount: " + books.size());
            return books.size();
        }
        class TopBookViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvPrice;
            ImageView imgBook;
            public TopBookViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvBookTitle);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                imgBook = itemView.findViewById(R.id.imgBook);
            }
        }
    }
}

