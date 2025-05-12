package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    private SQLiteTaskDAO taskDAO;
    private int userId;
    private TextView tvSelectedDate;
    private NumberPicker hourPicker, minutePicker, amPmPicker;
    private Spinner prioritySpinner;
    private EditText etTaskContent;
    private CheckBox cbPersonalActivity, cbExternalActivity;
    private Button btnSave;

    private Calendar selectedDateTime;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initViews();
        setupListeners();

        cbPersonalActivity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) cbExternalActivity.setChecked(false);
        });

        cbExternalActivity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) cbPersonalActivity.setChecked(false);
        });

        // Get selected date from intent
        long selectedDateMillis = getIntent().getLongExtra("selected_date", System.currentTimeMillis());
        selectedDateTime = Calendar.getInstance();
        selectedDateTime.setTimeInMillis(selectedDateMillis);

        tvSelectedDate.setText(dateFormat.format(selectedDateTime.getTime()));
        setupTimePickers();
        setupPrioritySpinner();

        taskDAO = new SQLiteTaskDAO(this);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
    }

    private void initViews() {
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        amPmPicker = findViewById(R.id.amPmPicker);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        etTaskContent = findViewById(R.id.etTaskContent);
        cbPersonalActivity = findViewById(R.id.cbPersonalActivity);
        cbExternalActivity = findViewById(R.id.cbExternalActivity);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupListeners() {
        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void setupTimePickers() {
        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setValue(selectedDateTime.get(Calendar.HOUR) == 0 ? 12 : selectedDateTime.get(Calendar.HOUR));

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));
        minutePicker.setValue(selectedDateTime.get(Calendar.MINUTE));

        amPmPicker.setMinValue(0);
        amPmPicker.setMaxValue(1);
        amPmPicker.setDisplayedValues(new String[]{"AM", "PM"});
        amPmPicker.setValue(selectedDateTime.get(Calendar.AM_PM));
    }

    private void setupPrioritySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.priority_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setSelection(1); // Medium default
    }

    private void saveTask() {
        String content = etTaskContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_task_content, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbPersonalActivity.isChecked() && !cbExternalActivity.isChecked()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một loại hoạt động", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = hourPicker.getValue();
        int minute = minutePicker.getValue();
        int amPm = amPmPicker.getValue();
        selectedDateTime.set(Calendar.HOUR, hour % 12);
        selectedDateTime.set(Calendar.MINUTE, minute);
        selectedDateTime.set(Calendar.AM_PM, amPm);

        Task.TaskPriority priority;
        switch (prioritySpinner.getSelectedItemPosition()) {
            case 0:
                priority = Task.TaskPriority.LOW;
                break;
            case 2:
                priority = Task.TaskPriority.HIGH;
                break;
            case 1:
            default:
                priority = Task.TaskPriority.MEDIUM;
                break;
        }

        // 👇 Xác định loại công việc
        String taskType = cbPersonalActivity.isChecked() ? "personal" : "extracurricular";

        // 👇 Tạo task mới với type
        Task newTask = new Task(
                0,
                selectedDateTime.getTime(),
                selectedDateTime.getTime(),
                content,
                false,
                userId,
                priority,
                false,
                taskType
        );

        taskDAO.addTask(newTask);
        Toast.makeText(this, R.string.task_added, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
