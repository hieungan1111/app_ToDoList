package com.example.todolist.dao;

import com.example.todolist.model.ScheduleDeadlineNotification;
import java.util.List;

public interface ScheduleDeadlineNotificationDAO {
    void addNotification(ScheduleDeadlineNotification notification);
    List<ScheduleDeadlineNotification> getAllByUserId(int userId);
    void updateSentStatus(int deadlineId, boolean sent);
    void deleteNotification(int deadlineId);
}
