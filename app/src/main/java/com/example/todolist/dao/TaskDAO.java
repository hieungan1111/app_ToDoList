package com.example.todolist.dao;

import com.example.todolist.model.Task;
import java.util.List;

public interface TaskDAO {
    void addTask(Task task);
    Task getTaskById(int id);
    List<Task> getAllTasksByUserId(int userId);
    void updateTask(Task task);
    void deleteTask(int id);
    void markTaskDone(int id, boolean done);
}
