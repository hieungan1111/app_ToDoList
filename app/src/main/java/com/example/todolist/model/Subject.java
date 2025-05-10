package com.example.todolist.model;

import java.util.Date;
import java.util.List;

public class Subject {
    public int id;
    public Date rangeStart;
    public Date rangeEnd;
    public Date timeStart;
    public Date timeEnd;
    public String subjectName;
    public String subjectColor;
    public List<String> weekDays;
    public int userId;

    public Subject(int id, Date rangeStart, Date rangeEnd, Date timeStart, Date timeEnd, String subjectName, String subjectColor, List<String> weekDays, int userId) {
        this.id = id;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.subjectName = subjectName;
        this.subjectColor = subjectColor;
        this.weekDays = weekDays;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(Date rangeStart) {
        this.rangeStart = rangeStart;
    }

    public Date getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(Date rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectColor() {
        return subjectColor;
    }

    public void setSubjectColor(String subjectColor) {
        this.subjectColor = subjectColor;
    }

    public List<String> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<String> weekDays) {
        this.weekDays = weekDays;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
