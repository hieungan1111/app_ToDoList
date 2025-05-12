package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.AlarmDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SQLiteAlarmDAO implements AlarmDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteAlarmDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addAlarm(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put("alarmName", alarm.getAlarmName());
        values.put("time", dateFormat.format(alarm.getTime()));
        values.put("repeatDays", String.join(",", alarm.getRepeatDays())); // chuyển List thành chuỗi
        values.put("isEnable", alarm.isEnable() ? 1 : 0);
        values.put("createAt", dateFormat.format(alarm.getCreateAt()));
        values.put("userId", alarm.getUserId());
        db.insert("Alarm", null, values);
    }
    @Override
    public Alarm getAlarmById(int id) {
        Cursor cursor = db.query("Alarm", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Alarm alarm = extractAlarmFromCursor(cursor);
            cursor.close();
            return alarm;
        }
        cursor.close();
        return null;
    }

    @Override
    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<>();
        Cursor cursor = db.query("Alarm", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            alarms.add(extractAlarmFromCursor(cursor));
        }
        cursor.close();
        return alarms;
    }

    @Override
    public void updateAlarm(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put("alarmName", alarm.getAlarmName());
        values.put("time", dateFormat.format(alarm.getTime()));
        values.put("repeatDays", String.join(",", alarm.getRepeatDays())); // lưu lại dưới dạng "Mon,Tue"
        values.put("isEnable", alarm.isEnable() ? 1 : 0);
        values.put("createAt", dateFormat.format(alarm.getCreateAt()));
        values.put("userId", alarm.getUserId());
        db.update("Alarm", values, "id = ?", new String[]{String.valueOf(alarm.getId())});
    }

    @Override
    public void deleteAlarm(int id) {
        db.delete("Alarm", "id = ?", new String[]{String.valueOf(id)});
    }

    private Alarm extractAlarmFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String alarmName = cursor.getString(cursor.getColumnIndexOrThrow("alarmName"));
        Date time = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        String repeatString = cursor.getString(cursor.getColumnIndexOrThrow("repeatDays"));
        List<String> repeatDays = Arrays.asList(repeatString.split(",")); // tách chuỗi thành List
        boolean isEnable = cursor.getInt(cursor.getColumnIndexOrThrow("isEnable")) == 1;
        Date createAt = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("createAt")));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

        return new Alarm(id, alarmName, time, repeatDays, isEnable, createAt, userId);
    }

    private Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            return new Date(); // fallback
        }
    }
}
