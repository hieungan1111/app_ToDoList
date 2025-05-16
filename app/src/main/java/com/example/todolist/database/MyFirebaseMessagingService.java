package com.example.todolist.database;

import com.google.firebase.messaging.FirebaseMessagingService;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Khi nhận được thông báo từ FCM
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // Gửi thông báo tới người dùng
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        // Lưu token vào SQLite
        Log.d(TAG, "New FCM Token: " + token);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        dbHelper.saveToken(token);  // Lưu token vào SQLite

        // Gửi token đến server của bạn (nếu cần)
        sendTokenToServer(token);
    }

    private void sendNotification(String messageBody) {
        // Đẩy thông báo tới người dùng (có thể dùng Notification Manager)
        // Ví dụ đơn giản chỉ sử dụng Log để kiểm tra
        Log.d(TAG, "Sending notification: " + messageBody);
        // Bạn có thể sử dụng NotificationManager để hiển thị thông báo
    }

    private void sendTokenToServer(String token) {
        // Hàm này gửi token lên server của bạn (nếu cần)
        // Ví dụ gửi token lên server thông qua API
    }
}