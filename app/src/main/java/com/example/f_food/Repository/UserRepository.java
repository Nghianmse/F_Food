package com.example.f_food.Repository;

import android.content.Context;

import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.DAO.UserDAO;

import com.example.f_food.Entity.User;

import java.util.Arrays;
import java.util.List;

public class UserRepository {
    private UserDAO userDAO;
    private RestaurantRoomDatabase restaurantRoomDatabase;

    public UserRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        userDAO = db.userDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (userDAO.getAllUsers().isEmpty()) {
            insertSampleData();
        }
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public void deleteById(int id) {
        userDAO.deleteById(id);
    }

    public void deleteAll() {
        userDAO.deleteAll();
    }

    public void insert(User user) {
        userDAO.insert(user);
    }

    public void insertAll(List<User> listUser) {
        userDAO.insertAll(listUser);
    }

    public void update(User user) {
        userDAO.update(user);
    }

    private void insertSampleData() {
        List<User> sampleUsers = Arrays.asList(
                new User(1, "John Doe", "a@gmail.com", "123", "0123456789", "Customer", "2023-01-01 10:00:00", "2023-01-01 10:00:00"),
                new User(2, "Alice Smith", "alice@example.com", "password456", "0987654321", "Restaurant", "2023-01-02 11:00:00", "2023-01-02 11:00:00"),
                new User(3, "Bob Johnson", "bob@example.com", "password789", "0112233445", "Shipper", "2023-01-03 12:00:00", "2023-01-03 12:00:00"),
                new User(4, "Admin User", "admin@example.com", "adminpass", "0223344556", "Admin", "2023-01-04 13:00:00", "2023-01-04 13:00:00")
        );

        for (User user : sampleUsers) {
            userDAO.insert(user);
        }
    }
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public User getUserByPhone(String phone) {
        return userDAO.getUserByPhone(phone);
    }
}