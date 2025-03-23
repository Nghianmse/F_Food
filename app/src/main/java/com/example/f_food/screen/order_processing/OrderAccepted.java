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

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        String userEmail = intent.getStringExtra("email");
        String userPassword = intent.getStringExtra("password");
        Log.d("DeliveryHistory", "Tên: " + userEmail + ", Email: " + userEmail);

        rvOrderAccepted = findViewById(R.id.rv_order_accepted);
        rvOrderAccepted.setLayoutManager(new LinearLayoutManager(this));

        orderRepository = new OrderRepository(this);
        List<Order> allOrders = orderRepository.getAllOrders();

        // 🔹 Lọc danh sách chỉ lấy các đơn hàng có trạng thái "Preparing"
        List<Order> preparingOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Preparing"))
                .collect(Collectors.toList());

        // 🔹 Khởi tạo adapter với danh sách đã lọc
        adapter = new OrderAcceptedAdapter(this, preparingOrders, userEmail, userPassword, userName, userPhone);
        rvOrderAccepted.setAdapter(adapter);

        // 🟢 Xử lý điều hướng với BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_delivery);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) { // 🔹 Chuyen sang Pending
                    Intent intent = new Intent(OrderAccepted.this, PendingOrder.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_orders) { // 🔹 Chuyển sang DeliveryHistory
                    Intent intent = new Intent(OrderAccepted.this, DeliveryHistory.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_delivery) { // 🔹 Giu nguyen trang
                    Intent intent = new Intent(OrderAccepted.this, OrderAccepted.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });
    }
}
