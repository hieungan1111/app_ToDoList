package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.todolist.dao.impl.SQLiteAlarmDAO;
import com.example.todolist.model.Alarm;

import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_ALARM_OFF = "ALARM_OFF";
    public static final String ACTION_STOP_ALARM = "STOP_ALARM";
    private static MediaPlayer currentPlayer = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int alarmId = intent.getIntExtra("alarmId", -1);
        Log.d("AlarmReceiver", "Received intent: action=" + action + ", alarmId=" + alarmId);

        if (ACTION_STOP_ALARM.equals(action)) {
            handleStopAlarm(context, alarmId);
            return;
        }

        String alarmName = intent.getStringExtra("alarmName");
        Log.d("AlarmReceiver", "Received alarm: ID=" + alarmId + ", Name=" + alarmName);

        showNotification(context, alarmName, alarmId);

        if (currentPlayer != null && currentPlayer.isPlaying()) {
            currentPlayer.stop();
            currentPlayer.release();
        }
        currentPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
        if (currentPlayer != null) {
            currentPlayer.start();
        } else {
            Log.e("AlarmReceiver", "Failed to create MediaPlayer");
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            stopMediaPlayer();
            handleAlarm(context, alarmId);
        }, 15_000);
    }

    private void showNotification(Context context, String title, int alarmId) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "alarm_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent stopIntent = new Intent(context, AlarmReceiver.class);
        stopIntent.setAction(ACTION_STOP_ALARM);
        stopIntent.putExtra("alarmId", alarmId);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                context, alarmId, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.add)
                .setContentTitle("Báo thức")
                .setContentText("Đến giờ: " + title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_media_pause, "Ngừng", stopPendingIntent);

        notificationManager.notify(alarmId, builder.build());
    }

    private void handleAlarm(Context context, int alarmId) {
        if (alarmId != -1) {
            SQLiteAlarmDAO dbHelper = new SQLiteAlarmDAO(context);
            Alarm alarm = dbHelper.getAlarmById(alarmId);
            if (alarm != null) {
                Log.d("AlarmReceiver", "Found alarm: " + alarm.getAlarmName());
                Log.d("AlarmReceiver", "Repeat days: " + alarm.getRepeatDays() + ", isEmpty: " + alarm.getRepeatDays().isEmpty());

                if (alarm.getRepeatDays() == null || alarm.getRepeatDays().isEmpty()) {
                    alarm.setEnable(false);
                    dbHelper.updateAlarm(alarm);
                    cancelAlarm(context, alarm);
                    Log.d("AlarmReceiver", "Updated alarm ID: " + alarmId + " to isEnable=false");
                } else {
                    Calendar nextAlarmTime = calculateNextAlarmTime(alarm);
                    if (nextAlarmTime.getTimeInMillis() > System.currentTimeMillis()) {
                        alarm.setTime(nextAlarmTime.getTime());
                        dbHelper.updateAlarm(alarm);
                        setNextAlarm(context, alarm);
                        Log.d("AlarmReceiver", "Set next alarm for ID: " + alarmId + " at " + nextAlarmTime.getTime());
                    } else {
                        Log.e("AlarmReceiver", "Invalid next alarm time for ID: " + alarmId);
                        alarm.setEnable(false);
                        dbHelper.updateAlarm(alarm);
                        cancelAlarm(context, alarm);
                    }
                }
            } else {
                Log.e("AlarmReceiver", "Alarm not found for ID: " + alarmId);
            }
        } else {
            Log.e("AlarmReceiver", "Invalid alarmId: " + alarmId);
        }

        Intent updateIntent = new Intent(ACTION_ALARM_OFF);
        updateIntent.putExtra("alarmId", alarmId);
        context.sendBroadcast(updateIntent);
        Log.d("AlarmReceiver", "Sent broadcast: " + ACTION_ALARM_OFF);
    }

    private void handleStopAlarm(Context context, int alarmId) {
        stopMediaPlayer();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(alarmId);
        Log.d("AlarmReceiver", "Cancelled notification ID: " + alarmId);

        if (alarmId != -1) {
            SQLiteAlarmDAO dbHelper = new SQLiteAlarmDAO(context);
            Alarm alarm = dbHelper.getAlarmById(alarmId);
            if (alarm != null) {
                alarm.setEnable(false);
                dbHelper.updateAlarm(alarm);
                cancelAlarm(context, alarm);
                Log.d("AlarmReceiver", "Stopped alarm ID: " + alarmId + ", set isEnable=false");
            } else {
                Log.e("AlarmReceiver", "Alarm not found for ID: " + alarmId);
            }
        } else {
            Log.e("AlarmReceiver", "Invalid alarmId for stop: " + alarmId);
        }

        Intent updateIntent = new Intent(ACTION_ALARM_OFF);
        updateIntent.putExtra("alarmId", alarmId);
        context.sendBroadcast(updateIntent);
        Log.d("AlarmReceiver", "Sent broadcast: " + ACTION_ALARM_OFF);
    }

    private void stopMediaPlayer() {
        if (currentPlayer != null && currentPlayer.isPlaying()) {
            currentPlayer.stop();
            currentPlayer.release();
            currentPlayer = null;
            Log.d("AlarmReceiver", "Stopped media player");
        }
    }

    private void cancelAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, alarm.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
        Log.d("AlarmReceiver", "Cancelled alarm ID: " + alarm.getId());
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setNextAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarmName", alarm.getAlarmName());
        intent.putExtra("alarmId", alarm.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, alarm.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarm.getTime().getTime(),
                pendingIntent
        );
        Log.d("AlarmReceiver", "Set next alarm ID: " + alarm.getId() + " at " + alarm.getTime());
    }

    private Calendar calculateNextAlarmTime(Alarm alarm) {
        Calendar now = Calendar.getInstance();
        Calendar nextAlarm = Calendar.getInstance();
        nextAlarm.setTime(alarm.getTime());

        List<String> repeatDays = alarm.getRepeatDays();
        int currentDay = now.get(Calendar.DAY_OF_WEEK);
        int daysToAdd = 7;

        for (String day : repeatDays) {
            int targetDay;
            switch (day.toUpperCase()) {
                case "T2":
                    targetDay = Calendar.MONDAY;
                    break;
                case "T3":
                    targetDay = Calendar.TUESDAY;
                    break;
                case "T4":
                    targetDay = Calendar.WEDNESDAY;
                    break;
                case "T5":
                    targetDay = Calendar.THURSDAY;
                    break;
                case "T6":
                    targetDay = Calendar.FRIDAY;
                    break;
                case "T7":
                    targetDay = Calendar.SATURDAY;
                    break;
                case "CN":
                    targetDay = Calendar.SUNDAY;
                    break;
                default:
                    targetDay = -1;
                    Log.w("AlarmReceiver", "Invalid repeat day: " + day);
                    continue;
            }
            int diff = (targetDay - currentDay + 7) % 7;
            if (diff == 0 && now.getTimeInMillis() > nextAlarm.getTimeInMillis()) {
                diff = 7;
            }
            if (diff == 0) diff = 7;
            daysToAdd = Math.min(daysToAdd, diff);
        }

        nextAlarm.add(Calendar.DAY_OF_YEAR, daysToAdd);
        Calendar original = Calendar.getInstance();
        original.setTime(alarm.getTime());
        nextAlarm.set(Calendar.HOUR_OF_DAY, original.get(Calendar.HOUR_OF_DAY));
        nextAlarm.set(Calendar.MINUTE, original.get(Calendar.MINUTE));
        nextAlarm.set(Calendar.SECOND, 0);
        nextAlarm.set(Calendar.MILLISECOND, 0);

        return nextAlarm;
    }
}