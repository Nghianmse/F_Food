package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.PendingOrderAdapter;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.entity.User;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.repository.UserRepository;
import com.example.f_food.screen.authentication_authorization.ShipperLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class PendingOrder extends AppCompatActivity {
    private RecyclerView rvPendingOrders;
    private PendingOrderAdapter adapter;
    private EditText etSearch;
    private List<Order> fullOrderList; // Dùng để search
    private OrderRepository orderRepository;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        rvPendingOrders = findViewById(R.id.rvPendingOrders);
        rvPendingOrders.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.et_search);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvPhone = findViewById(R.id.tvPhone);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        String userEmail = intent.getStringExtra("email");
        String userPassword = intent.getStringExtra("password");

        Log.d("DEBUG_INTENT", "Email: " + userEmail + ", Password: " + userPassword);

        UserRepository userRepository = new UserRepository(this);
        List<User> users = userRepository.getAllUsers();

        boolean isValidUser = false;
        for (User user : users) {
            if (user.getEmail().equals(userEmail) && user.getPassword().equals(userPassword)) {
                isValidUser = true;
                break;
            }
        }

        if (!isValidUser) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            Intent backToLogin = new Intent(this, ShipperLogin.class);
            startActivity(backToLogin);
            finish();
            return;
        }

        tvName.setText("Họ tên: " + userName);
        tvPhone.setText("Số điện thoại: " + userPhone);

        orderRepository = new OrderRepository(this);
        List<Order> allOrders = orderRepository.getAllOrders();
        fullOrderList = allOrders;

        List<Order> pendingOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Preparing"))
                .collect(Collectors.toList());

        if (pendingOrders.isEmpty()) {
            Toast.makeText(this, "Không có đơn hàng nào đang chờ xử lý!", Toast.LENGTH_SHORT).show();
        }

        adapter = new PendingOrderAdapter(this, pendingOrders, order ->
                Toast.makeText(PendingOrder.this, "Order ID: " + order.getOrderId(), Toast.LENGTH_SHORT).show(),
                userEmail, userPassword, userName, userPhone
        );

        rvPendingOrders.setAdapter(adapter);

        // Search listener
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

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(PendingOrder.this, PendingOrder.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_orders) {
                    Intent intent = new Intent(PendingOrder.this, DeliveryHistory.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_delivery) {
                    Intent intent = new Intent(PendingOrder.this, OrderAccepted.class);
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

    private void filterOrders(String query) {
        List<Order> filteredList = fullOrderList.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Pending"))
                .filter(order -> {
                    String lowerQuery = query.toLowerCase();
                    Restaurant restaurant = new RestaurantRepository(this).getRestaurantById(order.getRestaurantId());
                    String restaurantAddress = (restaurant != null) ? restaurant.getAddress().toLowerCase() : "";
                    String orderId = String.valueOf(order.getOrderId()).toLowerCase();
                    return restaurantAddress.contains(lowerQuery) || orderId.contains(lowerQuery);
                })
                .collect(Collectors.toList());

        adapter.updateList(filteredList);
    }
}
