package com.example.f_food.Screen.order_processing;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Adapter.PendingOrderAdapter;
import com.example.f_food.Entity.Order;
import com.example.f_food.R;
import com.example.f_food.Repository.OrderRepository;

import java.util.List;

public class PendingOrder extends AppCompatActivity {
    private RecyclerView rvPendingOrders;
    private PendingOrderAdapter adapter;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        rvPendingOrders = findViewById(R.id.rvPendingOrders);
        rvPendingOrders.setLayoutManager(new LinearLayoutManager(this));

        orderRepository = new OrderRepository(this);
        List<Order> orders = orderRepository.getAllOrders(); // Lấy danh sách đơn hàng

        // Khởi tạo adapter với listener
        adapter = new PendingOrderAdapter(this, orders, order ->
                Toast.makeText(PendingOrder.this, "Order ID: " + order.getOrderId(), Toast.LENGTH_SHORT).show()
        );

        rvPendingOrders.setAdapter(adapter);
    }
}
