package com.example.koreanshopee.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.Chat.Conversation;
import com.example.koreanshopee.Chat.ConversationAdapter;

import com.example.koreanshopee.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewConversations);
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.add(new Conversation("Nguyễn Văn A", "Hẹn gặp bạn lúc 3h nhé", R.drawable.avt1));
        conversationList.add(new Conversation("Trần Thị B", "Bạn ăn cơm chưa?", R.drawable.avt2));
        conversationList.add(new Conversation("Group Học Tiếng Hàn", "Thầy gửi file bài giảng rồi nha", R.drawable.avt3));

        ConversationAdapter adapter = new ConversationAdapter(getContext(), conversationList, conversation -> {
            // Khi click → mở ChatFragment
            Fragment chatFragment = new ChatFragment(); // Có thể truyền Bundle nếu cần
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
