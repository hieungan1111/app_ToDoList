package com.example.todolist.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.ScheduleNotifications;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final List<ScheduleNotifications> notificationList;

    public NotificationAdapter(List<ScheduleNotifications> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleNotifications notification = notificationList.get(position);

        holder.tvTitle.setText("Thông báo hoạt động");
        holder.tvTime.setText("Đã thông báo lúc: " +
                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(notification.getNotificationTime()));
        holder.tvContent.setText(notification.getTitle());

        // 👇 Đánh dấu font đậm nếu chưa xem
        if (!notification.isViewed()) {
            holder.tvTitle.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvTitle.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime, tvContent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvTime = itemView.findViewById(R.id.tvNotificationTime);
            tvContent = itemView.findViewById(R.id.tvNotificationContent);
        }
    }
}
