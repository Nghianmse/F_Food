package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.OrderAcceptedAdapter;
import com.example.f_food.entity.Order;
import com.example.f_food.repository.OrderRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class OrderAccepted extends AppCompatActivity {
    private RecyclerView rvOrderAccepted;
    private OrderAcceptedAdapter adapter;
    private OrderRepository orderRepository;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accepted);

        rvOrderAccepted = findViewById(R.id.rv_order_accepted);
        rvOrderAccepted.setLayoutManager(new LinearLayoutManager(this));

        orderRepository = new OrderRepository(this);
        List<Order> allOrders = orderRepository.getAllOrders();

        // 🔹 Lọc danh sách chỉ lấy các đơn hàng có trạng thái "Preparing"
        List<Order> preparingOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Preparing"))
                .collect(Collectors.toList());

        // 🔹 Khởi tạo adapter với danh sách đã lọc
        adapter = new OrderAcceptedAdapter(this, preparingOrders);
        rvOrderAccepted.setAdapter(adapter);

        // 🟢 Xử lý điều hướng với BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_delivery);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(OrderAccepted.this, PendingOrder.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_orders) {
                    startActivity(new Intent(OrderAccepted.this, DeliveryHistory.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_delivery) {
                    return true; // Giữ nguyên trang OrderAccepted
                }

                return false;
            }
        });
    }
}
