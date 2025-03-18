package com.example.f_food.repository;

import android.content.Context;

import com.example.f_food.dao.CategoryDAO;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Category;

import java.util.Arrays;
import java.util.List;

public class CategoryRepository {
    private CategoryDAO categoryDAO;

    public CategoryRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        categoryDAO = db.categoryDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (categoryDAO.getAllCategories().isEmpty()) {
            insertSampleData();
        }
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public Category getCategoryById(int id) {
        return categoryDAO.getCategoryById(id);
    }
    public Category getCategoryByName(String name) {
        return categoryDAO.getCategoryByName(name);
    }


    public void deleteById(int id) {
        categoryDAO.deleteById(id);
    }

    public void deleteAll() {
        categoryDAO.deleteAll();
    }

    public void insert(Category category) {
        categoryDAO.insert(category);
    }

    public void insertAll(List<Category> categories) {
        categoryDAO.insertAll(categories);
    }

    public void update(Category category) {
        categoryDAO.update(category);
    }

    private void insertSampleData() {
        List<Category> sampleCategories = Arrays.asList(
                new Category("Pizza"),
                new Category("Fast Food"),
                new Category("Asian Cuisine"),
                new Category("Desserts")
        );

        for (Category category : sampleCategories) {
            categoryDAO.insert(category);
        }
    }
}
