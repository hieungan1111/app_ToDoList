package com.example.todolist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todolist.fragment.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;

    // 🛡️ Request permission launcher
    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.i("Permission", "✅ POST_NOTIFICATIONS được cấp");
                } else {
                    Log.w("Permission", "❌ POST_NOTIFICATIONS bị từ chối");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Kiểm tra quyền POST_NOTIFICATIONS từ Android 13 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

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
