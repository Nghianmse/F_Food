package com.example.f_food.screen.features_restaurant_management;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.OrderDetailAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Food;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.OrderDetail;
import com.example.f_food.repository.OrderDetailRepository;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvUserId, tvStatus, tvCreatedAt, tvTotalPrice;
    private RecyclerView recyclerViewFoods;
    private OrderDetailAdapter adapter;
    private OrderDetailRepository repository;
    private RestaurantRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Ánh xạ view
        tvOrderId = findViewById(R.id.tvOrderId);
        tvUserId = findViewById(R.id.tvUserId);
        tvStatus = findViewById(R.id.tvStatus);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        recyclerViewFoods = findViewById(R.id.recyclerViewFoods);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        db = RestaurantRoomDatabase.getInstance(this);

        repository = new OrderDetailRepository(this);

        int orderId = getIntent().getIntExtra("order_id", -1);

        if (orderId != -1) {
            Order order = db.orderDAO().getOrderById(orderId);
            if (order != null) {
                tvOrderId.setText("Order ID: " + order.getOrderId());
                tvUserId.setText("User ID: " + order.getUserId());
                tvStatus.setText("Trạng thái: " + order.getOrderStatus());
                tvCreatedAt.setText("Ngày tạo: " + order.getCreatedAt());
                tvTotalPrice.setText("Tổng tiền: " + String.format("%,.0f VNĐ", order.getTotalPrice()));

                // Lấy danh sách món ăn theo orderId
                List<OrderDetail> orderDetails = db.orderDetailDAO().getOrderDetailsByOrderId(orderId);
                List<Food> foodList = new ArrayList<>();
                for (OrderDetail detail : orderDetails) {
                    Food food = db.foodDAO().getFoodById(detail.getFoodId());
                    if (food != null) {
                        foodList.add(food);
                    }
                }

                adapter = new OrderDetailAdapter(this, foodList);
                recyclerViewFoods.setAdapter(adapter);
            }
        }
    }
}
