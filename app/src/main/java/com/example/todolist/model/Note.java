package com.example.todolist.model;

import java.util.Date;

public class Note {
    public int id;
    public String title;
    public String content;
    public String category;
    public Date createAt;
    public Date updateAt;
    public String themeColor;
    public String fontColor;
    public int fontSize;
    public boolean isHidden;
    public int userId;
    public String categoryId;

    public Note() {
    }

    public Note(int id, String title, String content, String category, Date createAt, Date updateAt, String themeColor, String fontColor, int fontSize, boolean isHidden, int userId, String categoryId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.themeColor = themeColor;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
        this.isHidden = isHidden;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
