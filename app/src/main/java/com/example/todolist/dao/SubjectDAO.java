package com.example.todolist.dao;

import com.example.todolist.model.Subject;
import java.util.List;

public interface SubjectDAO {
    void addSubject(Subject subject);
    Subject getSubjectById(int id);
    List<Subject> getAllSubjectsByUserId(int userId);
    void updateSubject(Subject subject);
    void deleteSubject(int id);
    List<Subject> getSubjectsByWeekday( int   userId, String weekday, String rangeStart);
}
