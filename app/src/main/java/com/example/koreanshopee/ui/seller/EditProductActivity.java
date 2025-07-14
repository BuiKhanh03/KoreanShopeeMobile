package com.example.koreanshopee.ui.seller;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.koreanshopee.R;

public class EditProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chỉnh sửa sản phẩm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String productId = getIntent().getStringExtra("productId");
        Toast.makeText(this, "Chức năng chỉnh sửa sản phẩm sẽ được thêm sau!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 