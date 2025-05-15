package com.example.todolist.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.example.todolist.model.Deadline;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmUtils {

    public static void scheduleDeadlineReminder(Context context, Deadline deadline) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
                return; // Chưa được phép, không đặt báo thức nữa
            }
        }

        try {
            String dateTimeStr = deadline.getDay() + " " + deadline.getTimeEnd(); // "yyyy-MM-dd HH:mm"
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date deadlineDate = format.parse(dateTimeStr);

            if (deadlineDate == null) {
                Log.e("AlarmUtils", "Parsed date is null.");
                return;
            }

            // Trừ 1 ngày
            long reminderTimeMillis = deadlineDate.getTime() - (24 * 60 * 60 * 1000);
            // Test nhanh: trừ 1 phút để dễ thấy
//             long reminderTimeMillis = System.currentTimeMillis() + 120000;
//            long reminderTimeMillis = System.currentTimeMillis() + 30000; // 30s sau


            Log.d("AlarmUtils", "Scheduling alarm at: " + new Date(reminderTimeMillis));

            if (reminderTimeMillis < System.currentTimeMillis()) {
                Log.d("AlarmUtils", "Reminder time is in the past. Skipping alarm.");
                return;
            }

            Intent intent = new Intent(context, DeadlineReminderReceiver.class);
            intent.putExtra("deadlineName", deadline.getDeadlineName());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    deadline.getId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);

            Log.d("AlarmUtils", "Alarm set for: " + deadline.getDeadlineName());

        } catch (Exception e) {
            Log.e("AlarmUtils", "Error scheduling alarm: " + e.getMessage());
        }
    }

    public static void cancelDeadlineReminder(Context context, int deadlineId) {
        Intent intent = new Intent(context, DeadlineReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                deadlineId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d("AlarmUtils", "Alarm cancelled for id: " + deadlineId);
    }
}
