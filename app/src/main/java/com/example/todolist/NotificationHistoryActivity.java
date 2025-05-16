package com.example.todolist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapter.NotificationAdapter;
import com.example.todolist.dao.impl.SQLiteScheduleNotificationsDAO;
import com.example.todolist.model.ScheduleNotifications;

import java.util.List;

public class NotificationHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;
    private SQLiteScheduleNotificationsDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        emptyLayout = findViewById(R.id.emptyNotificationLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dao = new SQLiteScheduleNotificationsDAO(this);
        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);
        List<ScheduleNotifications> list = dao.getAllByUserId(userId);

        if (list.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setAdapter(new NotificationAdapter(list));
        }
        dao.markAllAsViewedByUser(userId);
        // Xử lý nút quay lại
        ImageButton backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(v -> finish());
    }
}
