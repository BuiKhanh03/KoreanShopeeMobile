package com.example.koreanshopee.ui.main;

public class Conversation {
    private String userId;
    private String userName;
    private String lastMessage;
    private int avatarResId;
    public Conversation(String userId, String userName, int avatarResId, String lastMessage) {
        this.userId = userId;
        this.userName = userName;
        this.avatarResId = avatarResId;
        this.lastMessage = lastMessage;
    }
    public String getUserId() { return userId; }

    public String getUserName() {
        return userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}

