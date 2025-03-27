package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.FoodAcceptShippingAdapter;
import com.example.f_food.entity.OrderDetail;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.OrderDetailRepository;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.screen.features_customer.GoogleMaps;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DeliveryDetails extends AppCompatActivity {
    private TextView tvOrderId, tvRestaurantAddress, tvDeliveryAddress, tvDeliveryTime, tvCost, deliCost;
    private RecyclerView foodRecyclerView;
    private BottomNavigationView bottomNavigationView;

    private double distance;
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

        orderRepository = new OrderRepository(this);
        orderDetailRepository = new OrderDetailRepository(this);
        foodRepository = new FoodRepository(this);

        tvOrderId = findViewById(R.id.orderId);
        tvRestaurantAddress = findViewById(R.id.restaurantAddress);
        tvDeliveryAddress = findViewById(R.id.deliveryAddress);
        tvDeliveryTime = findViewById(R.id.deliveryTime);
        tvCost = findViewById(R.id.foodCost);
        deliCost = findViewById(R.id.DeliCost);
        foodRecyclerView = findViewById(R.id.foodDeliveryDetails);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ImageButton btnOpenMap = findViewById(R.id.btnOpenMap);

        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int orderId = getIntent().getIntExtra("orderId", -1);
        String restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        String deliveryAddress = getIntent().getStringExtra("deliveryAddress");
        String deliveryTime = getIntent().getStringExtra("deliveryTime");
        double totalCost = orderRepository.getTotalPriceByOrderId(orderId);

        tvOrderId.setText("Order Code: ODFOD" + orderId);
        tvRestaurantAddress.setText("Restaurant: " + restaurantAddress);
        tvDeliveryTime.setText("Delivery Time: " + deliveryTime);
        tvCost.setText("Total Cost: $" + totalCost);

        calculateDistanceAndDisplay(restaurantAddress, deliveryAddress, distanceKm -> {
            distance = distanceKm;
            tvDeliveryAddress.setText("Delivery Address: " + deliveryAddress + " - " + String.format("%.1f km", distance));
            deliCost.setText("Phí ship: " + String.format("%.0f", distance * 10000) + " VND");
        });

        List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailsByOrderId(orderId);
        if (orderDetails != null && !orderDetails.isEmpty()) {
            FoodAcceptShippingAdapter foodAdapter = new FoodAcceptShippingAdapter(this, orderDetails);
            foodRecyclerView.setAdapter(foodAdapter);
        }

        btnOpenMap.setOnClickListener(v -> openMap(restaurantAddress, deliveryAddress));

        bottomNavigationView.setSelectedItemId(R.id.nav_orders);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent navIntent = new Intent();

                if (itemId == R.id.nav_home) {
                    navIntent.setClass(DeliveryDetails.this, PendingOrder.class);
                } else if (itemId == R.id.nav_orders) {
                    navIntent.setClass(DeliveryDetails.this, DeliveryHistory.class);
                } else if (itemId == R.id.nav_delivery) {
                    navIntent.setClass(DeliveryDetails.this, OrderAccepted.class);
                }

                navIntent.putExtra("email", userEmail);
                navIntent.putExtra("password", userPassword);
                navIntent.putExtra("userName", userName);
                navIntent.putExtra("userPhone", userPhone);
                startActivity(navIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
        });
    }

    public interface DistanceCallback {
        void onDistanceCalculated(double distance);
    }

    private void calculateDistanceAndDisplay(String resAddress, String deliveryAddress, DistanceCallback callback) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        new Thread(() -> {
            try {
                List<Address> startList = geocoder.getFromLocationName(resAddress, 1);
                List<Address> endList = geocoder.getFromLocationName(deliveryAddress, 1);

                if (!startList.isEmpty() && !endList.isEmpty()) {
                    Address start = startList.get(0);
                    Address end = endList.get(0);

                    float[] result = new float[1];
                    android.location.Location.distanceBetween(
                            start.getLatitude(), start.getLongitude(),
                            end.getLatitude(), end.getLongitude(), result);

                    double calculatedDistance = result[0] / 1000f;
                    runOnUiThread(() -> callback.onDistanceCalculated(calculatedDistance));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void openMap(String resAddress, String deliveryAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> startList = geocoder.getFromLocationName(resAddress, 1);
            List<Address> endList = geocoder.getFromLocationName(deliveryAddress, 1);

            if (!startList.isEmpty() && !endList.isEmpty()) {
                Address start = startList.get(0);
                Address end = endList.get(0);

                Intent intentMap = new Intent(this, GoogleMaps.class);
                intentMap.putExtra("origin_lat", start.getLatitude());
                intentMap.putExtra("origin_lng", start.getLongitude());
                intentMap.putExtra("dest_lat", end.getLatitude());
                intentMap.putExtra("dest_lng", end.getLongitude());
                intentMap.putExtra("address", end.getAddressLine(0));
                startActivity(intentMap);
            } else {
                Toast.makeText(this, "Không tìm thấy một trong hai vị trí!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tìm địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }
}
