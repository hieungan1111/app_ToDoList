package com.example.todolist.dao;

import com.example.todolist.model.Categories;
import java.util.List;

public interface CategoriesDAO {
    void addCategory(Categories category);
    List<Categories> getAllCategoriesByUserId(int userId);
    void deleteCategoryByName(String name, int userId);
    void deleteAllCategoriesOfUser(int userId);
}
