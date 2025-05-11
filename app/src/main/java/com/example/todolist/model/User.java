package com.example.todolist.model;

import java.util.Calendar;
import java.util.Date;

public class User {
    public int id;
    public String fullname;
    public String email;
    public Date createAt;
    public String fcmToken;
    public String birthday;
    public String gender;
    public String avatarUrl;
    public int is_verified;
    public String password;

    public User(int id, String fullname, String email, Date createAt, String fcmToken, String birthday, String gender, String avatarUrl, String password, int is_verified) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.createAt = createAt;
        this.fcmToken = fcmToken;
        this.birthday = birthday;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.is_verified=is_verified;
        this.password=password;
    }

    public User(String fullname, String email, String fcmToken, int is_verified, String password) {
        this.fullname = fullname;
        this.email = email;
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        this.createAt=currentDate;
        this.fcmToken = fcmToken;
        this.is_verified = is_verified;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", createAt=" + createAt +
                ", fcmToken='" + fcmToken + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", is_verified=" + is_verified +
                ", password='" + password + '\'' +
                '}';
    }
}
