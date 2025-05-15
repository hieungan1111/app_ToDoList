package com.example.todolist.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.dao.impl.SQLiteDeadlineDAO;
import com.example.todolist.dao.impl.SQLiteSubjectDAO;
import com.example.todolist.model.Deadline;
import com.example.todolist.model.Subject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeadlineAddDialog {

    // Interface để thông báo khi thêm deadline thành công
    public interface OnDeadlineAddedListener {
        void onDeadlineAdded();
    }

    public static void showCustomDialog(Context context,  OnDeadlineAddedListener listener) {
        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.deadline_add_dialog, null);

        // Tạo dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        // Khởi tạo các thành phần giao diện
        EditText deadlineNameEditText = view.findViewById(R.id.deadline_add_deadline_name);
        Spinner subjectSpinner = view.findViewById(R.id.deadline_add_spinner);
        TextView endDateTextView = view.findViewById(R.id.deadline_add_tv_end_date);
        TextView endTimeTextView = view.findViewById(R.id.deadline_add_tv_end_time);
        Button btnCancel = view.findViewById(R.id.deadline_add_btn_cancel);
        Button btnAdd = view.findViewById(R.id.deadline_add_btn_add);

        // Lấy danh sách môn học từ cơ sở dữ liệu
        SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(context);
        List<Subject> subjects = subjectDAO.getAllSubjectsByUserId(1);
        List<String> subjectNames = new ArrayList<>();
        subjectNames.add("Chọn môn học"); // Mục mặc định
        for (Subject subject : subjects) {
            subjectNames.add(subject.getSubjectName());
        }

        // Thiết lập Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, subjectNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(spinnerAdapter);

        // Xử lý sự kiện cho nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý sự kiện cho nút Thêm
        btnAdd.setOnClickListener(v -> {
            // Thu thập dữ liệu từ giao diện
            String deadlineName = deadlineNameEditText.getText().toString().trim();
            String selectedSubject = subjectSpinner.getSelectedItem() != null
                    ? subjectSpinner.getSelectedItem().toString() : "";
            String endDate = endDateTextView.getText().toString();
            String endTime = endTimeTextView.getText().toString();

            // Kiểm tra dữ liệu đầu vào
            if (deadlineName.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập tên deadline", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedSubject.equals("Chọn môn học")) {
                Toast.makeText(context, "Vui lòng chọn môn học", Toast.LENGTH_SHORT).show();
                return;
            }

            // lay idsubject
            int idSubject = 0;
            for (Subject subject : subjects) {
                if( selectedSubject.equals(subject.getSubjectName())){
                    idSubject = subject.getId();
                }
            }
            // Tạo đối tượng Deadline
            Toast.makeText(context, "tao deadline dang tao", Toast.LENGTH_SHORT).show();
            Deadline deadline = new Deadline(
                    endDate,
                    endTime,
                    selectedSubject,
                    idSubject,
                    deadlineName,
                    false,
                    1
            );

            // Lưu vào cơ sở dữ liệu
            try {
                SQLiteDeadlineDAO deadlineDAO = new SQLiteDeadlineDAO(context);
                deadlineDAO.addDeadline(deadline);
                Toast.makeText(context, "Thêm deadline thành công", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onDeadlineAdded();
                }
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Lỗi khi thêm deadline: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện chọn ngày nộp
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

        // Xử lý sự kiện chọn giờ nộp
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