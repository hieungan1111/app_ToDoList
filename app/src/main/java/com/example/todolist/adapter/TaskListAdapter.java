package com.example.todolist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListAdapter extends BaseAdapter {

    private final Context context;
    private final List<Task> tasks;
    private final OnTaskActionListener listener;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public TaskListAdapter(Context context, List<Task> tasks, OnTaskActionListener listener) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    public interface OnTaskActionListener {
        void onTaskStatusChanged(Task task, boolean isDone);
        void onTaskDeleted(Task task);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = tasks.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("personal".equals(task.getType())) {
            holder.tvPersonalActivity.setText(R.string.personal_activity); // "Hoạt động cá nhân"
            holder.tvPersonalActivity.setVisibility(View.VISIBLE);
        } else {
            holder.tvPersonalActivity.setText(R.string.external_activity); // "Hoạt động ngoại khóa"
            holder.tvPersonalActivity.setVisibility(View.VISIBLE);
        }

        holder.tvTaskTime.setText(timeFormat.format(task.getTimeStart()));
        holder.tvTaskContent.setText(task.getContent());
        holder.cbTaskDone.setOnCheckedChangeListener(null); // reset listener cũ trước khi gán mới
        holder.cbTaskDone.setChecked(task.isDone());

        // Gán lại listener
        holder.cbTaskDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                listener.onTaskStatusChanged(task, isChecked);
            }
        });

        switch (task.getTaskPriority()) {
            case LOW:
                holder.tvTaskPriority.setText(R.string.low_priority);
                break;
            case MEDIUM:
                holder.tvTaskPriority.setText(R.string.medium_priority);
                break;
            case HIGH:
                holder.tvTaskPriority.setText(R.string.high_priority);
                break;
        }

        if (task.isDone()) {
            holder.tvTaskContent.setPaintFlags(holder.tvTaskContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTaskStatus.setVisibility(View.VISIBLE);
            holder.tvTaskStatus.setText(R.string.completed); // "Đã hoàn thành"
            holder.tvTaskStatus.setBackgroundColor(Color.parseColor("#E6F4EA"));
            holder.tvTaskStatus.setTextColor(Color.parseColor("#34A853"));
        } else if (task.getTimeStart().before(new Date())) {
            holder.tvTaskContent.setPaintFlags(holder.tvTaskContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTaskStatus.setVisibility(View.VISIBLE);
            holder.tvTaskStatus.setText("Quá hạn");
            holder.tvTaskStatus.setBackgroundColor(Color.parseColor("#FEEAEA"));
            holder.tvTaskStatus.setTextColor(Color.parseColor("#EA4335"));
        } else {
            holder.tvTaskContent.setPaintFlags(holder.tvTaskContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTaskStatus.setVisibility(View.GONE);
        }

        holder.btnDeleteTask.setOnClickListener(v -> listener.onTaskDeleted(task));
        // Ẩn mặc định

        return convertView;
    }

    static class ViewHolder {
        TextView tvTaskTime, tvTaskContent, tvTaskPriority, tvPersonalActivity, tvTaskStatus;
        CheckBox cbTaskDone;
        ImageButton btnDeleteTask;

        ViewHolder(View view) {
            tvTaskTime = view.findViewById(R.id.tvTaskTime);
            tvTaskContent = view.findViewById(R.id.tvTaskContent);
            tvTaskPriority = view.findViewById(R.id.tvTaskPriority);
            tvPersonalActivity = view.findViewById(R.id.tvPersonalActivity);
            tvTaskStatus = view.findViewById(R.id.tvTaskStatus);
            cbTaskDone = view.findViewById(R.id.cbTaskDone);
            btnDeleteTask = view.findViewById(R.id.btnDeleteTask);
        }
    }
}
