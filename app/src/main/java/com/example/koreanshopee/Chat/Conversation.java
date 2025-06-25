package com.example.koreanshopee.Chat;

public class Conversation {
    private String userName;
    private String lastMessage;
    private int avatarResId;

    public Conversation(String userName, String lastMessage, int avatarResId) {
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.avatarResId = avatarResId;
    }

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

