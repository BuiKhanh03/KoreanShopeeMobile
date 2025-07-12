package com.example.koreanshopee.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.Category;
import com.example.koreanshopee.model.CategoryListResponse;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.koreanshopee.ui.main.ProductByCategoryActivity;

public class ShopsFragment extends Fragment {
    private RecyclerView recyclerCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();

    public ShopsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops, container, false);
        recyclerCategories = view.findViewById(R.id.recycler_categories);
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCategories.setAdapter(categoryAdapter);
        fetchCategories();
        return view;
    }

    private void fetchCategories() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body().getValue());
                    categoryAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
        private final List<Category> categories;
        public CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }
        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }
        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            Category category = categories.get(position);
            holder.tvCategoryName.setText(category.getName());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), ProductByCategoryActivity.class);
                intent.putExtra("categoryId", category.getId());
                holder.itemView.getContext().startActivity(intent);
            });
        }
        @Override
        public int getItemCount() {
            return categories.size();
        }
        static class CategoryViewHolder extends RecyclerView.ViewHolder {
            TextView tvCategoryName;
            public CategoryViewHolder(View itemView) {
                super(itemView);
                tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            }
        }
    }
}
