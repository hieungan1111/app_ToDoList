package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class UpcomingTaskRecyclerAdapter extends RecyclerView.Adapter<UpcomingTaskRecyclerAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks;

    public UpcomingTaskRecyclerAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_upcoming_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvContent.setText(task.getContent());

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy - hh:mm a", Locale.getDefault());
        holder.tvTime.setText(sdf.format(task.getDay()));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvTaskContent);
            tvTime = itemView.findViewById(R.id.tvTaskTime);
        }
    }
}
