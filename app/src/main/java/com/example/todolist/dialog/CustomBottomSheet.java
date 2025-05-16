package com.example.todolist.dialog;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.adapter.SubjectRecycleViewAdapter;
import com.example.todolist.dao.impl.SQLiteSubjectDAO;
import com.example.todolist.model.Subject;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CustomBottomSheet {

    private Context context;
    private Activity activity;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView recyclerView;
    private SubjectRecycleViewAdapter adapter;
    private List<Subject> subjectList;
    private TextView subject_date;
    private ImageButton subject_img_calender;
    private FloatingActionButton subject_bt_add;

    public CustomBottomSheet(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void showBottomSheet(String message) {
        bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(R.layout.calender_bottom_sheet);

        init();
        initAdapter();

        // Bắt sự kiện cho nút "Xem tất cả"
        androidx.appcompat.widget.AppCompatButton subject_btn_view_all = bottomSheetDialog.findViewById(R.id.subject_btn_view_all);
        subject_btn_view_all.setOnClickListener(v -> {
            refreshSubjectList(); // Tải lại toàn bộ danh sách môn học
        });

        subject_img_calender.setOnClickListener(v -> showDatePicker());
        subject_bt_add.setOnClickListener(v -> addSubject());

        bottomSheetDialog.show();
    }

    private void addSubject() {
        SubjectAddDialog.showCustomDialog(context, 1, () -> {
            refreshSubjectList();
        });
    }

    private void editSubject(Subject subject) {
        SubjectEditDialog.showEditDialog(context, subject, () -> {
            refreshSubjectList();
        });
    }

    private void refreshSubjectList() {
        SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(context);
        subjectList.clear();
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        List<Subject> updatedList = subjectDAO.getAllSubjectsByUserId(userId);
        if (updatedList != null) {
            subjectList.addAll(updatedList);
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshSubjectListByWeekday(String weekday, String date) {
        SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(context);
        subjectList.clear();
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        List<Subject> updatedList = subjectDAO.getSubjectsByWeekday(userId, weekday, date);
        if (updatedList != null) {
            subjectList.addAll(updatedList);
        }
        adapter.notifyDataSetChanged();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Định dạng ngày được chọn
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    subject_date.setText(selectedDate);

                    // Tính thứ trong tuần từ ngày được chọn
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    String[] daysOfWeek = {
                            "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"
                    };
                    String weekday = daysOfWeek[dayOfWeek - 1]; // Điều chỉnh để phù hợp với mảng

                    // Làm mới danh sách môn học theo weekday và ngày
                    refreshSubjectListByWeekday(weekday, selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void init() {
        subject_date = bottomSheetDialog.findViewById(R.id.subject_date);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        subject_date.setText(currentDate);

        recyclerView = bottomSheetDialog.findViewById(R.id.subject_recycle_view);
        subject_img_calender = bottomSheetDialog.findViewById(R.id.subject_img_calender);
        subject_bt_add = bottomSheetDialog.findViewById(R.id.subject_btn_add);
    }

    private void initAdapter() {
        // Lấy thời gian và thứ hôm nay
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        String[] daysOfWeek = {
                "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"
        };
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 1 = Sunday, 7 = Saturday
        String dayOfWeekString = daysOfWeek[dayOfWeek - 1]; // Điều chỉnh để phù hợp với mảng

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        subjectList = new ArrayList<>();

        SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(context);
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        List<Subject> subjects = subjectDAO.getSubjectsByWeekday(userId, dayOfWeekString, formattedDate);
        if (subjects != null) {
            subjectList.addAll(subjects);
        }

        adapter = new SubjectRecycleViewAdapter(subjectList, new SubjectRecycleViewAdapter.OnSubjectActionListener() {
            @Override
            public void onDeleteSubject(int subjectId, int position) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa môn học này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            try {
                                subjectDAO.deleteSubject(subjectId);
                                adapter.removeItem(position);
                                Toast.makeText(context, "Xóa môn học thành công", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Lỗi khi xóa môn học", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }

            @Override
            public void onEditSubject(Subject subject) {
                editSubject(subject);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}