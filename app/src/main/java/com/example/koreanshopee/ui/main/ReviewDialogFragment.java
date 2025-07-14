package com.example.koreanshopee.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.ReviewRequest;
import com.example.koreanshopee.model.ReviewResponse;
import com.example.koreanshopee.utils.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDialogFragment extends DialogFragment {
    private static final String ARG_PRODUCT_ID = "product_id";
    private String productId;
    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitButton;
    private Button cancelButton;

    public static ReviewDialogFragment newInstance(String productId) {
        ReviewDialogFragment fragment = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString(ARG_PRODUCT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_review, container, false);
        
        ratingBar = view.findViewById(R.id.ratingBar);
        commentEditText = view.findViewById(R.id.commentEditText);
        submitButton = view.findViewById(R.id.submitButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        submitButton.setOnClickListener(v -> submitReview());
        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void submitReview() {
        float rating = ratingBar.getRating();
        String comment = commentEditText.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(getContext(), "Vui lòng chọn đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (comment.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập nhận xét!", Toast.LENGTH_SHORT).show();
            return;
        }

        ReviewRequest reviewRequest = new ReviewRequest(productId, (int) rating, comment);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        TokenManager tokenManager = new TokenManager(getContext());
        String token = "Bearer " + tokenManager.getToken();
        api.createReview(token, reviewRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đánh giá đã được gửi!", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Lỗi gửi đánh giá!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 