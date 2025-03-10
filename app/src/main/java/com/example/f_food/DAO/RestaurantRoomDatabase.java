package com.example.f_food.DAO;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.f_food.Entity.Food;
import com.example.f_food.Entity.Order;
import com.example.f_food.Entity.OrderDetail;
import com.example.f_food.Entity.Policy;
import com.example.f_food.Entity.Restaurant;
import com.example.f_food.Entity.Review;
import com.example.f_food.Entity.User;
import com.example.f_food.Entity.OrderDetail;

@Database(entities = {Restaurant.class, Food.class, User.class, Policy.class, Order.class, OrderDetail.class}, version = 9, exportSchema = false)
public abstract class RestaurantRoomDatabase extends RoomDatabase {

    public abstract RestaurantDAO restaurantDAO();

    public abstract FoodDAO foodDAO();
    public  abstract  PolicyDAO policyDAO();

    public abstract UserDAO userDAO();

    public abstract  OrderDAO orderDAO();

    public abstract  OrderDetailDAO orderDetailDAO();

//    public abstract  ReviewDAO reviewDAO();

    private static volatile RestaurantRoomDatabase INSTANCE;

    public static RestaurantRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RestaurantRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RestaurantRoomDatabase.class, "restaurant_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}