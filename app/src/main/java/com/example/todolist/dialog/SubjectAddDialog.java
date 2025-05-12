package com.example.todolist.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todolist.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SubjectAddDialog {
    public static void showCustomDialog(Context context, String message) {

        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subject_add_dialog, null);

        // Tạo dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        dialog.show();


        // Khởi tạo các Button
        Button btnCancel = view.findViewById(R.id.subject_add_btn_cancel);
        Button btnAdd = view.findViewById(R.id.subject_add_btn_add);

        // Xử lý sự kiện cho nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý sự kiện cho nút Thêm
        btnAdd.setOnClickListener(v -> {
            // Thực hiện thao tác thêm môn học
            Toast.makeText(context, "Môn học đã được thêm", Toast.LENGTH_SHORT).show();
            // Đóng dialog sau khi thêm
        });

        // Xử lý sự kiện cho các nút ngày trong tuần

        List<String> weekdays = new ArrayList<>();
        View btnMonday = view.findViewById(R.id.subject_add_btn_monday);
        btnMonday.setOnClickListener(v -> {
            // Xử lý sự kiện cho ngày T2
            weekdays.add("monday");
            Toast.makeText(context, "Chọn Thứ 2", Toast.LENGTH_SHORT).show();
        });

        View btnTuesday = view.findViewById(R.id.subject_add_btn_tuesday);
        btnTuesday.setOnClickListener(v -> {
            // Xử lý sự kiện cho ngày T3
            weekdays.add("tuesday");
            Toast.makeText(context, "Chọn Thứ 3", Toast.LENGTH_SHORT).show();
        });

        View btnWednesday = view.findViewById(R.id.subject_add_btn_wednesday);
        btnWednesday.setOnClickListener(v -> {
            // Xử lý sự kiện cho ngày T4
            weekdays.add("wednesday");
            Toast.makeText(context, "Chọn Thứ 4", Toast.LENGTH_SHORT).show();
        });

        View btnThursday = view.findViewById(R.id.subject_add_btn_thursday);
        btnThursday.setOnClickListener(v -> {
            // Xử lý sự kiện cho ngày T5
            weekdays.add("thursday");
            Toast.makeText(context, "Chọn Thứ 5", Toast.LENGTH_SHORT).show();
        });

        View btnFriday = view.findViewById(R.id.subject_add_btn_friday);
        btnFriday.setOnClickListener(v -> {
            // Xử lý sự kiện cho ngày T6
            weekdays.add("friday");
            Toast.makeText(context, "Chọn Thứ 6", Toast.LENGTH_SHORT).show();
        });

        View btnSaturday = view.findViewById(R.id.subject_add_btn_saturday);
        btnSaturday.setOnClickListener(v -> {
            // Xử lý sự kiện cho ngày T7
            weekdays.add("saturday");
            Toast.makeText(context, "Chọn Thứ 7", Toast.LENGTH_SHORT).show();
        });

        View btnSunday = view.findViewById(R.id.subject_add_btn_sunday);
        btnSunday.setOnClickListener(v -> {
            // Xử lý sự kiện cho ngày CN
            weekdays.add("sunday");
            btnSunday.setBackgroundResource(R.drawable.bg_select_add_subject);
            Toast.makeText(context, "Chọn Chủ nhật", Toast.LENGTH_SHORT).show();
        });


        // x
        TextView timeStart = view.findViewById(R.id.subject_add_tv_start_time);
        timeStart.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    ( timePicker, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        timeStart.setText(time); // Ví dụ: cập nhật TextView
                    },
                    hour,
                    minute,
                    true // true = 24h format, false = AM/PM
            );
            timePickerDialog.show();
        });

        TextView timeEnd = view.findViewById(R.id.subject_add_tv_end_time);
        timeEnd.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (timePicker, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        timeEnd.setText(time); // Ví dụ: cập nhật TextView
                    },
                    hour,
                    minute,
                    true // true = 24h format, false = AM/PM
            );
            timePickerDialog.show();
        });

        TextView dateStart = view.findViewById(R.id.subject_add_tv_start_date);
        dateStart.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH); // Tháng tính từ 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                        // Cập nhật TextView với ngày được chọn
                        String selectedDate = String.format("%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        dateStart.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        TextView dateEnd = view.findViewById(R.id.subject_add_tv_end_date);
        dateEnd.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH); // Tháng tính từ 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                        // Cập nhật TextView với ngày được chọn
                        String selectedDate = String.format("%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        dateEnd.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });


        dialog.show();
    }

    void init(){

    }




}
