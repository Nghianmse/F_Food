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
                new Restaurant(1, "Pizza Hut", "Ha Noi", "012345", "Open", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8cmVzdGF1cmFudHxlbnwwfHwwfHx8MA%3D%3D"),
                new Restaurant(2, "KFC", "Ha Noi", "012345", "Open", "https://t3.ftcdn.net/jpg/03/24/73/92/360_F_324739203_keeq8udvv0P2h1MLYJ0GLSlTBagoXS48.jpg"),
                new Restaurant(3, "Sushi Bar", "Ha Noi", "012345", "Open", "https://images.wsj.net/im-65599456?size=1.5"),
                new Restaurant(4, "McDonald's", "Ha Noi", "012345", "Open", "https://images.squarespace-cdn.com/content/v1/61d2ccabbc553c1fec7c16e9/b43cd3de-f28e-4763-93cb-44b066748bca/DSC_8711.jpg")
        );

        for (Restaurant restaurant : sampleRestaurants) {
            restaurantDAO.insert(restaurant);
        }

    }
}
