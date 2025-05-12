package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.adapter.PersonalTaskAdapter;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;

import java.util.List;

public class ExtracurricularTaskActivity extends AppCompatActivity {

    private ListView listViewTasks;
    private LinearLayout emptyLayout;
    private SQLiteTaskDAO taskDAO;
    private PersonalTaskAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracurricular_task);

        listViewTasks = findViewById(R.id.listViewTasks);
        emptyLayout = findViewById(R.id.emptyLayout);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        taskDAO = new SQLiteTaskDAO(this);
        List<Task> tasks = taskDAO.getTasksByUserIdAndType(userId, "extracurricular");

        if (tasks.isEmpty()) {
            listViewTasks.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            adapter = new PersonalTaskAdapter(this, tasks);
            listViewTasks.setAdapter(adapter);
            emptyLayout.setVisibility(View.GONE);
            listViewTasks.setVisibility(View.VISIBLE);
        }
    }
}
