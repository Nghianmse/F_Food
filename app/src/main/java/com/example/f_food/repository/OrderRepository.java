package com.example.f_food.repository;

import android.content.Context;

import com.example.f_food.dao.FoodWithOrder;
import com.example.f_food.dao.OrderDAO;
import com.example.f_food.dao.OrderDetailDAO;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.dao.RestaurantDAO;
import com.example.f_food.dao.FoodDAO;
import com.example.f_food.dao.ShipperWithOrder;
import com.example.f_food.entity.OrderDetail;
import com.example.f_food.entity.Food;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.Restaurant;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private OrderDAO orderDAO;
    private RestaurantDAO restaurantDAO;

    private OrderDetailDAO orderDetailDAO;
    private FoodDAO foodDAO;


    public OrderRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        orderDAO = db.orderDAO();
        restaurantDAO = db.restaurantDAO();
        orderDetailDAO = db.orderDetailDAO();
        foodDAO = db.foodDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (orderDAO.getAllOrders().isEmpty()) {
            insertSampleData();
        }
    }

    // Lấy danh sách món ăn từ OrderDetail theo orderId
    public List<Food> getFoodListByOrderId(int orderId) {
        List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);
        List<Food> foodList = new ArrayList<>();

        for (OrderDetail detail : orderDetails) {
            Food food = foodDAO.getFoodById(detail.getFoodId());
            if (food != null) {
                foodList.add(food);
            }
        }
        return foodList;
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

    public void updateOrderStatus(int orderId, String newStatus) {
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            order.setOrderStatus(newStatus);
            orderDAO.update(order);
        }
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

    public List<FoodWithOrder> getFoodNamesByOrderId(int orderId) {
        return orderDAO.getFoodNamesByOrderId(orderId);
    }

    public List<FoodWithOrder> getImageByOrderId(int orderId) {
        return orderDAO.getImageByOrderId(orderId);
    }
    public void updateOrderShipper(int orderId, int shipperId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            order.setShipperId(shipperId);
            orderDAO.update(order);
        }
    }
    public ShipperWithOrder getShipperWithOrder(int orderId) {
        return orderDAO.getShipperWithOrder(orderId);
    }

    private void insertSampleData() {
        List<Order> sampleOrders = Arrays.asList(
                new Order(1, 1, 15.99, "Credit Card", "Pending", "2025-03-05 10:00:00", "2025-03-05 10:00:00", 1),
                new Order(2, 2, 22.50, "E-Wallet", "Preparing", "2025-03-05 10:10:00", "2025-03-05 10:15:00",1),
                new Order(3, 3, 9.99, "COD", "Delivered", "2025-03-05 11:00:00", "2025-03-05 12:00:00", 2),
                new Order(4, 4, 30.75, "Credit Card", "Cancelled", "2025-03-05 12:30:00", "2025-03-05 12:45:00", 2),
                new Order(5, 2, 18.25, "E-Wallet", "Pending", "2025-03-05 13:00:00", "2025-03-05 13:05:00", 3)
        );

        for (Order order : sampleOrders) {
            orderDAO.insert(order);
        }
    }
}
