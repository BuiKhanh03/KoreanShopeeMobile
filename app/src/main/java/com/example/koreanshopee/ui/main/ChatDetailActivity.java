package com.example.koreanshopee.ui.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.koreanshopee.R;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private List<Message> messageList;
    private MessageAdapter adapter;
    private ImageView btnBack, btnDelete;
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_detail);

        recyclerView = findViewById(R.id.recycler_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        btnBack = findViewById(R.id.btn_back);
        btnDelete = findViewById(R.id.btn_delete_chat);
        tvUsername = findViewById(R.id.tv_username);

        String username = getIntent().getStringExtra("username");
        if (username != null) {
            tvUsername.setText(username);
        }

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                btnSend.performClick();
                return true;
            }
            return false;
        });


        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                messageList.add(new Message(text, true));
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
                etMessage.setText("");

                recyclerView.postDelayed(() -> {
                    messageList.add(new Message("Đây là phản hồi!", false));
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }, 1000);
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xoá đoạn chat")
                    .setMessage("Bạn có chắc muốn xoá toàn bộ đoạn chat với " + username + " không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        messageList.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Đã xoá đoạn chat", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
