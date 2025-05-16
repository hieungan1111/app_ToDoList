package com.example.todolist.model;

import java.util.Date;
import java.util.List;

public class Subject {
    public int id;
    public String rangeStart;
    public String rangeEnd;
    public String timeStart;
    public String timeEnd;
    public String subjectName;
    public List<String> weekDays;
    public int userId;

    public Subject(String rangeStart, String rangeEnd, String timeStart, String timeEnd, String subjectName, List<String> weekDays, int userId) {

        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.subjectName = subjectName;
        this.weekDays = weekDays;
        this.userId = userId;
    }

    public Subject( int id ,String rangeStart, String rangeEnd, String timeStart, String timeEnd, String subjectName,List<String> weekDays, int userId) {
        this.id = id;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.subjectName = subjectName;
        this.weekDays = weekDays;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
