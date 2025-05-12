package com.example.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ToDoList.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Gọi khi database được tạo lần đầu
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullname TEXT, " +
                "email TEXT, " +
                "createAt TEXT, " +
                "fcmToken TEXT, " +
                "birthday TEXT, " +
                "gender TEXT, " +
                "is_verified INTEGER, " +
                "password TEXT, " +
                "avatarUrl TEXT)";

        String createTaskTable = "CREATE TABLE Task (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "day TEXT, " +
                "timeStart TEXT, " +
                "content TEXT, " +
                "isDone INTEGER, " +
                "userId INTEGER, " +
                "taskPriority TEXT, " +
                "isHidden INTEGER, " +
                "type TEXT, " +
                "FOREIGN KEY(userId) REFERENCES User(id))";

        String createCategories = "CREATE TABLE Categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "userId INTEGER, " +
                "FOREIGN KEY(userId) REFERENCES User(id))";

        String createNoteTable = "CREATE TABLE Note (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "content TEXT, " +
                "createAt TEXT, " +
                "updateAt TEXT, " +
                "themeColor TEXT, " +
                "fontColor TEXT, " +
                "fontSize INTEGER, " +
                "isHidden INTEGER, " +
                "userId TEXT, " +
                "categoryId INTEGER, " +
                "FOREIGN KEY(categoryId) REFERENCES Categories(id)," +
                "FOREIGN KEY(userId) REFERENCES User(id))";

        String createAlarmTable = "CREATE TABLE Alarm (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "alarmName TEXT, " +
                "time TEXT, " +
                "repeatDays TEXT, " +  // Có thể lưu dưới dạng chuỗi JSON
                "isEnable INTEGER, " +
                "createAt TEXT, " +
                "userId TEXT, " +
                "FOREIGN KEY(userId) REFERENCES User(id))";
        String createSubjectTable = "CREATE TABLE Subject (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "rangeStart TEXT, " +
                "rangeEnd TEXT, " +
                "timeStart TEXT, " +
                "timeEnd TEXT, " +
                "subject TEXT, " +
                "subjectColor TEXT, " +
                "weekdays TEXT, " + // lưu dạng JSON string hoặc "Mon,Tue"
                "userId TEXT, " +
                "FOREIGN KEY(userId) REFERENCES User(id))";

        String createDeadlineTable = "CREATE TABLE Deadline (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "day TEXT, " +
                "timeEnd TEXT, " +
                "subject TEXT, " +
                "subjectId TEXT, " +
                "deadlineName TEXT, " +
                "deadlineColor TEXT, " +
                "isDone INTEGER, " +
                "userId TEXT, " +
                "FOREIGN KEY(userId) REFERENCES User(id), " +
                "FOREIGN KEY(subjectId) REFERENCES Subject(id))";

        String createScheduleNotifications = "CREATE TABLE scheduleNotifications (" +
                "notificationTime TEXT, " +
                "sent INTEGER, " +
                "taskId TEXT, " +
                "title TEXT, " +
                "userId TEXT, " +
                "FOREIGN KEY(taskId) REFERENCES Task(id), " +
                "FOREIGN KEY(userId) REFERENCES User(id))";

        String createScheduleDeadlineNotification = "CREATE TABLE scheduleDeadlineNotification (" +
                "deadlineId TEXT, " +
                "notificationTime TEXT, " +
                "sent INTEGER, " +
                "subject TEXT, " +
                "title TEXT, " +
                "type TEXT, " +
                "userId TEXT, " +
                "FOREIGN KEY(deadlineId) REFERENCES Deadline(id), " +
                "FOREIGN KEY(userId) REFERENCES User(id))";

        db.execSQL(createUserTable);
        db.execSQL(createTaskTable);
        db.execSQL(createNoteTable);
        db.execSQL(createAlarmTable);
        db.execSQL(createCategories);
        db.execSQL(createSubjectTable);
        db.execSQL(createDeadlineTable);
        db.execSQL(createScheduleNotifications);
        db.execSQL(createScheduleDeadlineNotification);
    }

    // Gọi khi database cần nâng cấp (thay đổi VERSION)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS scheduleDeadlineNotification");
        db.execSQL("DROP TABLE IF EXISTS scheduleNotifications");
        db.execSQL("DROP TABLE IF EXISTS Deadline");
        db.execSQL("DROP TABLE IF EXISTS Subject");
        db.execSQL("DROP TABLE IF EXISTS Alarm");
        db.execSQL("DROP TABLE IF EXISTS Note");
        db.execSQL("DROP TABLE IF EXISTS Categories");
        db.execSQL("DROP TABLE IF EXISTS Task");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
}
