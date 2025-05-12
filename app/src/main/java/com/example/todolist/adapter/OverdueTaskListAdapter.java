package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OverdueTaskListAdapter extends BaseAdapter {

    private final Context context;
    private final List<Task> tasks;
    private final LayoutInflater inflater;

    public OverdueTaskListAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_overdue_task, parent, false);
            holder = new ViewHolder();
            holder.tvDate = convertView.findViewById(R.id.tvDate);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvType = convertView.findViewById(R.id.tvType);
            holder.tvPriority = convertView.findViewById(R.id.tvPriority);
            holder.tvOverdue = convertView.findViewById(R.id.tvOverdue);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = tasks.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        holder.tvDate.setText(dateFormat.format(task.getTimeStart()));
        holder.tvTime.setText(timeFormat.format(task.getTimeStart()));
        holder.tvTitle.setText(task.getContent());

        // Hiển thị loại hoạt động bằng tiếng Việt
        String type = task.getType();
        if ("personal".equalsIgnoreCase(type)) {
            holder.tvType.setText("Hoạt động cá nhân");
        } else if ("extracurricular".equalsIgnoreCase(type)) {
            holder.tvType.setText("Hoạt động ngoại khóa");
        } else {
            holder.tvType.setText("Khác");
        }

        // Hiển thị mức độ ưu tiên bằng tiếng Việt
        switch (task.getTaskPriority()) {
            case LOW:
                holder.tvPriority.setText("Thấp");
                break;
            case MEDIUM:
                holder.tvPriority.setText("Trung bình");
                break;
            case HIGH:
                holder.tvPriority.setText("Cao");
                break;
        }

        long daysOverdue = (new Date().getTime() - task.getTimeStart().getTime()) / (1000 * 60 * 60 * 24);
        holder.tvOverdue.setText("Quá hạn " + daysOverdue + " ngày");

        return convertView;
    }


    private static class ViewHolder {
        TextView tvDate, tvTime, tvTitle, tvType, tvPriority, tvOverdue;
    }
}
