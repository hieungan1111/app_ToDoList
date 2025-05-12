package com.example.todolist.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.todolist.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomBottomSheet {

    private Activity activity;
    private Dialog bottomSheetDialog;

    public CustomBottomSheet(Activity activity) {
        this.activity = activity;
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
        TextView textView = bottomSheetDialog.findViewById(R.id.textView);
        Button closeButton = bottomSheetDialog.findViewById(R.id.buttonClose);

        // Cập nhật nội dung của TextView
        if (textView != null) {
            textView.setText(message);
        }

        // Đặt hành động cho Button đóng
        if (closeButton != null) {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();  // Đóng BottomSheet khi nhấn nút
                }
            });
        }

        // Hiển thị BottomSheet
        bottomSheetDialog.show();
    }
}
