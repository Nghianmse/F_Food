package com.example.f_food.screen.features_customer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.OrderTrackingAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Order;
import com.example.f_food.R;

import java.util.List;

public class OrderTracking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderTrackingAdapter adapter;
    private RestaurantRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        recyclerView = findViewById(R.id.orderTracking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = RestaurantRoomDatabase.getInstance(this);
        loadOrders();
    }

    private void loadOrders() {
        // Instead of a raw thread, it's better to use a background task with proper handling.
        new Thread(() -> {
            // Query the database for filtered orders
            List<Order> orderList = db.orderDAO().getFilteredOrders();
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
}
