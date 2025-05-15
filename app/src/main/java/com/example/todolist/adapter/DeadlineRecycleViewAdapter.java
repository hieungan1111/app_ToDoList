package com.example.todolist.adapter;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Deadline;
import android.os.Handler;

import java.util.List;

public class DeadlineRecycleViewAdapter extends RecyclerView.Adapter<DeadlineRecycleViewAdapter.DeadlineViewHolder> {

    private List<Deadline> deadlineList;
    private OnDeadlineActionListener listener;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface OnDeadlineActionListener {
        void onEditDeadline(Deadline deadline, int position);
        void onDeleteDeadline(int deadlineId, int position);
        void onStatusChanged(Deadline deadline, boolean isCompleted, int position);
    }

    public DeadlineRecycleViewAdapter(List<Deadline> deadlineList, OnDeadlineActionListener listener) {
        this.deadlineList = deadlineList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_deadline, parent, false);
        return new DeadlineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeadlineViewHolder holder, int position) {
        Deadline deadline = deadlineList.get(position);

        // Hiển thị thông tin deadline
        holder.deadlineTime.setText(deadline.getTimeEnd());
        holder.deadlineDate.setText(deadline.getDay());
        holder.deadlineName.setText(deadline.getDeadlineName());
        holder.deadlineSubjectName.setText(deadline.getSubject());

        // Thiết lập trạng thái RadioButton
        holder.radioCompleted.setChecked(deadline.isDone());
        holder.radioNotCompleted.setChecked(!deadline.isDone());

        // Xử lý sự kiện thay đổi trạng thái
        holder.statusGroup.setOnCheckedChangeListener(null); // Ngăn lặp sự kiện
        holder.statusGroup.setOnCheckedChangeListener((group, checkedId) -> {
            boolean isCompleted = checkedId == R.id.deadline_radio_completed;
            deadline.setDone(isCompleted);
            if (listener != null) {
                listener.onStatusChanged(deadline, isCompleted, position);
            }
        });

        // Xử lý nút chỉnh sửa
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditDeadline(deadline, position);
            }
        });

        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteDeadline(deadline.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deadlineList != null ? deadlineList.size() : 0;
    }

    public void updateItem(int position, Deadline deadline) {
        deadlineList.set(position, deadline);
        handler.post(() -> notifyItemChanged(position));
//        notifyItemChanged(position);
    }


    public void removeItem(int position) {
        deadlineList.remove(position);
//        notifyItemRemoved(position);
        handler.post(() -> notifyItemRemoved(position));
    }

    static class DeadlineViewHolder extends RecyclerView.ViewHolder {
        TextView deadlineTime ,        deadlineDate, deadlineName, deadlineSubjectName;
        RadioGroup statusGroup;
        RadioButton radioCompleted, radioNotCompleted;
        ImageView btnEdit, btnDelete;

        public DeadlineViewHolder(@NonNull View itemView) {
            super(itemView);
            deadlineTime = itemView.findViewById(R.id.deadline_time);
            deadlineDate = itemView.findViewById(R.id.deadline_date);
            deadlineName = itemView.findViewById(R.id.deadline_name);
            deadlineSubjectName = itemView.findViewById(R.id.deadline_subject_name);
            statusGroup = itemView.findViewById(R.id.deadline_status_group);
            radioCompleted = itemView.findViewById(R.id.deadline_radio_completed);
            radioNotCompleted = itemView.findViewById(R.id.deadline_radio_not_completed);
            btnEdit = itemView.findViewById(R.id.deadline_btn_edit);
            btnDelete = itemView.findViewById(R.id.deadline_btn_delete);
        }
    }
}