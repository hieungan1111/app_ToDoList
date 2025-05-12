package com.example.todolist.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

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

    private  Context context;
    private Activity activity;

    private Dialog bottomSheetDialog;
    private RecyclerView recyclerView;
    private SubjectRecycleViewAdapter adapter;
    private List<Subject> subjectList;
    private  TextView subject_date;
    private ImageButton subject_img_calender;
    private FloatingActionButton subject_bt_add;


    public CustomBottomSheet(Activity activity, Context context) {
        this.activity = activity;
        this.context =context;
    }

    // Phương thức để hiển thị BottomSheet
    public void showBottomSheet(String message) {
        // Tạo một Dialog mới
        bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // Ẩn tiêu đề của dialog
        bottomSheetDialog.setCancelable(true);  // Cho phép đóng khi nhấn ra ngoài
        // Set layout cho BottomSheet
        bottomSheetDialog.setContentView(R.layout.calender_bottom_sheet);

        // Tìm các thành phần UI trong layout
        init();

        //khoiwr tao adapter
        initAdapter();
        //
        subject_img_calender.setOnClickListener(v -> showDatePicker());
        subject_bt_add.setOnClickListener(v -> addSubject());


        // Hiển thị BottomSheet
        bottomSheetDialog.show();
    }

    private void addSubject() {
        SubjectAddDialog.showCustomDialog(context, "Bạn đã đăng ký thành công!");
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Tháng tính từ 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Cập nhật TextView với ngày được chọn
                    String selectedDate = String.format("%02d/%02d/%04d",
                            selectedDay, selectedMonth + 1, selectedYear);
                    subject_date.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    void init(){
        subject_date = bottomSheetDialog.findViewById(R.id.subject_date);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        String currentDate = sdf.format(calendar.getTime());
        subject_date.setText(currentDate);
        recyclerView = bottomSheetDialog.findViewById(R.id.subject_recycle_view);
        subject_img_calender = bottomSheetDialog.findViewById(R.id.subject_img_calender);
        subject_bt_add = bottomSheetDialog.findViewById(R.id.subject_btn_add);
    }

    void initAdapter(){

        if( context != null){
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
        subjectList = new ArrayList<>();
        SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(context);
        subjectList = subjectDAO.getAllSubjectsByUserId(1);

        System.out.println(subjectList.size());
        adapter = new SubjectRecycleViewAdapter(subjectList);

        recyclerView.setAdapter(adapter);
    }
}
