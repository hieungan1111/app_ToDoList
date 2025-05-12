package com.example.todolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    public SubjectRecycleViewAdapter(List<Subject> subjectRecycleViewList) {
        this.subjectRecycleViewList = subjectRecycleViewList;
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
        holder.subjectStartTime.setText(currentItem.getTimeStart().toString());
        holder.subjectEndTime.setText(currentItem.getTimeEnd().toString());

        // Bắt sự kiện click
        holder.cardview_subject.setSelected(selectedPosition == position);
        holder.cardview_subject.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(position);
        });

    }

    public static class SubjectRecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectName;
        public TextView subjectStartTime;
        public TextView subjectEndTime;
        public CardView cardview_subject;

        public SubjectRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            subjectStartTime = itemView.findViewById(R.id.subject_start_time);
            subjectEndTime = itemView.findViewById(R.id.subject_end_time);
            cardview_subject = itemView.findViewById(R.id.cardview_subject);
        }
    }


}
