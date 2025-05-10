package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.TaskDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteTaskDAO implements TaskDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteTaskDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("day", dateFormat.format(task.getDay()));
        values.put("timeStart", dateFormat.format(task.getTimeStart()));
        values.put("content", task.getContent());
        values.put("isDone", task.isDone() ? 1 : 0);
        values.put("userId", task.getUserId());
        values.put("taskPriority", task.getTaskPriority().name());
        values.put("isHidden", task.isHidden() ? 1 : 0);
        db.insert("Task", null, values);
    }

    @Override
    public Task getTaskById(int id) {
        Cursor cursor = db.query("Task", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Task task = extractTaskFromCursor(cursor);
            cursor.close();
            return task;
        }
        cursor.close();
        return null;
    }

    @Override
    public List<Task> getAllTasksByUserId(int userId) {
        List<Task> list = new ArrayList<>();
        Cursor cursor = db.query("Task", null, "userId = ?", new String[]{String.valueOf(userId)}, null, null, "day ASC, timeStart ASC");
        while (cursor.moveToNext()) {
            list.add(extractTaskFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("day", dateFormat.format(task.getDay()));
        values.put("timeStart", dateFormat.format(task.getTimeStart()));
        values.put("content", task.getContent());
        values.put("isDone", task.isDone() ? 1 : 0);
        values.put("userId", task.getUserId());
        values.put("taskPriority", task.getTaskPriority().name());
        values.put("isHidden", task.isHidden() ? 1 : 0);
        db.update("Task", values, "id = ?", new String[]{String.valueOf(task.getId())});
    }

    @Override
    public void deleteTask(int id) {
        db.delete("Task", "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void markTaskDone(int id, boolean done) {
        ContentValues values = new ContentValues();
        values.put("isDone", done ? 1 : 0);
        db.update("Task", values, "id = ?", new String[]{String.valueOf(id)});
    }

    private Task extractTaskFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        Date day = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("day")));
        Date timeStart = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("timeStart")));
        String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        boolean isDone = cursor.getInt(cursor.getColumnIndexOrThrow("isDone")) == 1;
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
        String priorityStr = cursor.getString(cursor.getColumnIndexOrThrow("taskPriority"));
        Task.TaskPriority taskPriority = Task.TaskPriority.valueOf(priorityStr);
        boolean isHidden = cursor.getInt(cursor.getColumnIndexOrThrow("isHidden")) == 1;

        return new Task(id, day, timeStart, content, isDone, userId, taskPriority, isHidden);
    }

    private Date parseDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            return new Date();
        }
    }
}
