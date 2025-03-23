package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.adapter.OrderTrackingAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Order;
import com.example.f_food.R;
import com.example.f_food.screen.authentication_authorization.LoginActivity;

import java.util.List;

public class OrderTracking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderTrackingAdapter adapter;
    private RestaurantRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (!isUserLoggedIn()) {
            showAlertDialog("Bạn chưa đăng nhập, bạn vui lòng đăng nhập để thao tác.");
            return;
        }

        // Initialize views
        recyclerView = findViewById(R.id.orderTracking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the database
        db = RestaurantRoomDatabase.getInstance(this);

        // Set up the Home icon button
        ImageButton homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start HomeStart Activity when the home icon is clicked
                Intent intent = new Intent(OrderTracking.this, HomeStart.class);
                startActivity(intent);
                finish(); // Optionally finish the current activity if you don't want to return to it
            }
        });

        // Load orders
        loadOrders();
    }

    private void loadOrders() {
        // Get the logged-in userId
        int userId = getLoggedInUserId();

        // Instead of a raw thread, it's better to use a background task with proper handling.
        new Thread(() -> {
            // Query the database for orders filtered by userId
            List<Order> orderList = db.orderDAO().getFilteredOrdersByUserId(userId);
            runOnUiThread(() -> {
                // Set the adapter with the data
                if (adapter == null) {
                    adapter = new OrderTrackingAdapter(orderList, this);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setOrders(orderList);
                }
            });
        }).start();
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1); // Sử dụng PreferenceManager thay vì getSharedPreferences
        return userId != -1;
    }

    // Lấy userId của người dùng đã đăng nhập
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1); // Trả về -1 nếu không tìm thấy userId
    }

    // Hiển thị hộp thoại thông báo và chuyển sang màn hình đăng nhập
    private void showAlertDialog(String message) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(OrderTracking.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create()
                .show();
    }
}
