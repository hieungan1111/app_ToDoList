package com.example.todolist.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HandleDate {
    static public String formatDate(int day, int month, int year) {
        // month + 1 vì tháng trong Calendar bắt đầu từ 0
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
    }
    static public int compareDates(String dateStr1, String dateStr2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date1 = sdf.parse(dateStr1);
            Date date2 = sdf.parse(dateStr2);
            return date1.compareTo(date2); // Trả về:
            // -1 nếu date1 < date2
            //  0 nếu bằng nhau
            //  1 nếu date1 > date2
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Hoặc xử lý lỗi khác
        }
    }

    static public String formatTime(int hour, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    static public int compareTimes(String timeStr1, String timeStr2) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date time1 = sdf.parse(timeStr1);
            Date time2 = sdf.parse(timeStr2);
            return time1.compareTo(time2); // -1 nếu time1 < time2, 0 nếu bằng, 1 nếu time1 > time2
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // hoặc xử lý lỗi riêng
        }
    }



}
