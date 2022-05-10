package com.example.bmarket02;

public class Saved {
    private String item_id, user_phone;

    public Saved(String item_id, String user_phone) {
        this.item_id = item_id;
        this.user_phone = user_phone;
    }

    public Saved() {
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}
