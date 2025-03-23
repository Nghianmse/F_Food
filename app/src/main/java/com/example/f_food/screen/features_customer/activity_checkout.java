package com.example.f_food.screen.features_customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.CheckoutAdapter;
import com.example.f_food.dao.AddressDAO;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Address;
import com.example.f_food.entity.CartItem;
import com.example.f_food.R;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.OrderDetail;
import com.example.f_food.entity.User;
import com.example.f_food.repository.OrderDetailRepository;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.repository.UserRepository;
import com.example.f_food.screen.order_processing.AcceptShippingOrder;


import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;import java.util.Date;
import java.io.IOException;
import java.util.ArrayList;import java.util.Date;
import java.util.List;

import java.util.Locale;

public class activity_checkout extends AppCompatActivity {
    private double distance;
    private RecyclerView recyclerView;
    private CheckoutAdapter checkoutAdapter;
    private TextView phoneCheckout, addressCheckout;
    private TextView totalPriceCheckout, totalShipCheckout, totalSaleCheckout, totalPrice;
    double totalPrice2;
    Button btnCreateOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_checkout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hideSystemUI();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> hideSystemUI());

        recyclerView = findViewById(R.id.recyclerCheckout);
        phoneCheckout = findViewById(R.id.phoneCheckout);
        addressCheckout = findViewById(R.id.addressCheckout);
        totalPriceCheckout = findViewById(R.id.totalPriceCheckout);
        totalShipCheckout = findViewById(R.id.totalShipCheckout);
        totalSaleCheckout = findViewById(R.id.totalSaleCheckout);
        totalPrice = findViewById(R.id.totalPrice);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);






        ArrayList<CartItem> selectedItems = getIntent().getParcelableArrayListExtra("selectedItems");
        UserRepository userRepository = new UserRepository(this);
        OrderRepository orderRepository = new OrderRepository(this);
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository(this);
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);

        AddressDAO addressDAO = RestaurantRoomDatabase.getInstance(this).addressDAO();

        int uId = getLoggedInUserId();
        User u = userRepository.getUserById(uId);
        if (u != null) {
            phoneCheckout.setText(u.getPhone());
        } else {
            phoneCheckout.setText("Chưa có số điện thoại");
        }

        Address a = addressDAO.getDefaultAddressForUser(uId);
//        if (a != null) {
//            addressCheckout.setText(a.getAddress() + ", " + a.getDetailAddress());
//        } else {
//            addressCheckout.setText("Chưa có địa chỉ giao hàng");
//        }

        if (selectedItems != null) {
            checkoutAdapter = new CheckoutAdapter(selectedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(checkoutAdapter);
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


        ImageButton btnOpenMap = findViewById(R.id.btnOpenMap);
        Intent intentRId = getIntent();
        int rid = intentRId.getIntExtra("rId", 1);

        String rAddress = restaurantRepository.getRestaurantById(rid).getAddress();
        btnOpenMap.setOnClickListener(v -> openMap(rAddress, a.getAddress()));


        calculateDistanceAndDisplay(rAddress, a.getAddress(), distanceKm -> {
            distance = distanceKm; // gán vào biến của class
            Log.d("DISTANCE_LOG", "Khoảng cách là: " + distanceKm + " km");
            double totalPrice1 = getIntent().getDoubleExtra("totalPrice", 8);
            double ship = 10.0;
            double discount = getIntent().getDoubleExtra("discount", 8);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            totalPrice2 = totalPrice1 + (distance * 10000.0) - discount;

            totalPriceCheckout.setText("Tạm tính: " + currencyFormat.format(totalPrice1));
            totalSaleCheckout.setText("Giảm giá: " + currencyFormat.format(discount));
            totalPrice.setText("Tổng thanh toán: " + currencyFormat.format(totalPrice2));

// Cập nhật UI sau khi có khoảng cách
            addressCheckout.setText(a.getAddress() + " - " + String.format("%.1f km", distance));
            totalShipCheckout.setText("Phí ship: " + currencyFormat.format(distance * 10000));
        });

        btnCreateOrder.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận đặt hàng")
                    .setMessage("Bạn có chắc chắn muốn đặt hàng không?")
                    .setPositiveButton("Có", (dialog, which) -> {

                        Order o = new Order();
                        o.setUserId(uId);
                        o.setRestaurantId(rid);
                        o.setTotalPrice(totalPrice2);
                        o.setPaymentMethod("COD");
                        o.setOrderStatus("Pending");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = sdf.format(new Date());
                        o.setCreatedAt(formattedDate);
                        orderRepository.insert(o);

                        for (CartItem item : selectedItems) {
                            OrderDetail orderDetail = new OrderDetail();
                            orderDetail.setOrderId(orderRepository.getLastInsertedOrder().getOrderId());
                            orderDetail.setFoodId(item.getProduct().getFoodId());
                            orderDetail.setQuantity(item.getQuantity());
                            orderDetail.setPrice(item.getProduct().getPrice());

                            // Lưu OrderDetail vào database
                            orderDetailRepository.insert(orderDetail);
                        }

                        new AlertDialog.Builder(this)
                                .setTitle("Đặt hàng thành công")
                                .setMessage("Đơn hàng của bạn đã được tạo thành công!\nBạn có muốn tiếp tục mua hàng không?")
                                .setPositiveButton("Tiếp tục mua hàng", (dialog1, which1) -> {
                                    Intent intent = new Intent(this, HomeStart.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .setNegativeButton("OK", (dialog1, which1) -> {
                                    Intent intent = new Intent(this, activity_cart.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .setCancelable(false) // Không cho phép đóng hộp thoại khi bấm ra ngoài
                                .show();
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .setCancelable(false)
                    .show();
        });
    }

    private void openMap(String resAddress, String deliveryAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> startList = geocoder.getFromLocationName(resAddress, 1);
            List<android.location.Address> endList = geocoder.getFromLocationName(deliveryAddress, 1);
            if (!startList.isEmpty() && !endList.isEmpty()) {
                android.location.Address start = startList.get(0);
                android.location.Address end = endList.get(0);

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
    public interface DistanceCallback {
        void onDistanceCalculated(double distance);
    }

    private void calculateDistanceAndDisplay(String resAddress, String deliveryAddress, AcceptShippingOrder.DistanceCallback callback) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        new Thread(() -> {
            try {
                List<android.location.Address> startList = geocoder.getFromLocationName(resAddress, 1);
                List<android.location.Address> endList = geocoder.getFromLocationName(deliveryAddress, 1);

                if (!startList.isEmpty() && !endList.isEmpty()) {
                    android.location.Address start = startList.get(0);
                    android.location.Address end = endList.get(0);

                    float[] result = new float[1];
                    android.location.Location.distanceBetween(
                            start.getLatitude(), start.getLongitude(),
                            end.getLatitude(), end.getLongitude(),
                            result
                    );

                    double calculatedDistance = result[0] / 1000f;

                    runOnUiThread(() -> {
//                        tvDeliveryAddress.setText("Delivery Address: " + deliveryAddress + " - " + String.format("%.1f km", calculatedDistance));
                        callback.onDistanceCalculated(calculatedDistance); // Gọi callback
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1);
    }
    private String formatDateTime(String createdAt) {
        try {
            // Định dạng ban đầu của createdAt (ví dụ: "2025-03-10 14:30:15")
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Định dạng hiển thị mới (Chỉ ngày/tháng + giờ/phút)
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

            Date date = inputFormat.parse(createdAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return createdAt; // Nếu lỗi, hiển thị chuỗi gốc
        }
    }
}