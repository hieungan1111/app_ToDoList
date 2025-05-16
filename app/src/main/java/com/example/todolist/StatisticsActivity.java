package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todolist.view.DonutChartView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatisticsActivity extends AppCompatActivity {

    private DonutChartView donutChartView;
    private TextView chartPercentage;
    private BottomNavigationView bottomNavigationView;
    private ImageButton btnBack;

    private int userId;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize views
        donutChartView = findViewById(R.id.donut_chart);
        chartPercentage = findViewById(R.id.chart_percentage);
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnBack = findViewById(R.id.btn_back);

        // Set up the donut chart
        donutChartView.setPercentage(71f);
        chartPercentage.setText("71%");

        // Set up bottom navigation
//        bottomNavigationView.setSelectedItemId(R.id.nav_stats);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            // Handle navigation item clicks
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    // Navigate to home
//                    return true;
//                case R.id.nav_search:
//                    // Navigate to search
//                    return true;
//                case R.id.nav_stats:
//                    // Already on stats
//                    return true;
//                case R.id.nav_profile:
//                    // Navigate to profile
//                    return true;
//            }
//            return false;
//        });

        // Set up back button
        btnBack.setOnClickListener(v -> finish());
    }
}