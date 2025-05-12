package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todolist.fragment.FragmentCalender;
import com.example.todolist.fragment.FragmentHome;
import com.example.todolist.fragment.FragmentProfile;
import com.example.todolist.fragment.FragmentStatistics;
import com.example.todolist.fragment.FragmentTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            FragmentHome homeFragment = new FragmentHome();
            add(homeFragment, "HOME", "HOME");

            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.home);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

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
}