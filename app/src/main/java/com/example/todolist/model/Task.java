package com.example.todolist.model;

import java.util.Date;

public class Task {
    public int id;
    public Date day;
    public Date timeStart;
    public String content;
    public boolean isDone;
    public int userId;
    public TaskPriority taskPriority;
    public boolean isHidden;

    public enum TaskPriority {
        LOW, MEDIUM, HIGH
    }

    public Task(int id, Date day, Date timeStart, String content, boolean isDone, int userId, TaskPriority taskPriority, boolean isHidden) {
        this.id = id;
        this.day = day;
        this.timeStart = timeStart;
        this.content = content;
        this.isDone = isDone;
        this.userId = userId;
        this.taskPriority = taskPriority;
        this.isHidden = isHidden;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
}
