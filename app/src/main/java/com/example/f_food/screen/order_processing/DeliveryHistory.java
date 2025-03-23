package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.DeliveryHistoryAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DeliveryHistoryAdapter orderAdapter;
    private OrderRepository orderRepository;
    private BottomNavigationView bottomNavigationView;
    private EditText etSearch;

    private List<Order> fullOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_history);

        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.et_search);

        orderRepository = new OrderRepository(this);

        int shipperId = RestaurantRoomDatabase.getInstance(this).shipperDAO().getShipperByUserId(getLoggedInUserId()).getShipperId();

        List<Order> allOrders = orderRepository.getOrdersByShipperId(shipperId);

        // Lưu danh sách gốc để search
        fullOrderList = allOrders;

        // Lọc đơn hàng đã giao hoặc bị huỷ
        List<Order> filteredOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Delivered") ||
                        order.getOrderStatus().equalsIgnoreCase("Cancelled"))
                .collect(Collectors.toList());

        // Lấy thông tin người dùng từ Intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        String userEmail = intent.getStringExtra("email");
        String userPassword = intent.getStringExtra("password");
        Log.d("DeliveryHistory", "Tên: " + userEmail + ", Email: " + userEmail);

        // Khởi tạo Adapter
        orderAdapter = new DeliveryHistoryAdapter(this, filteredOrders, userEmail, userPassword, userName, userPhone);
        recyclerView.setAdapter(orderAdapter);

        // Search realtime
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOrders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Bottom Navigation xử lý điều hướng
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(DeliveryHistory.this, PendingOrder.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_orders) {
                    Intent intent = new Intent(DeliveryHistory.this, DeliveryHistory.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_delivery) {
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

    // Hàm xử lý tìm kiếm đơn hàng
    private void filterOrders(String query) {
        List<Order> filteredList = fullOrderList.stream()
                .filter(order -> {
                    String lowerQuery = query.toLowerCase();

                    boolean isDeliveredOrCancelled = order.getOrderStatus().equalsIgnoreCase("Delivered")
                            || order.getOrderStatus().equalsIgnoreCase("Cancelled");

                    if (!isDeliveredOrCancelled) return false;

                    String orderId = String.valueOf(order.getOrderId()).toLowerCase();
                    String status = order.getOrderStatus().toLowerCase();

                    Restaurant restaurant = new RestaurantRepository(this).getRestaurantById(order.getRestaurantId());
                    String restaurantAddress = (restaurant != null) ? restaurant.getAddress().toLowerCase() : "";

                    return orderId.contains(lowerQuery)
                            || status.contains(lowerQuery)
                            || restaurantAddress.contains(lowerQuery);
                })
                .collect(Collectors.toList());
        orderAdapter.setOrders(filteredList);
    }

    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1);
    }

}
