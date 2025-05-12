package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapter.ReminderAdapter;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemindersActivity extends AppCompatActivity {

    private RecyclerView recyclerReminders;
    private LinearLayout emptyReminderLayout;
    private ReminderAdapter adapter;
    private SQLiteTaskDAO taskDAO;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        recyclerReminders = findViewById(R.id.recyclerReminders);
        emptyReminderLayout = findViewById(R.id.emptyReminderLayout);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        taskDAO = new SQLiteTaskDAO(this);
        adapter = new ReminderAdapter(this, new ArrayList<>(), task -> {
            taskDAO.deleteTask(task.getId());
            loadReminders();
        });

        recyclerReminders.setLayoutManager(new LinearLayoutManager(this));
        recyclerReminders.setAdapter(adapter);

        loadReminders();
    }

    private void loadReminders() {
        List<Task> allTasks = taskDAO.getAllTasksByUserId(userId);
        List<Task> reminders = new ArrayList<>();
        Date now = new Date();

        for (Task task : allTasks) {
            if (!task.isDone() && (task.getTimeStart().equals(now) || task.getTimeStart().after(now))) {
                reminders.add(task);
            }
        }
        reminders.sort((t1, t2) -> t1.getTimeStart().compareTo(t2.getTimeStart()));
        if (reminders.isEmpty()) {
            recyclerReminders.setVisibility(View.GONE);
            emptyReminderLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerReminders.setVisibility(View.VISIBLE);
            emptyReminderLayout.setVisibility(View.GONE);
            adapter.setData(reminders);
        }
    }
}
