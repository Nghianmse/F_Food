package com.example.f_food.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.f_food.Entity.Food;

import java.util.List;

@Dao
public interface FoodDAO {

    @Query("SELECT * FROM Foods")
    List<Food> getAllFoods();

    @Query("SELECT * FROM Foods WHERE food_id = :id")
    Food getFoodById(int id);

    @Query("SELECT * FROM Foods WHERE restaurant_id = :restaurantId")
    List<Food> getFoodsByRestaurantId(int restaurantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Food food);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Food> foods);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("DELETE FROM Foods WHERE food_id = :id")
    void deleteById(int id);

    @Query("DELETE FROM Foods")
    void deleteAll();
}