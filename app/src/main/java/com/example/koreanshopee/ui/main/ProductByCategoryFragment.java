package com.example.koreanshopee.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.Category;
import com.example.koreanshopee.model.CategoryListResponse;
import com.example.koreanshopee.model.CategoryResponse;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductListResponse;
import com.example.koreanshopee.Item;
import com.example.koreanshopee.utils.TokenManager;
import com.example.koreanshopee.ItemAdapter;
import com.example.koreanshopee.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductByCategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> productList = new ArrayList<>();
    private String categoryId;

    private ImageView btnBack;

    public static ProductByCategoryFragment newInstance(String categoryId) {
        ProductByCategoryFragment fragment = new ProductByCategoryFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_by_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProductByCategory);
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new ItemAdapter(getContext(), productList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product_id", item.getId());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Item item) {
                addToCart(item);
            }

            @Override
            public void onReviewClick(Item item) {
                showReviewDialog(item);
            }
        });

        recyclerView.setAdapter(adapter);

        TextView tvCategoryTitle = view.findViewById(R.id.tv_category_title);

        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            if (categoryId != null) {
                fetchCategoryName(categoryId, tvCategoryTitle);  // Gọi API lấy tên danh mục
                loadProductsByCategory(categoryId);               // Gọi API lấy sản phẩm theo danh mục
            } else {
                Toast.makeText(getContext(), "Không tìm thấy categoryId", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void loadProductsByCategory(String categoryId) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getProductsByCategory(categoryId).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    productList.clear();
                    for (Product product : response.body().getValue()) {
                        Item item = new Item();
                        item.setId(product.getId());
                        item.setName(product.getName());
                        item.setPrice(product.getPrice());
                        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
                            item.setImage(product.getProductImages().get(0).toString());
                        }
                        item.setDescription(product.getDescription());
                        item.setCategoryId(product.getCategoryId());
                        productList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(Item item) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        TokenManager tokenManager = new TokenManager(requireContext());
        String token = "Bearer " + tokenManager.getToken();

        api.addToCart(token, item.getId(), 1).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showReviewDialog(Item item) {
        ReviewDialogFragment dialog = ReviewDialogFragment.newInstance(item.getId());
        dialog.show(getChildFragmentManager(), "ReviewDialog");
    }
    private void fetchCategoryName(String categoryId, TextView tvCategoryTitle) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCategoryById(categoryId).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvCategoryTitle.setText(response.body().getValue().getName());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi lấy tên danh mục", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
