package com.example.todolist.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.dao.impl.SQLiteSubjectDAO;
import com.example.todolist.helper.HandleDate;
import com.example.todolist.model.Subject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubjectAddDialog {

    // Interface để thông báo khi thêm môn học thành công
    public interface OnSubjectAddedListener {
        void onSubjectAdded();
    }

    public static void showCustomDialog(Context context, int userId, OnSubjectAddedListener listener) {
        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subject_add_dialog, null);

        // Tạo dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        // Khởi tạo các thành phần giao diện
        EditText subjectNameEditText = view.findViewById(R.id.subject_add_suject_name);
        TextView startDateTextView = view.findViewById(R.id.subject_add_tv_start_date);
        TextView endDateTextView = view.findViewById(R.id.subject_add_tv_end_date);
        TextView startTimeTextView = view.findViewById(R.id.subject_add_tv_start_time);
        TextView endTimeTextView = view.findViewById(R.id.subject_add_tv_end_time);
        CheckBox mondayCheckBox = view.findViewById(R.id.subject_add_btn_monday);
        CheckBox tuesdayCheckBox = view.findViewById(R.id.subject_add_btn_tuesday);
        CheckBox wednesdayCheckBox = view.findViewById(R.id.subject_add_btn_wednesday);
        CheckBox thursdayCheckBox = view.findViewById(R.id.subject_add_btn_thursday);
        CheckBox fridayCheckBox = view.findViewById(R.id.subject_add_btn_friday);
        CheckBox saturdayCheckBox = view.findViewById(R.id.subject_add_btn_saturday);
        CheckBox sundayCheckBox = view.findViewById(R.id.subject_add_btn_sunday);
        Button btnCancel = view.findViewById(R.id.subject_add_btn_cancel);
        Button btnAdd = view.findViewById(R.id.subject_add_btn_add);

        // Xử lý sự kiện cho nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý sự kiện cho nút Thêm
        btnAdd.setOnClickListener(v -> {
            // Thu thập dữ liệu từ giao diện
            String subjectName = subjectNameEditText.getText().toString().trim();
            String rangeStart = startDateTextView.getText().toString();
            String rangeEnd = endDateTextView.getText().toString();
            String timeStart = startTimeTextView.getText().toString();
            String timeEnd = endTimeTextView.getText().toString();

            // Thu thập các ngày trong tuần được chọn
            List<String> weekdays = new ArrayList<>();
            if (mondayCheckBox.isChecked()) weekdays.add("monday");
            if (tuesdayCheckBox.isChecked()) weekdays.add("tuesday");
            if (wednesdayCheckBox.isChecked()) weekdays.add("wednesday");
            if (thursdayCheckBox.isChecked()) weekdays.add("thursday");
            if (fridayCheckBox.isChecked()) weekdays.add("friday");
            if (saturdayCheckBox.isChecked()) weekdays.add("saturday");
            if (sundayCheckBox.isChecked()) weekdays.add("sunday");

            // Kiểm tra dữ liệu đầu vào
            if (subjectName.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập tên môn học", Toast.LENGTH_SHORT).show();
                return;
            }
            if (weekdays.isEmpty()) {
                Toast.makeText(context, "Vui lòng chọn ít nhất một ngày trong tuần", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate ngày giờ
            if (HandleDate.compareDates(rangeStart, rangeEnd) != -1) {
                Toast.makeText(context, "Vui lòng chọn ngày bắt đầu nhỏ hơn ngày kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }

            if (HandleDate.compareTimes(timeStart, timeEnd) != -1) {
                Toast.makeText(context, "Vui lòng chọn giờ bắt đầu nhỏ hơn giờ kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng Subject
            Subject subject = new Subject(
                    rangeStart,
                    rangeEnd,
                    timeStart,
                    timeEnd,
                    subjectName,
                    weekdays,
                    userId
            );

            // Lưu vào cơ sở dữ liệu
            try {
                SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(context);
                subjectDAO.addSubject(subject);
                Toast.makeText(context, "Thêm môn học thành công", Toast.LENGTH_SHORT).show();
                // Gọi callback để thông báo thêm thành công
                if (listener != null) {
                    listener.onSubjectAdded();
                }
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Lỗi khi thêm môn học: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện chọn ngày bắt đầu
        startDateTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        startDateTextView.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Xử lý sự kiện chọn ngày kết thúc
        endDateTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        endDateTextView.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Xử lý sự kiện chọn giờ bắt đầu
        startTimeTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view1, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        startTimeTextView.setText(time);
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });

        // Xử lý sự kiện chọn giờ kết thúc
        endTimeTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view1, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        endTimeTextView.setText(time);
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });

        dialog.show();
    }
}