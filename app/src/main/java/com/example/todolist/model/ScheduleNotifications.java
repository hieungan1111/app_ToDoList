package com.example.todolist.model;

import java.util.Date;

public class ScheduleNotifications {
    public Date notificationTime;
    public boolean sent;
    public int taskId;
    public String title;
    public int userId;
    public boolean viewed;

    public ScheduleNotifications(Date notificationTime, boolean sent, int taskId, String title, int userId,boolean viewed) {
        this.notificationTime = notificationTime;
        this.sent = sent;
        this.taskId = taskId;
        this.title = title;
        this.userId = userId;
        this.viewed = viewed;
    }
    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
