package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private final Context context;
    private List<Reminder> reminderList;
    private final OnReminderDeleteListener deleteListener;

    public interface OnReminderDeleteListener {
        void onDelete(Reminder reminder);
    }

    public ReminderAdapter(Context context, List<Reminder> reminderList, OnReminderDeleteListener deleteListener) {
        this.context = context;
        this.reminderList = reminderList;
        this.deleteListener = deleteListener;
    }

    public void setData(List<Reminder> reminders) {
        this.reminderList = reminders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        holder.tvDate.setText(dateFormat.format(reminder.getTime()));
        holder.tvTime.setText(timeFormat.format(reminder.getTime()));
        holder.tvTitle.setText(reminder.getTitle());
        holder.tvType.setText(reminder.getType());
        holder.tvPriority.setText(reminder.getPriority());

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDelete(reminder);
        });
    }

    @Override
    public int getItemCount() {
        return reminderList != null ? reminderList.size() : 0;
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvTitle, tvType, tvPriority;
        ImageButton btnDelete;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvType = itemView.findViewById(R.id.tvType);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
