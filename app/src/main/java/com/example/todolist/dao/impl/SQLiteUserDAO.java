package com.example.todolist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.dao.UserDAO;
import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteUserDAO implements UserDAO {

    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteUserDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put("fullname", user.getFullname());
        values.put("email", user.getEmail());
        values.put("createAt", dateFormat.format(user.getCreateAt()));
        values.put("fcmToken", user.getFcmToken());
        values.put("is_verified", user.getIs_verified());
        values.put("password", user.getPassword());
        db.insert("User", null, values);
    }

    @Override
    public User findUser(String email) {
        Cursor cursor = null;
        User user = null;

        try {
            // Sử dụng db.query() để truy vấn cơ sở dữ liệu với điều kiện email và code
            cursor = db.query(
                    "User",
                    null,
                    "email = ? and is_verified=?",
                    new String[]{email,"1"},
                    null,
                    null,
                    null
            );

            // Kiểm tra nếu cursor có dữ liệu
            if (cursor != null && cursor.moveToFirst()) {
                user = extractUserFromCursor(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    @Override
    public User findCode(String code) {
        Cursor cursor = null;
        User user = null;

        try {
            // Sử dụng db.query() để truy vấn cơ sở dữ liệu với điều kiện email và code
            cursor = db.query(
                    "User",
                    null,
                    "fcmToken = ?",
                    new String[]{code},
                    null,
                    null,
                    null
            );

            // Kiểm tra nếu cursor có dữ liệu
            if (cursor != null && cursor.moveToFirst()) {
                user = extractUserFromCursor(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    @Override
    public User checkVerify(String email, String code) {
        Cursor cursor = null;
        User user = null;

        try {
            // Sử dụng db.query() để truy vấn cơ sở dữ liệu với điều kiện email và code
            cursor = db.query(
                    "User",
                    null,
                    "email=? and fcmToken = ? and is_verified=?",
                    new String[]{email, code, "0"},
                    null,
                    null,
                    null
            );

            // Kiểm tra nếu cursor có dữ liệu
            if (cursor != null && cursor.moveToFirst()) {
                user = extractUserFromCursor(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    @Override
    public void updateIsVerified(int id) {
        ContentValues values = new ContentValues();
        values.put("is_verified",1);
        db.update("User", values, "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public boolean updatePassword(int id,String password) {
        ContentValues values = new ContentValues();
        values.put("password",password);
        int rowsAffected = db.update("User", values, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    @Override
    public void deleteUserByEmail(String email) {
        db.delete("User", "email = ?", new String[]{String.valueOf(email)});
    }

    @Override
    public User getUserById(int id) {
        Cursor cursor = db.query("User", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = extractUserFromCursor(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Cursor cursor = db.query("User", null, null, null, null, null, "createAt DESC");
        while (cursor.moveToNext()) {
            list.add(extractUserFromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("fullname", user.getFullname());
        values.put("birthday", user.getBirthday());
        values.put("gender", user.getGender());
        values.put("avatarUrl", user.getAvatarUrl());

        int rowsAffected = db.update("User", values, "id = ?", new String[]{String.valueOf(user.getId())});

        return rowsAffected > 0;
    }


    @Override
    public void deleteUser(int id) {
        db.delete("User", "id = ?", new String[]{String.valueOf(id)});
    }

    private User extractUserFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        Date createAt = parseDate(cursor.getString(cursor.getColumnIndexOrThrow("createAt")));
        String fcmToken = cursor.getString(cursor.getColumnIndexOrThrow("fcmToken"));
        String birthday = cursor.getString(cursor.getColumnIndexOrThrow("birthday"));
        String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
        int is_verified = cursor.getInt(cursor.getColumnIndexOrThrow("is_verified"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow("avatarUrl"));

        return new User(id, fullname, email, createAt, fcmToken, birthday, gender, avatarUrl, password,is_verified);
    }

    private Date parseDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            return new Date();
        }
    }
}
