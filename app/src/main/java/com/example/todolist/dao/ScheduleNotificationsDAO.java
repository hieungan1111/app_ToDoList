package com.example.todolist.dao;

import com.example.todolist.model.ScheduleNotifications;
import java.util.List;

public interface ScheduleNotificationsDAO {
    void addNotification(ScheduleNotifications notification);
    List<ScheduleNotifications> getAllByUserId(int userId);
    void updateSentStatus(int taskId, boolean sent);
    void deleteNotificationByTaskId(int taskId);
}
