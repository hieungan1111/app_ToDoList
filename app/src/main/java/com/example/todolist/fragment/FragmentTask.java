package com.example.todolist.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.ExtracurricularTaskActivity;
import com.example.todolist.NotificationHistoryActivity;
import com.example.todolist.OverdueTasksActivity;
import com.example.todolist.PersonalTaskActivity;
import com.example.todolist.R;
import com.example.todolist.RemindersActivity;
import com.example.todolist.ShowTaskActivity;
import com.example.todolist.adapter.UpcomingTaskRecyclerAdapter;
import com.example.todolist.dao.impl.SQLiteScheduleNotificationsDAO;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.ScheduleNotifications;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentTask extends Fragment {
    int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        refreshAllViews(view);

        ImageView showTask = view.findViewById(R.id.showTask);
        ImageView alert = view.findViewById(R.id.alert);
        ImageView overTime = view.findViewById(R.id.overTime);
        LinearLayout personal_activity = view.findViewById(R.id.personal_activity);
        LinearLayout extracurricular_activity = view.findViewById(R.id.extracurricular_activity);

        showTask.setOnClickListener(v -> startActivity(new Intent(requireContext(), ShowTaskActivity.class)));
        alert.setOnClickListener(v -> startActivity(new Intent(requireContext(), RemindersActivity.class)));
        overTime.setOnClickListener(v -> startActivity(new Intent(requireContext(), OverdueTasksActivity.class)));
        personal_activity.setOnClickListener(v -> startActivity(new Intent(requireContext(), PersonalTaskActivity.class)));
        extracurricular_activity.setOnClickListener(v -> startActivity(new Intent(requireContext(), ExtracurricularTaskActivity.class)));
        ImageButton notificationButton = view.findViewById(R.id.imageButton);

        SQLiteScheduleNotificationsDAO notificationDAO = new SQLiteScheduleNotificationsDAO(requireContext());
        List<ScheduleNotifications> notifications = notificationDAO.getAllByUserId(userId);

        boolean hasUnread = false;
        for (ScheduleNotifications noti : notifications) {
            if (!noti.isViewed()) {
                hasUnread = true;
                break;
            }
        }

        if (hasUnread) {
            notificationButton.setImageResource(R.drawable.notification_active); // biểu tượng có thông báo mới
        } else {
            notificationButton.setImageResource(R.drawable.notifications); // biểu tượng mặc định
        }

        notificationButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NotificationHistoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            refreshAllViews(getView());
        }
    }

    private void refreshAllViews(View view) {
        loadTodayTaskStats(view);
        loadWeeklyProgress(view);
        loadUpcomingTasks(view);
        updateTaskTypeCounts(view);
        loadWeeklyOverview(view);
    }

    private void loadTodayTaskStats(View view) {
        TextView tvTotalTasks = view.findViewById(R.id.textView9);
        TextView tvDoneTasks = view.findViewById(R.id.countDone);

        SQLiteTaskDAO taskDAO = new SQLiteTaskDAO(requireContext());
        List<Task> allTasks = taskDAO.getAllTasksByUserId(userId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = dateFormat.format(new Date());

        int todayCount = 0;
        int doneCount = 0;

        for (Task task : allTasks) {
            if (dateFormat.format(task.getDay()).equals(todayStr)) {
                todayCount++;
                if (task.isDone()) doneCount++;
            }
        }

        tvTotalTasks.setText(String.valueOf(todayCount));
        tvDoneTasks.setText(String.valueOf(doneCount));
    }

    private void loadWeeklyProgress(View view) {
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

    private void loadUpcomingTasks(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTasks);
        LinearLayout emptyLayout = view.findViewById(R.id.emptyLayout);

        SQLiteTaskDAO taskDAO = new SQLiteTaskDAO(requireContext());
        List<Task> allTasks = taskDAO.getAllTasksByUserId(userId);

        List<Task> upcomingTasks = new ArrayList<>();
        Date now = new Date();

        for (Task task : allTasks) {
            if (task.getDay().after(now) && !task.isDone()) {
                upcomingTasks.add(task);
            }
        }
        upcomingTasks.sort((t1, t2) -> t1.getDay().compareTo(t2.getDay()));
        if (upcomingTasks.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(new UpcomingTaskRecyclerAdapter(requireContext(), upcomingTasks));
        }
    }

    private void updateTaskTypeCounts(View view) {
        TextView txtCountPersonal = view.findViewById(R.id.txtCountPersonal);
        TextView txtCountExtra = view.findViewById(R.id.txtCountExtra);

        SQLiteTaskDAO taskDAO = new SQLiteTaskDAO(requireContext());
        txtCountPersonal.setText(String.valueOf(taskDAO.getTasksByUserIdAndType(userId, "personal").size()));
        txtCountExtra.setText(String.valueOf(taskDAO.getTasksByUserIdAndType(userId, "extracurricular").size()));
    }

    private void loadWeeklyOverview(View view) {
        int[] barIds = {
                R.id.barMon, R.id.barTue, R.id.barWed, R.id.barThu, R.id.barFri, R.id.barSat, R.id.barSun
        };
        int[] labelIds = {
                R.id.labelMon, R.id.labelTue, R.id.labelWed, R.id.labelThu, R.id.labelFri, R.id.labelSat, R.id.labelSun
        };

        SQLiteTaskDAO taskDAO = new SQLiteTaskDAO(requireContext());
        List<Task> allTasks = taskDAO.getAllTasksByUserId(userId);

        // Lấy ngày đầu tuần (Thứ 2)
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7; // Chuyển CN = 6
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        Date startOfWeek = cal.getTime();

        // Tạo danh sách rỗng cho từng ngày
        List<List<Task>> weekTasks = new ArrayList<>();
        for (int i = 0; i < 7; i++) weekTasks.add(new ArrayList<>());

        // Gom task vào từng ngày
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        for (Task task : allTasks) {
            for (int i = 0; i < 7; i++) {
                Calendar dayCal = Calendar.getInstance();
                dayCal.setTime(startOfWeek);
                dayCal.add(Calendar.DAY_OF_MONTH, i);

                if (sdf.format(task.getDay()).equals(sdf.format(dayCal.getTime()))) {
                    weekTasks.get(i).add(task);
                    break;
                }
            }
        }

        // Cập nhật UI cho từng cột ngày
        int maxHeightPx = dpToPx(100);
        for (int i = 0; i < 7; i++) {
            List<Task> tasksInDay = weekTasks.get(i);
            int done = 0;
            for (Task t : tasksInDay) if (t.isDone()) done++;

            TextView label = view.findViewById(labelIds[i]);
            View bar = view.findViewById(barIds[i]);

            label.setText(done + "/" + tasksInDay.size());

            float percent = tasksInDay.isEmpty() ? 0 : (done * 1.0f / tasksInDay.size());
            int fillHeightPx = (int) (maxHeightPx * percent);

            // Cập nhật chiều cao cho View để hiển thị cột
            ViewGroup.LayoutParams params = bar.getLayoutParams();
            params.height = fillHeightPx;
            bar.setLayoutParams(params);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
