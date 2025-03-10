package com.example.f_food.Repository;

import android.content.Context;

import com.example.f_food.DAO.RestaurantDAO;
import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.Entity.Restaurant;
import com.example.f_food.R;

import java.util.Arrays;
import java.util.List;

public class RestaurantRepository {
    private RestaurantDAO restaurantDAO;

    public RestaurantRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        restaurantDAO = db.restaurantDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (restaurantDAO.getAllRestaurants().isEmpty()) {
            insertSampleData();
        }
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDAO.getAllRestaurants();
    }

    public Restaurant getRestaurantById(int id) {
        return restaurantDAO.getRestaurantById(id);
    }

    public void deleteById(int id) {
        restaurantDAO.deleteById(id);
    }

    public void deleteAll() {
        restaurantDAO.deleteAll();
    }

    public void insert(Restaurant restaurant) {
        restaurantDAO.insert(restaurant);
    }

    public void insertAll(List<Restaurant> listRestaurant) {
        restaurantDAO.insertAll(listRestaurant);
    }

    public void update(Restaurant restaurant) {
        restaurantDAO.update(restaurant);
    }

    private void insertSampleData() {
        List<Restaurant> sampleRestaurants = Arrays.asList(
                new Restaurant(1, "Pizza Hut", "Ha Noi, My Dinh", "012345", "Open", ""),
                new Restaurant(2, "KFC", "Ha Noi, Dong Da", "012345", "Open", ""),
                new Restaurant(3, "Sushi Bar", "Ha Noi , Ha Dong", "012345", "Open", ""),
                new Restaurant(4, "McDonald's", "Ha Noi, Ba Dinh", "012345", "Open", "")
        );

        for (Restaurant restaurant : sampleRestaurants) {
            restaurantDAO.insert(restaurant);
        }

    }
}
