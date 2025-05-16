package com.example.todolist.work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.todolist.broadcast.NotificationHelper;
import com.example.todolist.dao.impl.SQLiteScheduleNotificationsDAO;
import com.example.todolist.model.ScheduleNotifications;
import com.example.todolist.sendEmail.EmailSender;

import java.util.Date;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int taskId = getInputData().getInt("task_id", -1);
        int userId = getInputData().getInt("user_id", -1);
        String content = getInputData().getString("task_content");
        String email = getInputData().getString("user_email");
        String priority = getInputData().getString("task_priority");

        Log.d("ReminderWorker", "⏰ Nhận tác vụ: " + taskId + " - " + content + " - Priority: " + priority);

        if ("HIGH".equalsIgnoreCase(priority)) {
            // Gửi thông báo + email
            NotificationHelper.sendNotification(getApplicationContext(), "🔔 Nhắc nhở công việc", content);
            if (email != null && !email.isEmpty()) {
                EmailSender.sendEmail(email, "Nhắc nhở công việc", "⏰ Sắp đến hạn: " + content);
            }
        } else if ("MEDIUM".equalsIgnoreCase(priority)) {
            // Chỉ gửi thông báo
            NotificationHelper.sendNotification(getApplicationContext(), "🔔 Nhắc nhở công việc", content);

        } else {
            // LOW hoặc không xác định → không làm gì
            Log.d("ReminderWorker", "⚠️ Độ ưu tiên thấp hoặc không xác định – Không gửi nhắc nhở.");
            return Result.success(); // Không làm gì vẫn return success
        }

        // Ghi log DB cho các priority thực sự được xử lý
        ScheduleNotifications notification = new ScheduleNotifications(
                new Date(),
                true,
                taskId,
                content,
                userId
        );
        new SQLiteScheduleNotificationsDAO(getApplicationContext()).addNotification(notification);

        return Result.success();
    }
}
