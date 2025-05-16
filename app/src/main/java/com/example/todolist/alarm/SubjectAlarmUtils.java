package com.example.todolist.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.todolist.model.Subject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SubjectAlarmUtils {

    public static void scheduleSubjectReminder(Context context, Subject subject, String dateStr) {
        try {
            String dateTimeStr = dateStr + " " + subject.getTimeStart(); // e.g., "2025-05-16 13:30"
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date subjectStart = format.parse(dateTimeStr);


            if (subjectStart == null) return;
            long reminderTimeMillis = subjectStart.getTime() - 30 * 60 * 1000; // Trước 30 phút
            Log.d("SubjectAlarmUtils", " buoc 2 Đặt thông báo cho môn: subject start: " +reminderTimeMillis);
            if (reminderTimeMillis < System.currentTimeMillis()) return;

            Log.d("SubjectAlarmUtils", "Đặt thông báo cho môn: " + subject.getSubjectName() + " lúc " + new Date(reminderTimeMillis));

            Intent intent = new Intent(context, SubjectReminderReceiver.class);
            intent.putExtra("subjectName", subject.getSubjectName());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    subject.getId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);

        } catch (Exception e) {
            Log.e("SubjectAlarmUtils", "Error scheduling subject reminder: " + e.getMessage());
        }
    }
}

