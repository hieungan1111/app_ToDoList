package com.example.todolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Subject;

import java.util.List;

public class SubjectRecycleViewAdapter extends RecyclerView.Adapter<SubjectRecycleViewAdapter.SubjectRecycleViewHolder> {
    private List<Subject> subjectRecycleViewList;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnSubjectActionListener actionListener;

    // Interface để xử lý sự kiện xóa và chỉnh sửa
    public interface OnSubjectActionListener {
        void onDeleteSubject(int subjectId, int position);
        void onEditSubject(Subject subject);
    }

    public SubjectRecycleViewAdapter(List<Subject> subjectRecycleViewList, OnSubjectActionListener actionListener) {
        this.subjectRecycleViewList = subjectRecycleViewList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public SubjectRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_subject, parent, false);
        return new SubjectRecycleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return subjectRecycleViewList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectRecycleViewHolder holder, int position) {
        Subject currentItem = subjectRecycleViewList.get(position);
        holder.subjectName.setText(currentItem.getSubjectName());
        holder.subjectStartTime.setText(currentItem.getTimeStart());
        holder.subjectEndTime.setText(currentItem.getTimeEnd());

        // Bắt sự kiện click cho CardView
        holder.cardview_subject.setSelected(selectedPosition == position);
        holder.cardview_subject.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(position);
        });

        // Bắt sự kiện click cho nút Xóa
        holder.deleteButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteSubject(currentItem.getId(), position);
            }
        });

        // Bắt sự kiện click cho nút Chỉnh sửa (tùy chọn)
        holder.editButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditSubject(currentItem);
            }
        });
    }

    // Cập nhật danh sách sau khi xóa
    public void removeItem(int position) {
        subjectRecycleViewList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, subjectRecycleViewList.size());
    }

    public static class SubjectRecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectName;
        public TextView subjectStartTime;
        public TextView subjectEndTime;
        public CardView cardview_subject;
        public ImageView editButton;
        public ImageView deleteButton;

        public SubjectRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            subjectStartTime = itemView.findViewById(R.id.subject_start_time);
            subjectEndTime = itemView.findViewById(R.id.subject_end_time);
            cardview_subject = itemView.findViewById(R.id.cardview_subject);
            editButton = itemView.findViewById(R.id.subject_btn_edit);
            deleteButton = itemView.findViewById(R.id.subject_btn_delete);
        }
    }
}