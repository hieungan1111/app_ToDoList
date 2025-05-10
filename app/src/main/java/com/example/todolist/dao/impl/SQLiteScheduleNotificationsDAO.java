package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.ScheduleNotificationsDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.ScheduleNotifications;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteScheduleNotificationsDAO implements ScheduleNotificationsDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteScheduleNotificationsDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addNotification(ScheduleNotifications notification) {
        ContentValues values = new ContentValues();
        values.put("notificationTime", dateFormat.format(notification.getNotificationTime()));
        values.put("sent", notification.isSent() ? 1 : 0);
        values.put("taskId", notification.getTaskId());
        values.put("title", notification.getTitle());
        values.put("userId", notification.getUserId());
        db.insert("scheduleNotifications", null, values);
    }

    @Override
    public List<ScheduleNotifications> getAllByUserId(int userId) {
        List<ScheduleNotifications> list = new ArrayList<>();
        Cursor cursor = db.query("scheduleNotifications", null, "userId = ?", new String[]{String.valueOf(userId)}, null, null, "notificationTime ASC");
        while (cursor.moveToNext()) {
            list.add(extractNotificationFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateSentStatus(int taskId, boolean sent) {
        ContentValues values = new ContentValues();
        values.put("sent", sent ? 1 : 0);
        db.update("scheduleNotifications", values, "taskId = ?", new String[]{String.valueOf(taskId)});
    }

    @Override
    public void deleteNotificationByTaskId(int taskId) {
        db.delete("scheduleNotifications", "taskId = ?", new String[]{String.valueOf(taskId)});
    }

    private ScheduleNotifications extractNotificationFromCursor(Cursor cursor) {
        Date notificationTime = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("notificationTime")));
        boolean sent = cursor.getInt(cursor.getColumnIndexOrThrow("sent")) == 1;
        int taskId = cursor.getInt(cursor.getColumnIndexOrThrow("taskId"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

        return new ScheduleNotifications(notificationTime, sent, taskId, title, userId);
    }

    private Date parseDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            return new Date();
        }
    }
}
