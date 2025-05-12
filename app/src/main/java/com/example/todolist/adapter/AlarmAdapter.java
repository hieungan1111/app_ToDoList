package com.example.todolist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AlarmActivity;
import com.example.todolist.R;
import com.example.todolist.dao.impl.SQLiteAlarmDAO;
import com.example.todolist.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private List<Alarm> alarmList;
    private Context context;
    private OnAlarmClickListener onAlarmClickListener;
    private AlarmActivity alarmActivity;
    private SQLiteAlarmDAO dbHelper;

    // Constructor
    public AlarmAdapter(Context context,
                        List<Alarm> alarmList,
                        OnAlarmClickListener onAlarmClickListener,
                        AlarmActivity alarmActivity) {
        this.context = context;
        this.alarmList = alarmList;
        this.onAlarmClickListener = onAlarmClickListener;
        this.alarmActivity = alarmActivity;
        this.dbHelper = new SQLiteAlarmDAO(context);
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);

        System.out.println();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        holder.txtTime.setText(sdf.format(alarm.time));
        holder.txtLabel.setText(alarm.alarmName);
        holder.switchEnable.setChecked(alarm.isEnable);

        holder.switchEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alarm.setEnable(isChecked); // cập nhật trạng thái enable

            if (isChecked) {
                alarmActivity.setAlarm(alarm); // bật báo thức
            } else {
                alarmActivity.cancelAlarm(alarm); // tắt báo thức
            }

            // Cập nhật trong database
            dbHelper.updateAlarm(alarm);

        });


        String repeatDaysText = "Lặp lại: " + String.join(", ", alarm.repeatDays);
        holder.txtRepeatDays.setText(repeatDaysText);

        // Long click listener để xóa
        holder.itemView.setOnLongClickListener(v -> {
            // Gọi callback khi người dùng nhấn giữ vào item
            onAlarmClickListener.onAlarmLongClick(alarm, position);
            return true;  // Trả về true để ngừng xử lý sự kiện click tiếp theo
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    // ViewHolder cho mỗi item trong RecyclerView
    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime, txtLabel, txtRepeatDays;
        Switch switchEnable;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtLabel = itemView.findViewById(R.id.txtLabel);
            switchEnable = itemView.findViewById(R.id.switchEnable);
            txtRepeatDays = itemView.findViewById(R.id.txtRepeatDays);
        }
    }

    // Cập nhật danh sách alarm trong adapter
    @SuppressLint("NotifyDataSetChanged")
    public void setAlarms(List<Alarm> alarms) {
        this.alarmList = alarms;
        notifyDataSetChanged();
    }

    // Interface để xử lý sự kiện long click trong Activity hoặc Fragment
    public interface OnAlarmClickListener {
        void onAlarmLongClick(Alarm alarm, int position);
    }
}
