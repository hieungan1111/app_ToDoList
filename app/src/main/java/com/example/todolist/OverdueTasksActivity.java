package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.adapter.OverdueTaskListAdapter;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverdueTasksActivity extends AppCompatActivity {

    private ListView listOverdueTasks;
    private LinearLayout emptyOverdueLayout;
    private SQLiteTaskDAO taskDAO;
    private OverdueTaskListAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue_tasks);

        listOverdueTasks = findViewById(R.id.listOverdueTasks);
        emptyOverdueLayout = findViewById(R.id.emptyOverdueLayout);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        taskDAO = new SQLiteTaskDAO(this);

        List<Task> overdueTasks = new ArrayList<>();
        Date now = new Date();

        for (Task task : taskDAO.getAllTasksByUserId(userId)) {
            if (!task.isDone() && task.getTimeStart().before(now)) {
                overdueTasks.add(task);
            }
        }

        Log.d("OverdueTasks", "Tổng số task quá hạn: " + overdueTasks.size());
        if (overdueTasks.isEmpty()) {
            listOverdueTasks.setVisibility(View.GONE);
            emptyOverdueLayout.setVisibility(View.VISIBLE);
        } else {
            adapter = new OverdueTaskListAdapter(this, overdueTasks);
            listOverdueTasks.setAdapter(adapter);
            listOverdueTasks.setVisibility(View.VISIBLE);
            emptyOverdueLayout.setVisibility(View.GONE);
        }
    }
}
