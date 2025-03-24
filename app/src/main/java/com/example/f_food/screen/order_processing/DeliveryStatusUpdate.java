package com.example.f_food.screen.order_processing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.R;
import com.example.f_food.adapter.FoodUpdateScreenAdapter;
import com.example.f_food.entity.Food;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.OrderDetailRepository;
import com.example.f_food.repository.OrderRepository;

import com.example.f_food.repository.ShipperRepository;

import com.example.f_food.screen.features_customer.GoogleMaps;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DeliveryStatusUpdate extends AppCompatActivity {
    private TextView tvOrderId, tvRestaurantAddress, tvDeliveryAddress, tvDeliveryTime, tvCost, tvDistance, DeliCost;
    private RecyclerView foodRecyclerView;
    private RadioGroup rgStatus;
    private Button btnUpdate;

    private RadioButton rbDeli;
    private double distance;
    private ImageButton btnOpenMap;
    private BottomNavigationView bottomNavigationView;

    private OrderRepository orderRepository;
    private FoodRepository foodRepository;
    private OrderDetailRepository orderDetailRepository;


    private List<Food> foodList;
    private int shipperId = 0;

    private int orderId;
    private double distanceKm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_status_update);

        // 1. Khởi tạo repository
        orderRepository = new OrderRepository(this);
        orderDetailRepository = new OrderDetailRepository(this);
        foodRepository = new FoodRepository(this);

        ShipperRepository shipperRepository = new ShipperRepository(this);
        shipperId = shipperRepository.getShipperByUserId(getLoggedInUserId()).getShipperId();

        tvOrderId = findViewById(R.id.orderId);
        tvRestaurantAddress = findViewById(R.id.restaurantAddress);
        tvDeliveryAddress = findViewById(R.id.deliveryAddress);
        tvDeliveryTime = findViewById(R.id.deliveryTime);
        tvCost = findViewById(R.id.foodCost);
        DeliCost = findViewById(R.id.DeliCost);
        btnOpenMap = findViewById(R.id.btnOpenMap);
        rgStatus = findViewById(R.id.rg_status);
        btnUpdate = findViewById(R.id.btn_update);
        foodRecyclerView = findViewById(R.id.foodListAcceptShipping);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        rbDeli = findViewById(R.id.rb_out_for_delivery);

        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Lấy dữ liệu từ intent
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", -1);
        String restaurantAddress = intent.getStringExtra("restaurantAddress");
        String deliveryAddress = intent.getStringExtra("deliveryAddress");
        String deliveryTime = intent.getStringExtra("deliveryTime");
        double cost = intent.getDoubleExtra("cost", 0.0);

        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        String userEmail = intent.getStringExtra("email");
        String userPassword = intent.getStringExtra("password");

        // 4. Hiển thị thông tin cơ bản
        tvOrderId.setText("Order ID: " + orderId);
        tvRestaurantAddress.setText("Restaurant: " + restaurantAddress);
        tvDeliveryAddress.setText("Delivery Address: " + deliveryAddress);
        tvDeliveryTime.setText("Delivery Time: " + deliveryTime);
        tvCost.setText("Total Cost: $" + cost);

        // 5. Load món ăn
        List<Food> foodList = orderRepository.getFoodListByOrderId(orderId);
        if (foodList != null && !foodList.isEmpty()) {
            FoodUpdateScreenAdapter foodAdapter = new FoodUpdateScreenAdapter(foodList);
            foodRecyclerView.setAdapter(foodAdapter);
        }

        // 6. Tính khoảng cách
        calculateDistanceAndDisplay(restaurantAddress, deliveryAddress, distanceKm -> {
            distance = distanceKm; // gán vào biến của class
            Log.d("DISTANCE_LOG", "Khoảng cách là: " + distanceKm + " km");

            // Cập nhật UI sau khi có khoảng cách
            tvDeliveryAddress.setText("Delivery Address: " + deliveryAddress + " - " + String.format("%.1f km", distance));
            DeliCost.setText("Phí ship: " + String.format("%.0f", distance * 10000) + " VND");
        });

        // 7. Mở bản đồ
        btnOpenMap.setOnClickListener(v -> openMap(restaurantAddress, deliveryAddress));

        // 8. Cập nhật trạng thái đơn
        rbDeli.setChecked(true);

        btnUpdate.setOnClickListener(v -> {
            int checkedId = rgStatus.getCheckedRadioButtonId();
            String status = "";

            if (checkedId == R.id.rb_out_for_delivery) status = "Delivering";
            else if (checkedId == R.id.rb_delivered) status = "Delivered";

            if (!status.isEmpty()) {
                orderRepository.updateOrderStatus(orderId, status, shipperId);
                Toast.makeText(this, "Cập nhật trạng thái: " + status, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Vui lòng chọn trạng thái!", Toast.LENGTH_SHORT).show();
            }
        });

        // 9. Bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.nav_delivery);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent navIntent = new Intent();
            if (item.getItemId() == R.id.nav_home) {
                navIntent.setClass(this, PendingOrder.class);
            } else if (item.getItemId() == R.id.nav_orders) {
                navIntent.setClass(this, DeliveryHistory.class);
            } else if (item.getItemId() == R.id.nav_delivery) {
                navIntent.setClass(this, OrderAccepted.class);
            }
            navIntent.putExtra("email", userEmail);
            navIntent.putExtra("password", userPassword);
            navIntent.putExtra("userName", userName);
            navIntent.putExtra("userPhone", userPhone);
            startActivity(navIntent);
            return true;
        });
    }

    public interface DistanceCallback {
        void onDistanceCalculated(double distance);
    }

    private void calculateDistanceAndDisplay(String resAddress, String deliveryAddress, DeliveryDetails.DistanceCallback callback) {
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
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1);
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
