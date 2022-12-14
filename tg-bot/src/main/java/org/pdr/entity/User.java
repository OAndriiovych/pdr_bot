package org.pdr.entity;

import org.pdr.utils.DataStructure;

@DataStructure
public class User {


    private final long chatId;
    private long userId;
    private String phone = null;

    private String userName = null;
    private boolean isPrem = false;
    private boolean isUserAnonymous = true;

    public User(long chatId) {
        this.chatId = chatId;
    }

    public User(long chatId, long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }


    public User(long chatId, long userId, String phone, String userName) {
        this.chatId = chatId;
        this.userId = userId;
        this.phone = phone;
        this.userName = userName;
        this.isUserAnonymous = false;
    }

    public long getChatId() {
        return chatId;
    }

    public long getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPrem(boolean prem) {
        isPrem = prem;
    }

    public boolean isPrem() {
        return isPrem;
    }

    public boolean isUserAnonymous() {
        return isUserAnonymous;
    }
}
