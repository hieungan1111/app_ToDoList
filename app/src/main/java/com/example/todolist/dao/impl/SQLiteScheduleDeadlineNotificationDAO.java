package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.ScheduleDeadlineNotificationDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.ScheduleDeadlineNotification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteScheduleDeadlineNotificationDAO implements ScheduleDeadlineNotificationDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteScheduleDeadlineNotificationDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addNotification(ScheduleDeadlineNotification notification) {
        ContentValues values = new ContentValues();
        values.put("deadlineId", notification.getDeadlineId());
        values.put("notificationTime", dateFormat.format(notification.getNotificationTime()));
        values.put("sent", notification.isSent() ? 1 : 0);
        values.put("subject", notification.getSubject());
        values.put("title", notification.getTitle());
        values.put("type", notification.getType());
        values.put("userId", notification.getUserId());
        db.insert("scheduleDeadlineNotification", null, values);
    }

    @Override
    public List<ScheduleDeadlineNotification> getAllByUserId(int userId) {
        List<ScheduleDeadlineNotification> list = new ArrayList<>();
        Cursor cursor = db.query("scheduleDeadlineNotification", null, "userId = ?", new String[]{String.valueOf(userId)}, null, null, "notificationTime ASC");
        while (cursor.moveToNext()) {
            list.add(extractNotificationFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateSentStatus(int deadlineId, boolean sent) {
        ContentValues values = new ContentValues();
        values.put("sent", sent ? 1 : 0);
        db.update("scheduleDeadlineNotification", values, "deadlineId = ?", new String[]{String.valueOf(deadlineId)});
    }

    @Override
    public void deleteNotification(int deadlineId) {
        db.delete("scheduleDeadlineNotification", "deadlineId = ?", new String[]{String.valueOf(deadlineId)});
    }

    private ScheduleDeadlineNotification extractNotificationFromCursor(Cursor cursor) {
        int deadlineId = cursor.getInt(cursor.getColumnIndexOrThrow("deadlineId"));
        Date notificationTime = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("notificationTime")));
        boolean sent = cursor.getInt(cursor.getColumnIndexOrThrow("sent")) == 1;
        String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

        return new ScheduleDeadlineNotification(deadlineId, notificationTime, sent, subject, title, type, userId);
    }

    private Date parseDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            return new Date(); // fallback
        }
    }
}
