package com.example.todolist.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todolist.R;

public class SubjectReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String subjectName = intent.getStringExtra("subjectName");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "subject_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Sắp đến giờ học")
                .setContentText("Bạn sắp có môn học: " + subjectName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}


