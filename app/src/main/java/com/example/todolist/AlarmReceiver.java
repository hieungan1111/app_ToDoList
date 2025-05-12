package com.example.todolist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.example.todolist.dao.impl.SQLiteAlarmDAO;
import com.example.todolist.model.Alarm;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmName = intent.getStringExtra("alarmName");
        int alarmId = intent.getIntExtra("alarmId", -1);
        // Hiện thông báo
        showNotification(context, alarmName);

        // (Tùy chọn) Phát âm thanh
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
        mediaPlayer.start();

        // Tự động tắt sau 30 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }


        }, 30_000); // 30 giây

    }

    private void showNotification(Context context, String title) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "alarm_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.add)
                .setContentTitle("Báo thức")
                .setContentText("Đến giờ: " + title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}

