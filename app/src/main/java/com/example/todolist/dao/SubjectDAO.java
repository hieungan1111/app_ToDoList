package com.example.todolist.dao;

import com.example.todolist.model.Subject;
import java.util.List;

public interface SubjectDAO {
    void addSubject(Subject subject);
    Subject getSubjectById(int id);
    List<Subject> getAllSubjectsByUserId(int userId);
    void updateSubject(Subject subject);
    void deleteSubject(int id);
    List<Subject> getSubjectsByWeekday( String weekday, String rangeStart);
}
