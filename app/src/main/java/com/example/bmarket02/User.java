package com.example.bmarket02;

public class User {
    private String about, country, grade, name, password, phone, school, status;

    public User(String about, String country, String grade, String name, String password, String phone, String school, String status) {
        this.about = about;
        this.country = country;
        this.grade = grade;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.school = school;
        this.status = status;
    }

    public User() {
    }

    public String getAbout() {
        return about;
    }

    public String getCountry() {
        return country;
    }

    public String getGrade() {
        return grade;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getSchool() {
        return school;
    }

    public String getStatus() {
        return status;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
