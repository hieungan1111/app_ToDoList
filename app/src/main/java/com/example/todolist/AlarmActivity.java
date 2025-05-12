package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapter.AlarmAdapter;
import com.example.todolist.dao.impl.SQLiteAlarmDAO;
import com.example.todolist.model.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmActivity extends AppCompatActivity implements AlarmAdapter.OnAlarmClickListener {

    Button addAlarmDialog;
    SQLiteAlarmDAO dbHelper;
    AlarmAdapter alarmAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        dbHelper = new SQLiteAlarmDAO(this);
        recyclerView = findViewById(R.id.recyclerAlarm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        alarmAdapter = new AlarmAdapter(this, dbHelper.getAllAlarms(), this, this);
        recyclerView.setAdapter(alarmAdapter);

        addAlarmDialog = findViewById(R.id.addAlarmDialog);
        addAlarmDialog.setOnClickListener(v -> openAlarmDialog());
    }

    private void openAlarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alarm_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        EditText etAlarmName = dialogView.findViewById(R.id.etAlarmName);

        CheckBox cbMon = dialogView.findViewById(R.id.cbMon);
        CheckBox cbTue = dialogView.findViewById(R.id.cbTue);
        CheckBox cbWed = dialogView.findViewById(R.id.cbWed);
        CheckBox cbThu = dialogView.findViewById(R.id.cbThu);
        CheckBox cbFri = dialogView.findViewById(R.id.cbFri);
        CheckBox cbSat = dialogView.findViewById(R.id.cbSat);
        CheckBox cbSun = dialogView.findViewById(R.id.cbSun);

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String alarmName = etAlarmName.getText().toString().trim();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            // Nếu giờ này đã qua hôm nay, đặt sang ngày mai
            if (cal.before(Calendar.getInstance())) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }

            List<String> repeatDays = new ArrayList<>();
            if (cbMon.isChecked()) repeatDays.add("T2");
            if (cbTue.isChecked()) repeatDays.add("T3");
            if (cbWed.isChecked()) repeatDays.add("T4");
            if (cbThu.isChecked()) repeatDays.add("T5");
            if (cbFri.isChecked()) repeatDays.add("T6");
            if (cbSat.isChecked()) repeatDays.add("T7");
            if (cbSun.isChecked()) repeatDays.add("CN");

            Toast.makeText(this,
                    "Giờ: " + hour + ":" + minute + "\nTên: " + alarmName + "\nLặp lại: " + repeatDays,
                    Toast.LENGTH_LONG).show();

            Alarm newAlarm = new Alarm(
                    0, alarmName, cal.getTime(), repeatDays, true, new Date(), 1
            );

            dbHelper.addAlarm(newAlarm);
            alarmAdapter.setAlarms(dbHelper.getAllAlarms());
            setAlarm(newAlarm);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onAlarmLongClick(Alarm alarm, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa báo thức")
                .setMessage("Bạn có chắc chắn muốn xóa báo thức này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteAlarm(alarm.getId());  // Xóa báo thức khỏi DB
                    alarmAdapter.setAlarms(dbHelper.getAllAlarms());  // Cập nhật lại RecyclerView
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarmName", alarm.getAlarmName());
        intent.putExtra("alarmId", alarm.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, alarm.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarm.getTime().getTime(),
                pendingIntent
        );
    }

    public void cancelAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, alarm.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.cancel(pendingIntent);
    }


}
