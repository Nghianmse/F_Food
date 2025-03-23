package com.example.f_food.screen.features_restaurant_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.OrdersRestaurantAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Order;
import com.example.f_food.repository.RestaurantRepository;
import java.util.List;

public class RestaurantOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersRestaurantAdapter adapter;
    private RestaurantRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_orders);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurantOrdersActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    private void loadOrders() {
        db = RestaurantRoomDatabase.getInstance(this);
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        int uid = getLoggedInUserId();
        int rid = restaurantRepository.getRestaurantByUserId(uid).getRestaurantId();

        List<Order> orderList = db.orderDAO().getOrdersByRestaurantId(rid);

        adapter = new OrdersRestaurantAdapter(this, orderList, new OrdersRestaurantAdapter.OnOrderClickListener() {
            @Override
            public void onAcceptClick(Order order) {
                order.setOrderStatus("Delivered");
                db.orderDAO().update(order);
                loadOrders();
            }

            @Override
            public void onRejectClick(Order order) {
                order.setOrderStatus("Cancelled");
                db.orderDAO().update(order);
                loadOrders();
            }

            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(RestaurantOrders.this, OrderDetailActivity.class);
                intent.putExtra("order_id", order.getOrderId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1); // Trả về -1 nếu không tìm thấy userId
    }
}