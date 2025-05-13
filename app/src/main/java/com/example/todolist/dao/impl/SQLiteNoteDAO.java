package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.NoteDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SQLiteNoteDAO implements NoteDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteNoteDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addNote(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        //values.put("category", note.getCategory());
        values.put("createAt", dateFormat.format(note.getCreateAt()));
        values.put("updateAt", dateFormat.format(note.getUpdateAt()));
        values.put("themeColor", note.getThemeColor());
        values.put("fontColor", note.getFontColor());
        values.put("fontSize", note.getFontSize());
        values.put("isHidden", note.isHidden() ? 1 : 0);
        values.put("userId", note.getUserId());
        values.put("categoryId", note.getCategoryId());
        db.insert("Note", null, values);
    }

    @Override
    public Note getNoteById(int id) {
        Cursor cursor = db.query("Note", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Note note = extractNoteFromCursor(cursor);
            cursor.close();
            return note;
        }
        cursor.close();
        return null;
    }

    @Override
    public List<Note> getAllNotesByUserId(int userId) {
        List<Note> list = new ArrayList<>();
        Cursor cursor = db.query("Note", null, "userId = ?", new String[]{String.valueOf(userId)}, null, null, "createAt DESC");
        while (cursor.moveToNext()) {
            list.add(extractNoteFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateNote(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        //values.put("category", note.getCategory());
        values.put("createAt", dateFormat.format(note.getCreateAt()));
        values.put("updateAt", dateFormat.format(note.getUpdateAt()));
        values.put("themeColor", note.getThemeColor());
        values.put("fontColor", note.getFontColor());
        values.put("fontSize", note.getFontSize());
        values.put("isHidden", note.isHidden() ? 1 : 0);
        values.put("userId", note.getUserId());
        values.put("categoryId", note.getCategoryId());
        db.update("Note", values, "id = ?", new String[]{String.valueOf(note.getId())});
    }

    @Override
    public void deleteNote(int id) {
        db.delete("Note", "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public List<Note> getNotesByCategoryId(String categoryId, int userId) {
        List<Note> list = new ArrayList<>();
        Cursor cursor = db.query("Note", null, "categoryId = ? AND userId = ?", new String[]{categoryId, String.valueOf(userId)}, null, null, null);
        while (cursor.moveToNext()) {
            list.add(extractNoteFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Note> getAllNotes() {
        List<Note> list = new ArrayList<>();
        Cursor cursor = db.query("Note", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(extractNoteFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    private Note extractNoteFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        //String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        Date createAt = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("createAt")));
        Date updateAt = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("updateAt")));
        String themeColor = cursor.getString(cursor.getColumnIndexOrThrow("themeColor"));
        String fontColor = cursor.getString(cursor.getColumnIndexOrThrow("fontColor"));
        int fontSize = cursor.getInt(cursor.getColumnIndexOrThrow("fontSize"));
        boolean isHidden = cursor.getInt(cursor.getColumnIndexOrThrow("isHidden")) == 1;
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
        String categoryId = cursor.getString(cursor.getColumnIndexOrThrow("categoryId"));

        return new Note(id, title, content, "category", createAt, updateAt, themeColor, fontColor, fontSize, isHidden, userId, categoryId);
    }

    private Date parseDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            return new Date();
        }
    }
}
