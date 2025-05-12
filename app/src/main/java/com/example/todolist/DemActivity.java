package com.example.todolist;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DemActivity extends AppCompatActivity {
    private TextView tvTime;
    private Button btnStop;
    private GridLayout gridLayout;
    private CountDownTimer countDownTimer;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dem);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        tvTime = findViewById(R.id.tv_time);
        btnStop = findViewById(R.id.btn_stop);
        gridLayout = findViewById(R.id.gridLayout);

        int[] buttonIds = {
                R.id.btn_1m, R.id.btn_5m, R.id.btn_10m, R.id.btn_15m,
                R.id.btn_20m, R.id.btn_25m, R.id.btn_45m, R.id.btn_60m, R.id.btn_120m
        };

        for (int id : buttonIds) {
            Button b = findViewById(id);
            b.setOnClickListener(v -> {
                String text = b.getText().toString();
                int minutes = Integer.parseInt(text.split(" ")[0]);
                long millis = minutes * 60 * 1000L; // chuyển phút thành milliseconds

                gridLayout.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);

                // Khởi tạo và bắt đầu đếm
                countDownTimer = new CountDownTimer(millis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000);
                        int min = seconds / 60;
                        int sec = seconds % 60;
                        tvTime.setText(String.format("%02d:%02d", min, sec));
                    }

                    @Override
                    public void onFinish() {
                        tvTime.setText("00:00");
                        btnStop.setVisibility(View.GONE);
                        gridLayout.setVisibility(View.VISIBLE);
                    }
                }.start();
            });
        }

        btnStop.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel(); // Dừng đếm
            }
            gridLayout.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
            tvTime.setText("00:00");
        });

    }
}