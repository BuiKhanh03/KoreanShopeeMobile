package com.example.koreanshopee.ui.seller;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.Category;
import com.example.koreanshopee.model.CategoryListResponse;
import com.example.koreanshopee.model.CreateProductRequest;
import com.example.koreanshopee.model.ProductResponse;
import com.example.koreanshopee.utils.TokenManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private EditText etProductName, etProductDescription, etProductPrice, etProductBrand;
    private Spinner spinnerCategory;
    private Button btnAddProduct;
    private List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm sản phẩm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadCategories();
        setupListeners();
    }

    private void initViews() {
        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductBrand = findViewById(R.id.etProductBrand);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddProduct = findViewById(R.id.btnAddProduct);
    }

    private void setupListeners() {
        btnAddProduct.setOnClickListener(v -> addProduct());
    }

    private void loadCategories() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body().getValue());
                    
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categoryList) {
                        categoryNames.add(category.getName());
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductActivity.this, 
                        android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct() {
        String name = etProductName.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();
        String priceStr = etProductPrice.getText().toString().trim();
        String brand = etProductBrand.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || brand.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerCategory.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Vui lòng chọn danh mục!", Toast.LENGTH_SHORT).show();
            return;
        }

        Category selectedCategory = categoryList.get(spinnerCategory.getSelectedItemPosition());
        
        // Tạo product sizes mặc định
        List<CreateProductRequest.ProductSizeRequest> productSizes = new ArrayList<>();
        productSizes.add(new CreateProductRequest.ProductSizeRequest("Mặc định"));

        CreateProductRequest request = new CreateProductRequest(
            selectedCategory.getId(),
            name,
            description,
            price,
            brand,
            productSizes
        );

        TokenManager tokenManager = new TokenManager(this);
        String token = "Bearer " + tokenManager.getToken();

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.createProduct(token, request).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, "Lỗi thêm sản phẩm!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 