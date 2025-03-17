package com.example.f_food.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.f_food.Entity.Order;

import java.util.List;

@Dao
public interface OrderDAO {

    @Query("SELECT * FROM Orders")
    List<Order> getAllOrders();

    @Query("SELECT * FROM Orders WHERE order_id = :id")
    Order getOrderById(int id);

    @Query("SELECT * FROM Orders WHERE user_id = :userId")
    List<Order> getOrdersByUserId(int userId);
    @Query("UPDATE Orders SET order_status = :newStatus WHERE order_id = :orderId")
    void updateOrderStatus(int orderId, String newStatus);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Order order);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Order> orders);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("DELETE FROM Orders WHERE order_id = :id")
    void deleteById(int id);

    @Query("DELETE FROM Orders")
    void deleteAll();
}
