package com.example.f_food.Screen.order_processing;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.stream.Collectors;

import com.example.f_food.Adapter.DeliveryHistoryAdapter;
import com.example.f_food.Entity.Order;
import com.example.f_food.R;
import com.example.f_food.Repository.OrderRepository;

public class DeliveryHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DeliveryHistoryAdapter orderAdapter;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_history);

        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderRepository = new OrderRepository(this);
        List<Order> allOrders = orderRepository.getAllOrders(); // Lấy toàn bộ danh sách đơn hàng từ Database

        // 🔹 Lọc các đơn hàng chỉ có trạng thái "Delivered" hoặc "Cancelled"
        List<Order> filteredOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Delivered") ||
                        order.getOrderStatus().equalsIgnoreCase("Cancelled"))
                .collect(Collectors.toList());

        // Gán danh sách đã lọc vào Adapter
        orderAdapter = new DeliveryHistoryAdapter(this, filteredOrders);
        recyclerView.setAdapter(orderAdapter);
    }
}
