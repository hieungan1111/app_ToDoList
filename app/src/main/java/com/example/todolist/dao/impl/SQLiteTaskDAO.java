package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.todolist.dao.TaskDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteTaskDAO implements TaskDAO {

    private final SQLiteDatabase db;
    private final DatabaseHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteTaskDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public int addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("day", dateFormat.format(task.getDay()));
        values.put("timeStart", dateFormat.format(task.getTimeStart()));
        values.put("content", task.getContent());
        values.put("isDone", task.isDone() ? 1 : 0);
        values.put("userId", task.getUserId());
        values.put("taskPriority", task.getTaskPriority().name());
        values.put("isHidden", task.isHidden() ? 1 : 0);
        values.put("type", task.getType());

        long id = db.insert("Task", null, values);
        task.setId((int) id);  // 👈 Gán lại ID vào đối tượng Task
        return (int) id;
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
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Task WHERE userId = ?";
        Log.d("DEBUG", "Executing SQL: " + query + " [userId=" + userId + "]");
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                task.setDay(parseDate(cursor.getString(cursor.getColumnIndexOrThrow("day"))));
                task.setTimeStart(parseDate(cursor.getString(cursor.getColumnIndexOrThrow("timeStart"))));
                task.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
                task.setDone(cursor.getInt(cursor.getColumnIndexOrThrow("isDone")) == 1);
                task.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("userId")));
                task.setTaskPriority(Task.TaskPriority.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("taskPriority"))));
                task.setHidden(cursor.getInt(cursor.getColumnIndexOrThrow("isHidden")) == 1);
                task.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return taskList;
    }

    public List<Task> getTaskDoneByUserId(int userId) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Task WHERE isDone = 1 AND userId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Task task = extractTaskFromCursor(cursor);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskList;
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
        values.put("type", task.getType());  // 👈 thêm dòng này
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
        Task.TaskPriority taskPriority = Task.TaskPriority.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("taskPriority")));
        boolean isHidden = cursor.getInt(cursor.getColumnIndexOrThrow("isHidden")) == 1;
        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));  // 👈 thêm dòng này

        return new Task(id, day, timeStart, content, isDone, userId, taskPriority, isHidden, type);  // 👈 truyền type
    }

    private Date parseDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            return new Date();
        }
    }
    public List<Task> getTasksByUserIdAndType(int userId, String type) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Task WHERE userId = ? AND type = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), type});

        if (cursor.moveToFirst()) {
            do {
                Task task = extractTaskFromCursor(cursor);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return taskList;
    }

    public List<Task> getTaskDone() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Task WHERE isDone = 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = extractTaskFromCursor(cursor);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskList;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Task";
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Task task = extractTaskFromCursor(cursor);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return taskList;
    }
}
