package com.example.koreanshopee.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.R;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private Context context;
    private List<Conversation> conversations;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Conversation conversation);
    }

    public ConversationAdapter(Context context, List<Conversation> conversations, OnItemClickListener listener) {
        this.context = context;
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation convo = conversations.get(position);
        holder.textUserName.setText(convo.getUserName());
        holder.textLastMessage.setText(convo.getLastMessage());
        holder.imageAvatar.setImageResource(convo.getAvatarResId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(convo));
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName, textLastMessage;
        ImageView imageAvatar;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);
            textLastMessage = itemView.findViewById(R.id.textLastMessage);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
        }
    }
}

