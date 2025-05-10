package com.example.todolist.dao;

import com.example.todolist.model.User;
import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(int id);
}
