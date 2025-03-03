package com.example.f_food.Repository;

import android.content.Context;

import com.example.f_food.DAO.FoodDAO;
import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.Entity.Food;

import java.util.Arrays;
import java.util.List;

public class FoodRepository {
    private FoodDAO foodDAO;

    public FoodRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        foodDAO = db.foodDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (foodDAO.getAllFoods().isEmpty()) {
            insertSampleData();
        }
    }

    public List<Food> getAllFoods() {
        return foodDAO.getAllFoods();
    }

    public Food getFoodById(int id) {
        return foodDAO.getFoodById(id);
    }

    public List<Food> getFoodsByRestaurantId(int restaurantId) {
        return foodDAO.getFoodsByRestaurantId(restaurantId);
    }

    public void deleteById(int id) {
        foodDAO.deleteById(id);
    }

    public void deleteAll() {
        foodDAO.deleteAll();
    }

    public void insert(Food food) {
        foodDAO.insert(food);
    }

    public void insertAll(List<Food> listFood) {
        foodDAO.insertAll(listFood);
    }

    public void update(Food food) {
        foodDAO.update(food);
    }

    private void insertSampleData() {
        List<Food> sampleFoods = Arrays.asList(
                new Food(1, "Margherita Pizza", "Classic cheese pizza", 8.99, "Pizza", "", "Available"),
                new Food(1, "Pepperoni Pizza", "Pepperoni and cheese", 9.99, "Pizza", "", "Available"),
                new Food(2, "Fried Chicken", "Crispy fried chicken", 7.99, "Fast Food", "", "Available"),
                new Food(3, "Sushi Roll", "Fresh salmon sushi roll", 12.99, "Japanese", "", "Available")
        );

        for (Food food : sampleFoods) {
            foodDAO.insert(food);
        }
    }
}