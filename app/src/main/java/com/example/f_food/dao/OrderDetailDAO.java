package com.example.f_food.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.f_food.entity.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDAO {

    @Query("SELECT * FROM OrderDetails")
    List<OrderDetail> getAllOrderDetails();

    @Query("SELECT * FROM OrderDetails WHERE order_detail_id = :id")
    OrderDetail getOrderDetailById(int id);

    @Query("SELECT * FROM OrderDetails WHERE order_id = :orderId")
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderDetail orderDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<OrderDetail> orderDetailsList);

    @Update
    void update(OrderDetail orderDetails);

    @Delete
    void delete(OrderDetail orderDetails);

    @Query("DELETE FROM OrderDetails WHERE order_detail_id = :id")
    void deleteById(int id);

    @Query("DELETE FROM OrderDetails")
    void deleteAll();
}
