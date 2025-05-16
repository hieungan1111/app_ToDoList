package com.example.todolist.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ProgressBar;


import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.todolist.CalendarActivity;
import com.example.todolist.DemActivity;
import com.example.todolist.AlarmActivity;
import com.example.todolist.NoteActivity;
import com.example.todolist.NotificationHistoryActivity;
import com.example.todolist.R;

import com.example.todolist.RemindersActivity;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;
import com.example.todolist.dao.impl.SQLiteScheduleNotificationsDAO;
import com.example.todolist.model.ScheduleNotifications;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;


public class FragmentHome extends Fragment {
    int userId=-1;
    SQLiteUserDAO db;
    CardView cardCountdown;

    CardView alarmButton, noteButton;

    TextView username_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username_text=view.findViewById(R.id.username_text);
        db=new SQLiteUserDAO(requireContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1); // -1 là mặc định nếu không có
        if (userId != -1) {
            User u=db.getUserById(userId);
            username_text.setText(u.getFullname());
        } else {
            Log.d("FragmentHome", "Chưa đăng nhập hoặc userId không tồn tại");
        }

        cardCountdown=view.findViewById(R.id.cardCountdown);
        cardCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireContext(), DemActivity.class);
                startActivity(intent);
            }
        });

        alarmButton = view.findViewById(R.id.alarm_card);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang AddAlarmActivity
                Intent intent = new Intent(getActivity(), AlarmActivity.class);
                startActivity(intent);
            }
        });

        noteButton = view.findViewById(R.id.cardNote);
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                startActivity(intent);
            }
        });
        int userId = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE).getInt("userId", -1);
        loadWeeklyProgressMain(view, userId);

        ImageView arrowButtonReminder = view.findViewById(R.id.arrowButtonReminder);
        arrowButtonReminder.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RemindersActivity.class);
            startActivity(intent);
        });
        ImageView notificationBell = view.findViewById(R.id.notification_bell);
        notificationBell.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NotificationHistoryActivity.class);
            startActivity(intent);
        });
        SQLiteScheduleNotificationsDAO notificationDAO = new SQLiteScheduleNotificationsDAO(requireContext());
        List<ScheduleNotifications> list = notificationDAO.getAllByUserId(userId);

// Kiểm tra xem có notification nào chưa xem không
        boolean hasUnread = false;
        for (ScheduleNotifications noti : list) {
            if (!noti.isViewed()) {
                hasUnread = true;
                break;
            }
        }

// Cập nhật icon tùy theo trạng thái
        if (hasUnread) {
            notificationBell.setImageResource(R.drawable.notification_active);
        } else {
            notificationBell.setImageResource(R.drawable.p_notification);
        }
        Button viewScheduleButton = view.findViewById(R.id.view_schedule_button);
        viewScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadWeeklyProgressMain(View view, int userId) {
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView progressLabel = view.findViewById(R.id.progressLabel);

        SQLiteTaskDAO taskDAO = new SQLiteTaskDAO(requireContext());
        List<Task> allTasks = taskDAO.getAllTasksByUserId(userId);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        Date weekStart = calendar.getTime();
        calendar.add(Calendar.DATE, 6);
        Date weekEnd = calendar.getTime();

        int weekCount = 0, weekDone = 0;

        for (Task task : allTasks) {
            Date taskDate = task.getDay();
            if (!taskDate.before(weekStart) && !taskDate.after(weekEnd)) {
                weekCount++;
                if (task.isDone()) weekDone++;
            }
        }

        int percent = weekCount > 0 ? (weekDone * 100 / weekCount) : 0;
        progressBar.setProgress(percent);
        progressLabel.setText(weekDone + "/" + weekCount + " nhiệm vụ");
    }

}
