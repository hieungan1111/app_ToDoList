package com.example.todolist.dao;

import com.example.todolist.model.User;
import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User findUser(String email); //kiểm tra email đã xác minh chưa
    User findCode(String code); //kiểm tra code có trùng không
    User checkVerify(String email, String code);
    void updateIsVerified(int id);
    boolean updatePassword(int id,String password);
    void deleteUserByEmail(String email);
    User getUserById(int id);
    List<User> getAllUsers();
    boolean updateUser(User user);
    void deleteUser(int id);
}
