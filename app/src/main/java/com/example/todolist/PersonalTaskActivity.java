package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.adapter.PersonalTaskAdapter; // ✅ dùng adapter mới
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;

import java.util.List;

public class PersonalTaskActivity extends AppCompatActivity {

    private ListView listViewPersonalTasks;
    private LinearLayout emptyLayout;
    private SQLiteTaskDAO taskDAO;
    private PersonalTaskAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_task);

        listViewPersonalTasks = findViewById(R.id.listViewTasks);
        emptyLayout = findViewById(R.id.emptyLayout);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        taskDAO = new SQLiteTaskDAO(this);
        List<Task> personalTasks = taskDAO.getTasksByUserIdAndType(userId, "personal");

        if (personalTasks.isEmpty()) {
            listViewPersonalTasks.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            adapter = new PersonalTaskAdapter(this, personalTasks); // ✅ dùng adapter mới
            listViewPersonalTasks.setAdapter(adapter);
            emptyLayout.setVisibility(View.GONE);
            listViewPersonalTasks.setVisibility(View.VISIBLE);
        }
    }
}
