package com.example.f_food.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.f_food.Entity.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM Categories")
    List<Category> getAllCategories();

    @Query("SELECT * FROM Categories WHERE category_id = :id")
    Category getCategoryById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM Categories WHERE category_id = :id")
    void deleteById(int id);

    @Query("DELETE FROM Categories")
    void deleteAll();
}
