package com.example.todolist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todolist.alarm.SubjectAlarmUtils;
import com.example.todolist.dao.impl.SQLiteSubjectDAO;
import com.example.todolist.fragment.FragmentCalender;
import com.example.todolist.fragment.FragmentHome;
import com.example.todolist.fragment.FragmentProfile;
import com.example.todolist.fragment.FragmentStatistics;
import com.example.todolist.fragment.FragmentTask;
import com.example.todolist.model.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // cau hinhf để bật thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("subject_channel", "Subject Reminder", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        manager = getSupportFragmentManager();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // Gọi đặt thông báo môn học hôm nay
        scheduleTodaySubjects(this);


        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String tag = null;

            int id = item.getItemId();

            if (id == R.id.home) {
                selectedFragment = new FragmentHome();
                tag = "HOME";
            } else if (id == R.id.task) {
                selectedFragment = new FragmentTask();
                tag = "TASK";
            } else if (id == R.id.statistics) {
                selectedFragment = new FragmentStatistics();
                tag = "STATISTICS";
            } else if (id == R.id.calender) {
                selectedFragment = new FragmentCalender();
                tag = "CALENDER";
            } else if (id == R.id.profile) {
                selectedFragment = new FragmentProfile();
                tag = "PROFILE";
            }

            if (selectedFragment != null) {
                add(selectedFragment, tag, tag);
                return true;
            }

            return false;
        });


    }

    private void add(Fragment fg, String tag, String name) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fg, tag); // dùng replace để tránh chồng fragment
        transaction.addToBackStack(name);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void scheduleTodaySubjects(Context context) {
        SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(context);
        List<Subject> allSubjects = subjectDAO.getAllSubjectsByUserId(1); // Sửa userId nếu cần

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Calendar calendar = Calendar.getInstance();
        String weekday = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date()).toLowerCase();
        Log.d("MainActivity", "Đang kiểm tra môn học hôm nay, weekday: " + weekday + ", date: " + today);
        for (Subject subject : allSubjects) {

            if (!subject.getWeekDays().contains(weekday)) continue;


            if (today.compareTo(subject.getRangeStart()) >= 0 && today.compareTo(subject.getRangeEnd()) <= 0) {
                SubjectAlarmUtils.scheduleSubjectReminder(context, subject, today);
            }
        }
    }

}