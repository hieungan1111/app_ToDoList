package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private final List<Date> dates;
    private final Context context;
    private int selectedPosition = -1;
    private final OnDateSelectedListener listener;
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    private final SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());
    private final Calendar today = Calendar.getInstance();
    private boolean isInitialLoad = true;

    public interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }

    public DateAdapter(Context context, List<Date> dates, OnDateSelectedListener listener) {
        this.context = context;
        this.dates = dates;
        this.listener = listener;

        // Khi khởi tạo lần đầu, tìm vị trí của ngày hiện tại
        if (isInitialLoad) {
            findAndSelectToday();
        }
    }

    private void findAndSelectToday() {
        // Tìm vị trí của ngày hiện tại
        for (int i = 0; i < dates.size(); i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dates.get(i));
            if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                selectedPosition = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Date date = dates.get(position);

        holder.tvDate.setText(dayFormat.format(date));

        // Format day of week in Vietnamese
        String dayOfWeek = dayOfWeekFormat.format(date);
        switch (dayOfWeek) {
            case "Mon":
                holder.tvDayOfWeek.setText("Th 2");
                break;
            case "Tue":
                holder.tvDayOfWeek.setText("Th 3");
                break;
            case "Wed":
                holder.tvDayOfWeek.setText("Th 4");
                break;
            case "Thu":
                holder.tvDayOfWeek.setText("Th 5");
                break;
            case "Fri":
                holder.tvDayOfWeek.setText("Th 6");
                break;
            case "Sat":
                holder.tvDayOfWeek.setText("Th 7");
                break;
            case "Sun":
                holder.tvDayOfWeek.setText("CN");
                break;
            default:
                holder.tvDayOfWeek.setText(dayOfWeek);
        }

        // Set selected state
        if (position == selectedPosition) {
            holder.dateContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            holder.tvDayOfWeek.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.dateContainer.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.tvDayOfWeek.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            listener.onDateSelected(dates.get(selectedPosition));
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void updateDates(List<Date> newDates, boolean resetSelection) {
        this.dates.clear();
        this.dates.addAll(newDates);

        if (resetSelection) {
            // Khi chuyển tháng, không chọn ngày nào
            selectedPosition = -1;
            isInitialLoad = false;
        } else if (isInitialLoad) {
            // Khi khởi tạo lần đầu, tìm và chọn ngày hiện tại
            findAndSelectToday();
            isInitialLoad = false;
        }

        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDayOfWeek;
        LinearLayout dateContainer;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            dateContainer = itemView.findViewById(R.id.dateContainer);
        }
    }
}