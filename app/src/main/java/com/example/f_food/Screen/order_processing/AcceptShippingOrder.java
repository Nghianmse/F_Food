package com.example.f_food.Screen.order_processing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.R;

public class AcceptShippingOrder extends AppCompatActivity {
    private TextView tvOrderId, tvRestaurantAddress, tvDeliveryAddress, tvDeliveryTime, tvFoodOrder, tvCost;
    private Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_shipping_order);

        // Ánh xạ view từ XML
        tvOrderId = findViewById(R.id.orderId);
        tvRestaurantAddress = findViewById(R.id.restaurantAddress);
        tvDeliveryAddress = findViewById(R.id.deliveryAddress);
        tvDeliveryTime = findViewById(R.id.deliveryTime);
        tvFoodOrder = findViewById(R.id.foodOrder);
        tvCost = findViewById(R.id.foodCost);
        btnAccept = findViewById(R.id.acceptButton);

        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int orderId = extras.getInt("orderId", -1);
            String restaurantAddress = extras.getString("restaurantAddress", "N/A");
            String deliveryAddress = extras.getString("deliveryAddress", "N/A");
            String deliveryTime = extras.getString("deliveryTime", "N/A");
            String foodOrder = extras.getString("foodOrder", "N/A");
            double cost = extras.getDouble("cost", 0.0);

            // Hiển thị dữ liệu lên giao diện
            tvOrderId.setText("Order ID: " + orderId);
            tvRestaurantAddress.setText("Restaurant: " + restaurantAddress);
            tvDeliveryAddress.setText("Delivery: " + deliveryAddress);
            tvDeliveryTime.setText("Time: " + deliveryTime);
            tvFoodOrder.setText("Food: " + foodOrder);
            tvCost.setText("Cost: $" + cost);
        }

        // Xử lý khi nhấn nút Accept
        btnAccept.setOnClickListener(v -> showAcceptDialog());
    }

    // Hàm hiển thị AlertDialog khi ấn "ACCEPT"
    private void showAcceptDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Order Accepted")
                .setMessage("Accept Order Successfully!")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish(); // Đóng màn hình sau khi nhấn OK
                })
                .show();
    }
}
