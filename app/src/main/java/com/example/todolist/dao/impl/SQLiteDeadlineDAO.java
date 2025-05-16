package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.alarm.AlarmUtils;
import com.example.todolist.dao.DeadlineDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Deadline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteDeadlineDAO implements DeadlineDAO {

    private final Context context;
    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteDeadlineDAO(Context context) {
        this.context = context.getApplicationContext();
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
//        db.insert("Deadline", null, values);

        long insertedId = db.insert("Deadline", null, values);
        deadline.setId((int) insertedId);

        // Đặt lịch báo thức
        AlarmUtils.scheduleDeadlineReminder(context, deadline);
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

        AlarmUtils.cancelDeadlineReminder(context, deadline.id);
        AlarmUtils.scheduleDeadlineReminder(context, deadline);
    }


    @Override
    public void deleteDeadline(int id) {
        AlarmUtils.cancelDeadlineReminder(context, id);
        db.delete("Deadline", "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void updateStatus(int id, boolean done) {
        ContentValues values = new ContentValues();
        values.put("isDone", done ? 1 : 0);
        db.update("Deadline", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Thêm phương thức mới để lấy deadline theo subjectId
    public List<Deadline> getDeadlinesBySubjectId(int subjectId) {
        List<Deadline> list = new ArrayList<>();
        Cursor cursor = db.query("Deadline", null, "subjectId = ?", new String[]{String.valueOf(subjectId)}, null, null, null);
        while (cursor.moveToNext()) {
            list.add(extractDeadlineFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    // Thêm phương thức mới để lọc deadline theo subjectId, ngày, và trạng thái
    public List<Deadline> getDeadlinesByFilters(int userId, Integer subjectId, String dueDate, Integer status) {
        List<Deadline> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM Deadline WHERE userId = ?");
        List<String> args = new ArrayList<>();
        args.add(String.valueOf(userId));

        if (subjectId != null) {
            query.append(" AND subjectId = ?");
            args.add(String.valueOf(subjectId));
        }
        if (dueDate != null) {
            query.append(" AND day <= ?");
            args.add(dueDate);
        }
        if (status != null) {
            query.append(" AND isDone = ?");
            args.add(status == 1 ? "1" : "0");
        }

        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        while (cursor.moveToNext()) {
            list.add(extractDeadlineFromCursor(cursor));
        }
        cursor.close();
        return list;
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

    public List<Deadline> getCompletedDeadlineByUserId(int userId){
        List<Deadline> list = new ArrayList<>();

        String query = "SELECT * FROM Deadline WHERE isDone = 1 AND userId = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            list.add(extractDeadlineFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

}
