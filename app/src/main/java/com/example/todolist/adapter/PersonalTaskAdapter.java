package com.example.todolist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.widget.BaseAdapter;

import com.example.todolist.R;
import com.example.todolist.dao.impl.SQLiteTaskDAO;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PersonalTaskAdapter extends BaseAdapter {

    private final Context context;
    private final List<Task> tasks;
    private final LayoutInflater inflater;

    public PersonalTaskAdapter(Context context, List<Task> tasks) {
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
            convertView = inflater.inflate(R.layout.item_personal_task, parent, false);
            holder = new ViewHolder();
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.tvContent = convertView.findViewById(R.id.tvContent);
            holder.checkBoxDone = convertView.findViewById(R.id.checkBoxDone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = tasks.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        holder.tvTime.setText(sdf.format(task.getTimeStart()));
        holder.tvContent.setText(task.getContent());
        holder.checkBoxDone.setChecked(task.isDone());

        // ✨ Xử lý giao diện khi task đã hoàn thành
        if (task.isDone()) {
            holder.tvContent.setAlpha(0.5f);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvContent.setAlpha(1.0f);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Cập nhật DB khi người dùng nhấn vào checkbox
        holder.checkBoxDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            new SQLiteTaskDAO(context).updateTask(task); // update DB

            // Cập nhật UI
            notifyDataSetChanged();
        });

        return convertView;
    }



    private static class ViewHolder {
        TextView tvTime, tvContent;
        CheckBox checkBoxDone;
    }
}
