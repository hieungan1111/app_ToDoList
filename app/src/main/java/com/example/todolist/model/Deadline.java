package com.example.todolist.model;

import java.util.Date;

public class Deadline {
    public int id;
    public String day;
    public String timeEnd;
    public String subject;
    public String idSubject;
    public String deadlineName;
    public boolean isDone;
    public int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(String idSubject) {
        this.idSubject = idSubject;
    }

    public String getDeadlineName() {
        return deadlineName;
    }

    public void setDeadlineName(String deadlineName) {
        this.deadlineName = deadlineName;
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

    public Deadline(int id, String day, String timeEnd, String subject, String idSubject, String deadlineName, boolean isDone, int userId) {
        this.id = id;
        this.day = day;
        this.timeEnd = timeEnd;
        this.subject = subject;
        this.idSubject = idSubject;
        this.deadlineName = deadlineName;
        this.isDone = isDone;
        this.userId = userId;

    }
}
