package com.example.todolist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.BarChartView;
import com.example.todolist.R;


import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentStatistics extends Fragment {

    private TextView tvStartDate, tvEndDate, tvTotalHours, tvDailyAverage;
    private TextView tvProductivityScore, tvCompletedTasks, tvPendingTasks, tvCompletionRate;
    private ProgressBar progressProductivity;
    private Button btnToday, btnWeek, btnMonth;
    private BarChartView barChartView;

    // Sample data - in a real app, this would come from your database
    private float[] dailyHours = {7.5f, 8.0f, 9.5f, 6.5f, 8.5f, 2.5f, 0.0f};
    private String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    private int completedTasks = 24;
    private int pendingTasks = 8;

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
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvStartDate = view.findViewById(R.id.tv_start_date);
        tvEndDate = view.findViewById(R.id.tv_end_date);
        tvTotalHours = view.findViewById(R.id.tv_total_hours);
        tvDailyAverage = view.findViewById(R.id.tv_daily_average);
        tvProductivityScore = view.findViewById(R.id.tv_productivity_score);
        tvCompletedTasks = view.findViewById(R.id.tv_completed_tasks);
        tvPendingTasks = view.findViewById(R.id.tv_pending_tasks);
        tvCompletionRate = view.findViewById(R.id.tv_completion_rate);
        progressProductivity = view.findViewById(R.id.progress_productivity);

        btnToday = view.findViewById(R.id.btn_today);
        btnWeek = view.findViewById(R.id.btn_week);
        btnMonth = view.findViewById(R.id.btn_month);

        barChartView = view.findViewById(R.id.bar_chart_view);
        barChartView.setValues(dailyHours);

        // Set up date range buttons
        setupDateRangeButtons();

        // Set default to weekly view
        updateDateRange(DateRange.WEEK);

        // Update statistics
        updateStatistics();
    }

    private void setupDateRangeButtons() {
        btnToday.setOnClickListener(v -> {
            updateDateRange(DateRange.TODAY);
            updateStatistics();
            // Update chart data for today
            float[] todayData = {dailyHours[getCurrentDayIndex()]};
            barChartView.setValues(todayData);
        });

        btnWeek.setOnClickListener(v -> {
            updateDateRange(DateRange.WEEK);
            updateStatistics();
            // Reset to weekly data
            barChartView.setValues(dailyHours);
        });

        btnMonth.setOnClickListener(v -> {
            updateDateRange(DateRange.MONTH);
            updateStatistics();
            // For demo, we'll just use the same data
            // In a real app, you'd load the monthly data
            barChartView.setValues(dailyHours);
        });
    }

    private int getCurrentDayIndex() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Convert to 0-based index (Monday = 0)
        return (dayOfWeek + 5) % 7;
    }

    private void updateDateRange(DateRange range) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();

        switch (range) {
            case TODAY:
                tvStartDate.setText(sdf.format(endDate));
                tvEndDate.setText(sdf.format(endDate));
                break;

            case WEEK:
                calendar.add(Calendar.DAY_OF_YEAR, -6);
                Date startDate = calendar.getTime();
                tvStartDate.setText(sdf.format(startDate));
                tvEndDate.setText(sdf.format(endDate));
                break;

            case MONTH:
                calendar.add(Calendar.MONTH, -1);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date monthStartDate = calendar.getTime();
                tvStartDate.setText(sdf.format(monthStartDate));
                tvEndDate.setText(sdf.format(endDate));
                break;
        }
    }

    private void updateStatistics() {
        // Calculate total hours
        float totalHours = 0;
        for (float hours : dailyHours) {
            totalHours += hours;
        }

        // Update UI
        tvTotalHours.setText(String.format(Locale.getDefault(), "%.1f", totalHours));
        tvDailyAverage.setText(String.format(Locale.getDefault(), "%.1f", totalHours / 7));

        tvCompletedTasks.setText(String.valueOf(completedTasks));
        tvPendingTasks.setText(String.valueOf(pendingTasks));

        int completionRate = (int) (((float) completedTasks / (completedTasks + pendingTasks)) * 100);
        tvCompletionRate.setText(completionRate + "%");

        // Productivity score (this would be calculated based on your app's logic)
        int productivityScore = 85;
        tvProductivityScore.setText(productivityScore + "%");
        progressProductivity.setProgress(productivityScore);
    }

    // Method to update data from outside the fragment
    public void updateData(float[] newDailyHours, int newCompletedTasks, int newPendingTasks) {
        this.dailyHours = newDailyHours;
        this.completedTasks = newCompletedTasks;
        this.pendingTasks = newPendingTasks;

        if (isAdded()) {
            barChartView.setValues(dailyHours);
            updateStatistics();
        }
    }

    private enum DateRange {
        TODAY, WEEK, MONTH
    }
}
