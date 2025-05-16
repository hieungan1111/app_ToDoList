package com.example.todolist.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.todolist.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "my_app_channel";
    private static final String CHANNEL_NAME = "To_Do_List";

    public static void showNotification(Context context, String title, String body) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Chuông mặc định
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Tạo Notification Channel nếu Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            // Cấu hình chuông và rung cho Channel
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            channel.setSound(soundUri, audioAttributes);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(new long[]{0, 300, 200, 300})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Gửi thông báo (ID có thể đổi nếu cần)
        notificationManager.notify(1, builder.build());
    }
}
