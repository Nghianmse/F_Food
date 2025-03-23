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
import com.example.f_food.adapter.OrderAcceptedAdapter;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.repository.ShipperRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class OrderAccepted extends AppCompatActivity {
    private RecyclerView rvOrderAccepted;
    private OrderAcceptedAdapter adapter;
    private OrderRepository orderRepository;
    private BottomNavigationView bottomNavigationView;
    private EditText etSearch;
    private List<Order> fullOrderList;
    private int shipperId = 0;
    private String userName, userPhone, userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accepted);

        // Lấy thông tin người dùng
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userPhone = intent.getStringExtra("userPhone");
        userEmail = intent.getStringExtra("email");
        userPassword = intent.getStringExtra("password");

        Log.d("OrderAccepted", "Tên: " + userEmail + ", Email: " + userEmail);
        ShipperRepository shipperRepository = new ShipperRepository(this);
        shipperId = shipperRepository.getShipperByUserId(getLoggedInUserId()).getShipperId();
        // Gán view
        rvOrderAccepted = findViewById(R.id.rv_order_accepted);
        rvOrderAccepted.setLayoutManager(new LinearLayoutManager(this));
        etSearch = findViewById(R.id.et_search);

        // Lấy danh sách đơn hàng và lọc
        orderRepository = new OrderRepository(this);
        List<Order> allOrders = orderRepository.getOrdersByShipperId(shipperId);
        fullOrderList = allOrders;

        List<Order> preparingOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Delivering"))
                .collect(Collectors.toList());

        // Adapter
        adapter = new OrderAcceptedAdapter(this, preparingOrders, userEmail, userPassword, userName, userPhone);
        rvOrderAccepted.setAdapter(adapter);

        // Search realtime
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOrders(s.toString());
            }
        });

        // Bottom nav
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_delivery);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent1;
            if (itemId == R.id.nav_home) {
                intent1 = new Intent(OrderAccepted.this, PendingOrder.class);
            } else if (itemId == R.id.nav_orders) {
                intent1 = new Intent(OrderAccepted.this, DeliveryHistory.class);
            } else if (itemId == R.id.nav_delivery) {
                intent1 = new Intent(OrderAccepted.this, OrderAccepted.class);
            } else return false;

            intent1.putExtra("email", userEmail);
            intent1.putExtra("password", userPassword);
            intent1.putExtra("userName", userName);
            intent1.putExtra("userPhone", userPhone);
            startActivity(intent1);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        });
    }

    private void filterOrders(String query) {
        List<Order> filteredList = fullOrderList.stream()
                .filter(order -> order.getOrderStatus().equalsIgnoreCase("Preparing"))
                .filter(order -> {
                    String lowerQuery = query.toLowerCase();
                    String orderId = String.valueOf(order.getOrderId()).toLowerCase();
                    String status = order.getOrderStatus().toLowerCase();

                    Restaurant restaurant = new RestaurantRepository(this).getRestaurantById(order.getRestaurantId());
                    String restaurantAddress = (restaurant != null) ? restaurant.getAddress().toLowerCase() : "";

                    return orderId.contains(lowerQuery)
                            || status.contains(lowerQuery)
                            || restaurantAddress.contains(lowerQuery);
                })
                .collect(Collectors.toList());

        adapter.updateOrders(filteredList);
    }
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1);
    }
}
