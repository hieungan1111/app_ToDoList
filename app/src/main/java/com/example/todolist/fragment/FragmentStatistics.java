package com.example.todolist.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.R;
import com.example.todolist.dao.impl.SQLiteDeadlineDAO;
import com.example.todolist.dao.impl.SQLiteSubjectDAO;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.view.DonutChartView;


import android.widget.TextView;

import android.widget.ImageButton;

public class FragmentStatistics extends Fragment {

    private DonutChartView donutChartView;
    private TextView chartPercentage;
    private ImageButton btnBack;
    private TextView taskRate, deadlineRate, classRate;
    private SQLiteTaskDAO taskDAO;
    private SQLiteDeadlineDAO deadlineDAO;
    private SQLiteSubjectDAO subjectDAO;
    private int userId;

    public FragmentStatistics() {
        // Required empty public constructor
    }

    public static FragmentStatistics newInstance() {
        return new FragmentStatistics();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        Log.d("User ID", String.valueOf(userId));

        // Initialize views
        donutChartView = view.findViewById(R.id.donut_chart);
        chartPercentage = view.findViewById(R.id.chart_percentage);
        btnBack = view.findViewById(R.id.btn_back);

        taskRate = view.findViewById(R.id.taskRate);
        deadlineRate = view.findViewById(R.id.deadlineRate);
        classRate = view.findViewById(R.id.classRate);

        taskDAO = new SQLiteTaskDAO(view.getContext());
        deadlineDAO = new SQLiteDeadlineDAO(view.getContext());
        subjectDAO = new SQLiteSubjectDAO(view.getContext());

        // Tính phần trăm công việc hoàn thành
        int totalTasks = taskDAO.getAllTasksByUserId(userId).size();
        int completedTasks = taskDAO.getTaskDoneByUserId(userId).size();

        Log.d("Complete task", String.valueOf(completedTasks));

        float percentage = 0;
        if (totalTasks > 0) {
            percentage = (completedTasks * 100f) / totalTasks;
        }

        // Deadline hoàn thành
        int totalDeadlines = deadlineDAO.getAllDeadlinesByUserId(userId).size();
        int completedDeadline = deadlineDAO.getCompletedDeadlineByUserId(userId).size();
        int subject = subjectDAO.getAllSubjectsByUserId(userId).size();

        taskRate.setText("" + completedTasks + "/" + totalTasks);
        deadlineRate.setText("" + completedDeadline + "/" + totalDeadlines);
        classRate.setText("" + subject);

        // Set up the donut chart
        donutChartView.setPercentage(percentage);
        chartPercentage.setText(String.format("%.0f%%", percentage));


        // Set up back button
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }
}