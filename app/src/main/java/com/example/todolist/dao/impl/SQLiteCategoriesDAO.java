package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.CategoriesDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.Categories;

import java.util.ArrayList;
import java.util.List;

public class SQLiteCategoriesDAO implements CategoriesDAO {

    private final SQLiteDatabase db;

    public SQLiteCategoriesDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addCategory(Categories category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("userId", category.getUserId());
        db.insert("Categories", null, values);
    }

    @Override
    public List<Categories> getAllCategoriesByUserId(int userId) {
        List<Categories> list = new ArrayList<>();
        Cursor cursor = db.query("Categories", null, "userId = ?", new String[]{String.valueOf(userId)}, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            list.add(new Categories(name, userId));
        }
        cursor.close();
        return list;
    }

    @Override
    public void deleteCategoryByName(String name, int userId) {
        db.delete("Categories", "name = ? AND userId = ?", new String[]{name, String.valueOf(userId)});
    }

    @Override
    public void deleteAllCategoriesOfUser(int userId) {
        db.delete("Categories", "userId = ?", new String[]{String.valueOf(userId)});
    }
}
