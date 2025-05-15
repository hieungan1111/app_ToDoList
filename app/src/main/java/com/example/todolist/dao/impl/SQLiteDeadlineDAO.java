package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.DeadlineDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Deadline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteDeadlineDAO implements DeadlineDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteDeadlineDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addDeadline(Deadline deadline) {
        ContentValues values = new ContentValues();
        values.put("day", (deadline.getDay()));
        values.put("timeEnd",(deadline.getTimeEnd()));
        values.put("subject", deadline.getSubject());
        values.put("subjectId", deadline.getIdSubject());
        values.put("deadlineName", deadline.getDeadlineName());
        values.put("isDone", deadline.isDone() ? 1 : 0);
        values.put("userId", deadline.getUserId());
        db.insert("Deadline", null, values);
    }

    @Override
    public Deadline getDeadlineById(int id) {
        Cursor cursor = db.query("Deadline", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Deadline deadline = extractDeadlineFromCursor(cursor);
            cursor.close();
            return deadline;
        }
        cursor.close();
        return null;
    }

    @Override
    public List<Deadline> getAllDeadlinesByUserId(int userId) {
        List<Deadline> list = new ArrayList<>();
        Cursor cursor = db.query("Deadline", null, "userId = ?", new String[]{String.valueOf(userId)}, null, null, null);
        while (cursor.moveToNext()) {
            list.add(extractDeadlineFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateDeadline(Deadline deadline) {
        ContentValues values = new ContentValues();
        values.put("day", (deadline.getDay()));
        values.put("timeEnd", (deadline.getTimeEnd()));
        values.put("subject", deadline.getSubject());
        values.put("subjectId", deadline.getIdSubject());
        values.put("deadlineName", deadline.getDeadlineName());
        values.put("isDone", deadline.isDone() ? 1 : 0);
        values.put("userId", deadline.getUserId());
        db.update("Deadline", values, "id = ?", new String[]{String.valueOf(deadline.getId())});
    }


    @Override
    public void deleteDeadline(int id) {
        db.delete("Deadline", "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void updateStatus(int id, boolean done) {
        ContentValues values = new ContentValues();
        values.put("isDone", done ? 1 : 0);
        db.update("Deadline", values, "id = ?", new String[]{String.valueOf(id)});
    }

    private Deadline extractDeadlineFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String day = (cursor.getString(cursor.getColumnIndexOrThrow("day")));
        String timeEnd = (cursor.getString(cursor.getColumnIndexOrThrow("timeEnd")));
        String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
        int idSubject = cursor.getInt(cursor.getColumnIndexOrThrow("subjectId"));
        String deadlineName = cursor.getString(cursor.getColumnIndexOrThrow("deadlineName"));
        boolean isDone = cursor.getInt(cursor.getColumnIndexOrThrow("isDone")) == 1;
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

        return new Deadline(id, day, timeEnd, subject, idSubject, deadlineName, isDone, userId);
    }


}
