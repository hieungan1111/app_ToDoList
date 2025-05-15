package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.SubjectDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Subject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SQLiteSubjectDAO implements SubjectDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteSubjectDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }


    @Override
    public void addSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put("rangeStart", (subject.getRangeStart()));
        values.put("rangeEnd", (subject.getRangeEnd()));
        values.put("timeStart", (subject.getTimeStart()));
        values.put("timeEnd", (subject.getTimeEnd()));
        values.put("subject", subject.getSubjectName());
        values.put("weekdays", String.join(",", subject.getWeekDays()));
        values.put("userId", subject.getUserId());
        db.insert("Subject", null, values);
    }

    @Override
    public Subject getSubjectById(int id) {
        Cursor cursor = db.query("Subject", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Subject subject = extractSubjectFromCursor(cursor);
            cursor.close();
            return subject;
        }
        cursor.close();
        return null;
    }



    @Override
    public List<Subject> getAllSubjectsByUserId(int userId) {
        List<Subject> list = new ArrayList<>();
        Cursor cursor = db.query("Subject", null, "userId = ?", new String[]{String.valueOf(userId)}, null, null, "rangeStart ASC");
        while (cursor.moveToNext()) {
            list.add(extractSubjectFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put("rangeStart",(subject.getRangeStart()));
        values.put("rangeEnd", (subject.getRangeEnd()));
        values.put("timeStart", (subject.getTimeStart()));
        values.put("timeEnd", (subject.getTimeEnd()));
        values.put("subject", subject.getSubjectName());
        values.put("weekdays", String.join(",", subject.getWeekDays()));
        values.put("userId", subject.getUserId());
        db.update("Subject", values, "id = ?", new String[]{String.valueOf(subject.getId())});
    }

    @Override
    public void deleteSubject(int id) {
        db.delete("Subject", "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public List<Subject> getSubjectsByWeekday(String weekday, String day) {
        List<Subject> list = new ArrayList<>();
        String selection = "userId = ? AND weekdays LIKE ? AND rangeStart <= ? AND rangeEnd >= ?";
        String[] selectionArgs = new String[]{String.valueOf(1), "%" + weekday + "%", day, day};
        Cursor cursor = db.query("Subject", null, selection, selectionArgs, null, null, "timeStart ASC");
        while (cursor.moveToNext()) {
            list.add(extractSubjectFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    private Subject extractSubjectFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String rangeStart = (cursor.getString(cursor.getColumnIndexOrThrow("rangeStart")));
        String rangeEnd = (cursor.getString(cursor.getColumnIndexOrThrow("rangeEnd")));
        String timeStart = (cursor.getString(cursor.getColumnIndexOrThrow("timeStart")));
        String timeEnd = (cursor.getString(cursor.getColumnIndexOrThrow("timeEnd")));
        String subjectName = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
        String subjectColor = cursor.getString(cursor.getColumnIndexOrThrow("subjectColor"));
        String weekdaysStr = cursor.getString(cursor.getColumnIndexOrThrow("weekdays"));
        List<String> weekDays = Arrays.asList(weekdaysStr.split(","));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

        return new Subject(id, rangeStart, rangeEnd, timeStart, timeEnd, subjectName, weekDays, userId);
    }


}
