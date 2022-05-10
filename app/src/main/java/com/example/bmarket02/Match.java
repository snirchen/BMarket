package com.example.bmarket02;

public class Match {
    private String comment, item_id, user_phone;

    public Match(String comment, String item_id, String user_phone) {
        this.comment = comment;
        this.item_id = item_id;
        this.user_phone = user_phone;
    }

    public Match() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
