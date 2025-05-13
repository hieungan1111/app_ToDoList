package com.example.todolist.dao;

import com.example.todolist.model.Alarm;
import java.util.List;

public interface AlarmDAO {
    long addAlarm(Alarm alarm);
    Alarm getAlarmById(int id);
    List<Alarm> getAllAlarms();
    void updateAlarm(Alarm alarm);
    void deleteAlarm(int id);
}
