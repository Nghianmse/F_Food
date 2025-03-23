package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.DeliveryHistoryAdapter;
import com.example.f_food.entity.Order;
import com.example.f_food.repository.OrderRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DeliveryHistoryAdapter orderAdapter;
    private OrderRepository orderRepository;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_history);

        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderRepository = new OrderRepository(this);
        List<Order> allOrders = orderRepository.getAllOrders();

        List<Order> filteredOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Delivered") ||
                        order.getOrderStatus().equalsIgnoreCase("Cancelled"))
                .collect(Collectors.toList());

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        String userEmail = intent.getStringExtra("email");
        String userPassword = intent.getStringExtra("password");
        Log.d("DeliveryHistory", "Tên: " + userEmail + ", Email: " + userEmail);

        orderAdapter = new DeliveryHistoryAdapter(this, filteredOrders);
        recyclerView.setAdapter(orderAdapter);

        // ✅ Làm đậm icon `nav_orders` khi vào màn hình DeliveryHistory
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

        // ✅ Xử lý khi bấm vào `nav_home` để quay lại `PendingOrder`
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) { // 🔹 Quay lại PendingOrder
                    Intent intent = new Intent(DeliveryHistory.this, PendingOrder.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_orders) { // 🔹 Giữ nguyên trang
                    Intent intent = new Intent( DeliveryHistory.this, DeliveryHistory.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_delivery) { // 🔹 Chuyển sang DeliveryStatusUpdate
                    Intent intent = new Intent(DeliveryHistory.this, OrderAccepted.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }

                return false;
            }
        });

    }
}
