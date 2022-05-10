package com.example.bmarket02;

import java.io.Serializable;

public class Item implements Serializable, Comparable {

    private Boolean active;
    private String activity_type, item_author, item_condition, item_description, item_grade, item_id, item_name, item_price, item_subject, item_upload_time, school, user_phone;

    public Item(Boolean active, String activity_type, String item_author, String item_condition, String item_description, String item_grade, String item_id, String item_name, String item_price, String item_subject, String item_upload_time, String school, String user_phone) {
        this.active = active;
        this.activity_type = activity_type;
        this.item_author = item_author;
        this.item_condition = item_condition;
        this.item_description = item_description;
        this.item_grade = item_grade;
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_subject = item_subject;
        this.item_upload_time = item_upload_time;
        this.school = school;
        this.user_phone = user_phone;
    }

    public Item() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getItem_author() {
        return item_author;
    }

    public void setItem_author(String item_author) {
        this.item_author = item_author;
    }

    public String getItem_condition() {
        return item_condition;
    }

    public void setItem_condition(String item_condition) {
        this.item_condition = item_condition;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_grade() {
        return item_grade;
    }

    public void setItem_grade(String item_grade) {
        this.item_grade = item_grade;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_subject() {
        return item_subject;
    }

    public void setItem_subject(String item_subject) {
        this.item_subject = item_subject;
    }

    public String getItem_upload_time() {
        return item_upload_time;
    }

    public void setItem_upload_time(String item_upload_time) {
        this.item_upload_time = item_upload_time;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }


    @Override
    public int compareTo(Object o) {
        int compareage = Integer.parseInt(((Item)o).getItem_price());
        /* For Ascending order*/
        return Integer.parseInt(this.item_price) - compareage;
    }
}
