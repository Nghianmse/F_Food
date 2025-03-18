package com.example.f_food.repository;
import android.content.Context;
import com.example.f_food.dao.OrderDetailDAO;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.OrderDetail;
import java.util.Arrays;
import java.util.List;

public class OrderDetailRepository {
    private OrderDetailDAO orderDetailDAO;

    public OrderDetailRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        orderDetailDAO = db.orderDetailDAO();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        if (orderDetailDAO.getAllOrderDetails().isEmpty()) {
            insertSampleData();
        }
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailDAO.getAllOrderDetails();
    }

    public OrderDetail getOrderDetailById(int id) {
        return orderDetailDAO.getOrderDetailById(id);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailDAO.getOrderDetailsByOrderId(orderId);
    }

    public void deleteById(int id) {
        orderDetailDAO.deleteById(id);
    }

    public void deleteAll() {
        orderDetailDAO.deleteAll();
    }

    public void insert(OrderDetail orderDetails) {
        orderDetailDAO.insert(orderDetails);
    }

    public void insertAll(List<OrderDetail> listOrderDetails) {
        orderDetailDAO.insertAll(listOrderDetails);
    }

    public void update(OrderDetail orderDetails) {
        orderDetailDAO.update(orderDetails);
    }

    private void insertSampleData() {
        List<OrderDetail> sampleOrderDetails = Arrays.asList(
                new OrderDetail(1, 1, 2, 9.99),
                new OrderDetail(1, 2, 1, 12.50),
                new OrderDetail(2, 3, 3, 7.99),
                new OrderDetail(3, 4, 2, 15.99),
                new OrderDetail(4, 5, 1, 18.25)
        );

        for (OrderDetail orderDetails : sampleOrderDetails) {
            orderDetailDAO.insert(orderDetails);
        }
    }
}