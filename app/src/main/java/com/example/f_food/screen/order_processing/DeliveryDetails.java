package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.FoodAcceptShippingAdapter;
import com.example.f_food.dao.OrderDetailDAO;
import com.example.f_food.entity.Food;
import com.example.f_food.entity.OrderDetail;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.OrderDetailRepository;
import com.example.f_food.repository.OrderRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DeliveryDetails extends AppCompatActivity {
    private TextView tvOrderId, tvRestaurantAddress, tvDeliveryAddress, tvDeliveryTime, tvCost;
    private RecyclerView foodRecyclerView;
    private Button acceptButton;
    private BottomNavigationView bottomNavigationView;

    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;
    private FoodRepository foodRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        String userEmail = intent.getStringExtra("email");
        String userPassword = intent.getStringExtra("password");
        Log.d("DeliveryHistory", "Tên: " + userEmail + ", Email: " + userEmail);

        // Khởi tạo repository
        orderRepository = new OrderRepository(this);
        orderDetailRepository = new OrderDetailRepository(this);
        foodRepository = new FoodRepository(this);

        // Ánh xạ UI
        tvOrderId = findViewById(R.id.orderId);
        tvRestaurantAddress = findViewById(R.id.restaurantAddress);
        tvDeliveryAddress = findViewById(R.id.deliveryAddress);
        tvDeliveryTime = findViewById(R.id.deliveryTime);
        tvCost = findViewById(R.id.foodCost);
        foodRecyclerView = findViewById(R.id.foodDeliveryDetails);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Nhận dữ liệu từ Intent
        int orderId = getIntent().getIntExtra("orderId", -1);
        String restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        String deliveryAddress = getIntent().getStringExtra("deliveryAddress");
        String deliveryTime = getIntent().getStringExtra("deliveryTime");

        double totalCost = orderRepository.getTotalPriceByOrderId(orderId);

        // Hiển thị thông tin đơn hàng
        tvOrderId.setText("Order ID: " + orderId);
        tvRestaurantAddress.setText("Restaurant: " + restaurantAddress);
        tvDeliveryAddress.setText("Delivery Address: " + deliveryAddress);
        tvDeliveryTime.setText("Delivery Time: " + deliveryTime);
        tvCost.setText("Total Cost: $" + totalCost);

        // Lấy danh sách OrderDetail theo OrderId
        List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailsByOrderId(orderId);

        if (orderDetails != null && !orderDetails.isEmpty()) {
            FoodAcceptShippingAdapter foodAdapter = new FoodAcceptShippingAdapter(this, orderDetails);
            foodRecyclerView.setAdapter(foodAdapter);
        } else {
            Log.d("AcceptShippingOrder", "Không có món ăn nào trong đơn hàng.");
        }



        // 🚀 Xử lý sự kiện chuyển màn hình khi bấm vào BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

        // ✅ Xử lý khi bấm vào `nav_home` để quay lại `PendingOrder`
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) { // 🔹 Quay lại PendingOrder
                    Intent intent = new Intent(DeliveryDetails.this, PendingOrder.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_orders) { // 🔹 Giữ nguyên trang
                    Intent intent = new Intent( DeliveryDetails.this, DeliveryHistory.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_delivery) { // 🔹 Chuyển sang DeliveryStatusUpdate
                    Intent intent = new Intent(DeliveryDetails.this, OrderAccepted.class);
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