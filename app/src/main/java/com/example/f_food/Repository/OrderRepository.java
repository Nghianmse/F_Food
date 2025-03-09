package com.example.f_food.Repository;

import android.content.Context;
import com.example.f_food.DAO.OrderDAO;
import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.DAO.RestaurantDAO;
import com.example.f_food.Entity.Order;
import com.example.f_food.Entity.Restaurant;
import java.util.Arrays;
import java.util.List;

public class OrderRepository {
    private OrderDAO orderDAO;
    private RestaurantDAO restaurantDAO;

    public OrderRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        orderDAO = db.orderDAO();
        restaurantDAO = db.restaurantDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (orderDAO.getAllOrders().isEmpty()) {
            insertSampleData();
        }
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public Order getOrderById(int id) {
        return orderDAO.getOrderById(id);
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.getOrdersByUserId(userId);
    }

    public String getRestaurantAddressByOrderId(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            Restaurant restaurant = restaurantDAO.getRestaurantById(order.getRestaurantId());
            return (restaurant != null) ? restaurant.getAddress() : "Unknown Address";
        }
        return "Unknown Address";
    }

    public void deleteById(int id) {
        orderDAO.deleteById(id);
    }

    public void deleteAll() {
        orderDAO.deleteAll();
    }

    public void insert(Order order) {
        orderDAO.insert(order);
    }

    public void insertAll(List<Order> listOrder) {
        orderDAO.insertAll(listOrder);
    }

    public void update(Order order) {
        orderDAO.update(order);
    }

    private void insertSampleData() {
        List<Order> sampleOrders = Arrays.asList(
                new Order(1, 1, 15.99, "Credit Card", "Pending", "2025-03-05 10:00:00", "2025-03-05 10:00:00"),
                new Order(2, 2, 22.50, "E-Wallet", "Preparing", "2025-03-05 10:10:00", "2025-03-05 10:15:00"),
                new Order(3, 3, 9.99, "COD", "Delivered", "2025-03-05 11:00:00", "2025-03-05 12:00:00"),
                new Order(4, 4, 30.75, "Credit Card", "Cancelled", "2025-03-05 12:30:00", "2025-03-05 12:45:00"),
                new Order(5, 2, 18.25, "E-Wallet", "Pending", "2025-03-05 13:00:00", "2025-03-05 13:05:00"),
                new Order(1, 2, 15.99, "Credit Card", "Pending", "2025-03-05 10:00:00", "2025-03-05 10:00:00"),
                new Order(2, 2, 22.50, "E-Wallet", "Preparing", "2025-03-05 10:10:00", "2025-03-05 10:15:00"),
                new Order(3, 3, 9.99, "COD", "Delivered", "2025-03-05 11:00:00", "2025-03-05 12:00:00"),
                new Order(4, 4, 30.75, "Credit Card", "Cancelled", "2025-03-05 12:30:00", "2025-03-05 12:45:00"),
                new Order(5, 2, 18.25, "E-Wallet", "Pending", "2025-03-05 13:00:00", "2025-03-05 13:05:00")
        );

        for (Order order : sampleOrders) {
            orderDAO.insert(order);
        }
    }
}
