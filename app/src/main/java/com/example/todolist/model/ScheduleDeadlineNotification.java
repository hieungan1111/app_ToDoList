package com.example.todolist.model;

import java.util.Date;

public class ScheduleDeadlineNotification {
    public int deadlineId;
    public Date notificationTime;
    public boolean sent;
    public String subject;
    public String title;
    public String type;
    public int userId;

    public ScheduleDeadlineNotification(int deadlineId, Date notificationTime, boolean sent, String subject, String title, String type, int userId) {
        this.deadlineId = deadlineId;
        this.notificationTime = notificationTime;
        this.sent = sent;
        this.subject = subject;
        this.title = title;
        this.type = type;
        this.userId = userId;
    }

    public int getDeadlineId() {
        return deadlineId;
    }

    public void setDeadlineId(int deadlineId) {
        this.deadlineId = deadlineId;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
