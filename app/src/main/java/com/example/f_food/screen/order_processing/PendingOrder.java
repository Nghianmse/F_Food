package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.PendingOrderAdapter;
import com.example.f_food.entity.Order;
import com.example.f_food.repository.OrderRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class PendingOrder extends AppCompatActivity {
    private RecyclerView rvPendingOrders;
    private PendingOrderAdapter adapter;
    private OrderRepository orderRepository;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        rvPendingOrders = findViewById(R.id.rvPendingOrders);
        rvPendingOrders.setLayoutManager(new LinearLayoutManager(this));

        orderRepository = new OrderRepository(this);
        List<Order> allOrders = orderRepository.getAllOrders(); // Lấy danh sách đơn hàng

        List<Order> orders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Pending"))
                .collect(Collectors.toList());

        // Kiểm tra nếu không có đơn hàng nào đang "Pending"
        if (orders.isEmpty()) {
            Toast.makeText(this, "Không có đơn hàng nào đang chờ xử lý!", Toast.LENGTH_SHORT).show();
        }

        adapter = new PendingOrderAdapter(this, orders, order ->
                Toast.makeText(PendingOrder.this, "Order ID: " + order.getOrderId(), Toast.LENGTH_SHORT).show()
        );

        rvPendingOrders.setAdapter(adapter);

        // 🚀 Xử lý sự kiện chuyển màn hình khi bấm vào BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) { // 🔹 Giữ nguyên trang
                    return true;
                } else if (itemId == R.id.nav_orders) { // 🔹 Chuyển sang DeliveryHistory
                    Intent intent = new Intent(PendingOrder.this, DeliveryHistory.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_delivery) { // 🔹 Chuyển sang DeliveryStatusUpdate
                    Intent intent = new Intent(PendingOrder.this, OrderAccepted.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }

                return false;
            }
        });

    }
}
