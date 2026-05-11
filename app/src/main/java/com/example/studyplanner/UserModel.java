package com.example.studyplanner;

public class UserModel {
    private String phone;
    private long createdAt;

    public UserModel() {
    }

    public UserModel(String phone, long createdAt) {
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}