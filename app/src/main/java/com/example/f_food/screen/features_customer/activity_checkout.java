package com.example.f_food.screen.features_customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;import java.util.Date;
import java.util.Locale;

public class activity_checkout extends AppCompatActivity {

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

        double totalPrice1 = getIntent().getDoubleExtra("totalPrice", 8);
        double ship = 10.0;
        double discount = getIntent().getDoubleExtra("discount", 8);
        totalPrice2 = totalPrice1 + ship - discount;

        totalPriceCheckout.setText("Tạm tính: " + String.format("%.2f VND", totalPrice1));
        totalShipCheckout.setText("Phí ship: " + String.format("%.2f VND", ship));
        totalSaleCheckout.setText("Giảm giá: " + String.format("%.2f VND", discount));
        totalPrice.setText("Tổng thanh toán: " + String.format("%.2f VND", totalPrice2));

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
        if (a != null) {
            addressCheckout.setText(a.getAddress() + ", " + a.getDetailAddress());
        } else {
            addressCheckout.setText("Chưa có địa chỉ giao hàng");
        }

        if (selectedItems != null) {
            checkoutAdapter = new CheckoutAdapter(selectedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(checkoutAdapter);
        }
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        btnCreateOrder.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận đặt hàng")
                    .setMessage("Bạn có chắc chắn muốn đặt hàng không?")
                    .setPositiveButton("Có", (dialog, which) -> {


                        for (CartItem item : selectedItems) {

                            Order o = new Order();
                            o.setUserId(uId);
                            o.setRestaurantId(item.getProduct().getRestaurantId());
                            o.setTotalPrice(item.getProduct().getPrice() * item.getQuantity());
                            o.setPaymentMethod("COD");
                            o.setOrderStatus("Pending");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = sdf.format(new Date());
                            o.setCreatedAt(formattedDate);
                            orderRepository.insert(o);

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