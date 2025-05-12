package com.example.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapter.DateAdapter;
import com.example.todolist.adapter.TaskListAdapter;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowTaskActivity extends AppCompatActivity implements DateAdapter.OnDateSelectedListener, TaskListAdapter.OnTaskActionListener{

    private SQLiteTaskDAO taskDAO;
    private int userId;
    private ListView taskListView;
    private TaskListAdapter taskListAdapter;
    private TextView tvMonthYear;
    private RecyclerView dateRecyclerView;
    private DateAdapter dateAdapter;

    private Calendar currentCalendar;
    private List<Date> dates;
    private List<Task> tasks;
    private List<Task> filteredTasks;
    private Date selectedDate;
    private boolean isInitialLoad = true;
    ImageButton btnBack;

    private final SimpleDateFormat vietnameseMonthYearFormat = new SimpleDateFormat("'tháng' M 'năm' yyyy", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && selectedDate != null) {
                    loadTasksByDate(selectedDate);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);

        initViews();
        setupListeners();

        currentCalendar = Calendar.getInstance();
        updateMonthYearText();

        taskDAO = new SQLiteTaskDAO(this);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        dates = generateDatesForMonth(currentCalendar);
        filteredTasks = new ArrayList<>();
        loadTasks();

        setupDateAdapter();
        taskListView = findViewById(R.id.taskListView);
        taskListAdapter = new TaskListAdapter(this, filteredTasks, this);
        taskListView.setAdapter(taskListAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        tvMonthYear = findViewById(R.id.tvMonthYear);
        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        taskListView = findViewById(R.id.taskListView);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        ImageButton btnPrevMonth = findViewById(R.id.btnPrevMonth);
        ImageButton btnNextMonth = findViewById(R.id.btnNextMonth);
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);

        btnPrevMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateMonthYearText();
            updateDates(true);
        });

        btnNextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateMonthYearText();
            updateDates(true);
        });

        fabAddTask.setOnClickListener(v -> {
            if (selectedDate != null) {
                Intent intent = new Intent(ShowTaskActivity.this, AddTaskActivity.class);
                intent.putExtra("selected_date", selectedDate.getTime());
                addTaskLauncher.launch(intent);
            } else {
                Toast.makeText(this, "Vui lòng chọn một ngày trước", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMonthYearText() {
        tvMonthYear.setText(vietnameseMonthYearFormat.format(currentCalendar.getTime()));
    }

    private void setupDateAdapter() {
        dateAdapter = new DateAdapter(this, dates, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dateRecyclerView.setLayoutManager(layoutManager);
        dateRecyclerView.setHasFixedSize(true);
        dateRecyclerView.setNestedScrollingEnabled(false);
        dateRecyclerView.setAdapter(dateAdapter);

        if (isInitialLoad) {
            Calendar today = Calendar.getInstance();
            int todayPosition = -1;

            for (int i = 0; i < dates.size(); i++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dates.get(i));
                if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                    todayPosition = i;
                    selectedDate = dates.get(i);
                    break;
                }
            }

            if (todayPosition != -1) {
                final int finalTodayPosition = todayPosition;
                final Date finalSelectedDate = selectedDate;
                dateRecyclerView.post(() -> {
                    scrollToCenter(finalTodayPosition);
                    loadTasksByDate(finalSelectedDate);
                });
            }

            isInitialLoad = false;
        }
    }



    private List<Date> generateDatesForMonth(Calendar calendar) {
        List<Date> dates = new ArrayList<>();
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= maxDays; day++) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            dates.add(cal.getTime());
        }
        return dates;
    }

    private void updateDates(boolean resetSelection) {
        dates = generateDatesForMonth(currentCalendar);
        dateAdapter.updateDates(dates, resetSelection);
        if (resetSelection) {
            selectedDate = null;
            filteredTasks.clear();
            taskListAdapter.notifyDataSetChanged();
        }
    }

    private void loadTasks() {
        tasks = taskDAO.getAllTasksByUserId(userId);
    }

    private void loadTasksByDate(Date date) {
        tasks = taskDAO.getAllTasksByUserId(userId);
        filteredTasks.clear();

        String selectedDateStr = dateFormat.format(date);
        Log.d("DEBUG", "Filtering tasks for date: " + selectedDateStr);

        for (Task task : tasks) {
            String taskDateStr = dateFormat.format(task.getDay());
            if (taskDateStr.equals(selectedDateStr)) {
                filteredTasks.add(task);
            }
        }

        taskListAdapter.notifyDataSetChanged(); // ✅ THAY cho taskAdapter.updateTasks(...)
    }

    @Override
    public void onDateSelected(Date date) {
        selectedDate = date;
        loadTasksByDate(date);

        int selectedPosition = -1;
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).equals(date)) {
                selectedPosition = i;
                break;
            }
        }

        if (selectedPosition != -1) {
            scrollToCenter(selectedPosition);
        }
    }



    @Override
    public void onTaskStatusChanged(Task task, boolean isDone) {
        task.setDone(isDone);
        taskDAO.markTaskDone(task.getId(), isDone);
        taskListAdapter.notifyDataSetChanged(); // ✅
    }

    @Override
    public void onTaskDeleted(Task task) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .create();

        TextView btnCancel = view.findViewById(R.id.btnCancel);
        TextView btnDelete = view.findViewById(R.id.btnDelete);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            taskDAO.deleteTask(task.getId());
            tasks.remove(task);
            filteredTasks.remove(task);
            taskListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã xóa công việc", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
    private void scrollToCenter(int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) dateRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            dateRecyclerView.post(() -> {
                View view = layoutManager.findViewByPosition(position);
                if (view != null) {
                    int offset = dateRecyclerView.getWidth() / 2 - view.getWidth() / 2;
                    layoutManager.scrollToPositionWithOffset(position, offset);
                } else {
                    layoutManager.scrollToPosition(position);
                }
            });
        }
    }
}
