package com.example.f_food.repository;

import android.content.Context;

import com.example.f_food.dao.FoodDAO;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Food;

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

    public List<Food> getFoodsByName(String name) {
        return foodDAO.getFoodsByName(name);
    }

    public List<Food> getFoodsByCategoryId(int categoryId) {
        return foodDAO.getFoodsByCategoryId(categoryId);
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
                new Food(1, "Margherita Pizza", "Classic cheese pizza", 8.99, 1, "https://media.istockphoto.com/id/184946701/photo/pizza.jpg?s=612x612&w=0&k=20&c=97rc0VIi-s3mn4xe4xDy9S-XJ_Ohbn92XaEMaiID_eY=", "Available"),
                new Food(1, "Pepperoni Pizza", "Pepperoni and cheese", 9.99, 2, "https://www.allrecipes.com/thmb/rarUQLSXEvsTwnbu-0Zm5NDNtBE=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Pepperoni-Pizza-Cheese-Drip-Chips-2000-15cec53ba155400aa33587c1561f01d5.jpg", "Available"),
                new Food(1, "Fried Chicken", "Crispy fried chicken", 7.99, 3, "https://cravinghomecooked.com/wp-content/uploads/2019/08/crispy-fried-chicken-1.jpg", "Available"),
                new Food(3, "Sushi Roll", "Fresh salmon sushi roll", 12.99, 4, "https://norecipes.com/wp-content/uploads/2022/02/shrimp-tempura-roll-004.jpg", "Available")
        );

        for (Food food : sampleFoods) {
            foodDAO.insert(food);
        }
    }
}