package com.example.todolist.dao;

import com.example.todolist.model.Deadline;
import java.util.List;

public interface DeadlineDAO {
    void addDeadline(Deadline deadline);
    Deadline getDeadlineById(int id);
    List<Deadline> getAllDeadlinesByUserId(int userId);
    void updateDeadline(Deadline deadline);
    void deleteDeadline(int id);
    void markDone(int id, boolean done);
}
