package com.example.todolist.model;

import java.util.Date;
import java.util.List;

public class Alarm {
    public int id;
    public String alarmName;
    public Date time;
    public List<String> repeatDays;
    public boolean isEnable;
    public Date createAt;
    public int userId;

    public Alarm(int id, String alarmName, Date time, List<String> repeatDays, boolean isEnable, Date createAt, int userId) {
        this.id = id;
        this.alarmName = alarmName;
        this.time = time;
        this.repeatDays = repeatDays;
        this.isEnable = isEnable;
        this.createAt = createAt;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<String> getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(List<String> repeatDays) {
        this.repeatDays = repeatDays;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

