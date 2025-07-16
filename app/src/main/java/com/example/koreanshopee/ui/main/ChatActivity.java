package com.example.koreanshopee.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.R;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements ConversationAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ConversationAdapter adapter;
    private List<Conversation> chatUsers;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.recycler_chat_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        chatUsers = new ArrayList<>();
        chatUsers.add(new Conversation("1", "Trang", R.drawable.ic_user, "Xin chào!"));
        chatUsers.add(new Conversation("2", "Minh", R.drawable.ic_user, "Khi nào giao hàng?"));

        adapter = new ConversationAdapter(chatUsers, this);
        recyclerView.setAdapter(adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void onItemClick(@NonNull Conversation user) {
        Intent intent = new Intent(this, ChatDetailActivity.class);
        intent.putExtra("username", user.getUserName());
        startActivity(intent);
    }

}