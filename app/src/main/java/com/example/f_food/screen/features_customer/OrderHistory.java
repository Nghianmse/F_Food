package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.OrderHistoryAdapter;
import com.example.f_food.entity.Order;
import com.example.f_food.R;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.screen.authentication_authorization.LoginActivity;

import java.util.List;

public class OrderHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.orderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (!isUserLoggedIn()) {
            showAlertDialog("Bạn chưa đăng nhập, bạn vui lòng đăng nhập để thao tác.");
            return;
        }

        // Lấy userId từ SharedPreferences
        int userId = getLoggedInUserId();

        // Fetch orders with status "Delivered" or "Cancelled" for the logged-in user
        orderList = RestaurantRoomDatabase.getInstance(this).orderDAO().getDeliveredOrCancelledOrdersByUserId(userId);

        // Set up the RecyclerView adapter
        orderAdapter = new OrderHistoryAdapter(orderList, this);
        recyclerView.setAdapter(orderAdapter);

        // Handle home icon click
        ImageButton homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(v -> {
            // Start HomeStart Activity when the home icon is clicked
            Intent intent = new Intent(OrderHistory.this, HomeStart.class);
            startActivity(intent);
            finish(); // Optionally finish the current activity if you don't want to return to it
        });
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        return userId != -1;
    }

    // Lấy userId của người dùng đã đăng nhập
    private int getLoggedInUserId() {
        SharedPreferences preferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        return preferences.getInt("userId", -1); // Trả về -1 nếu không tìm thấy userId
    }

    // Hiển thị hộp thoại thông báo và chuyển sang màn hình đăng nhập
    private void showAlertDialog(String message) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(OrderHistory.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create()
                .show();
    }
}
