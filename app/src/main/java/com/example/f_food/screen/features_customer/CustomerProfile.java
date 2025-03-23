package com.example.f_food.screen.features_customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.f_food.entity.Address;
import com.example.f_food.entity.User;
import com.example.f_food.dao.AddressDAO;
import com.example.f_food.dao.UserDAO;
import com.example.f_food.repository.AddressRepository;
import com.example.f_food.dao.RestaurantRoomDatabase;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.R;

public class CustomerProfile extends AppCompatActivity {
    // Lấy DB

    private TextView tvAddress;
    private EditText tvFullName, tvPhone, tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_profile);

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customerProfile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AddressRepository addressRepository = new AddressRepository(this);

        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(this);
        int userId = getLoggedInUserId();
        // Lấy user info
        User user = db.userDAO().getUserById(userId);
        // Lấy địa chỉ mặc định
        Address defaultAddress = db.addressDAO().getDefaultAddressForUser(userId);
        // Hander changepass text
        TextView btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerProfile.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        // Handle Manage Address button
        Button btnManageAddress = findViewById(R.id.btnManageAddress);
        btnManageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerProfile.this, com.example.f_food.screen.features_customer.Address.class);
                startActivity(intent);
            }
        });

        // Handle icon events
        TextView trackingIcon = findViewById(R.id.trackingIcon);
        trackingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle tracking icon click
                Intent trackingIntent = new Intent(CustomerProfile.this, OrderTracking.class);
                startActivity(trackingIntent);
            }
        });

        TextView reviewIcon = findViewById(R.id.reviewIcon);
        reviewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle review icon click
                Intent reviewIntent = new Intent(CustomerProfile.this, OrderTracking.class);
                startActivity(reviewIntent);
            }
        });

        TextView historyIcon = findViewById(R.id.historyIcon);
        historyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle history icon click
                Intent historyIntent = new Intent(CustomerProfile.this, OrderHistory.class);
                startActivity(historyIntent);
            }
        });

        // Mapping view
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);

// Set dữ liệu
        if (user != null) {
            tvFullName.setText(user.getFullName());
            tvPhone.setText(user.getPhone());
            tvEmail.setText(user.getEmail());
        }

        if (defaultAddress != null) {
            String fullAddress = defaultAddress.getDetailAddress() + ", " + defaultAddress.getAddress();
            tvAddress.setText(fullAddress);
        } else {
            tvAddress.setText("Chưa có địa chỉ mặc định");
        }

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CustomerProfile.this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn thay đổi các thông tin?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Lấy dữ liệu từ EditText
                                String newFullName = tvFullName.getText().toString().trim();
                                String newPhone = tvPhone.getText().toString().trim();
                                String newEmail = tvEmail.getText().toString().trim();

                                if (user != null) {
                                    user.setFullName(newFullName);
                                    user.setPhone(newPhone);
                                    user.setEmail(newEmail);

                                    db.userDAO().update(user);

                                    Toast.makeText(CustomerProfile.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                                    // Load lại activity
                                    Intent intent = getIntent();
                                    finish(); // kết thúc activity hiện tại
                                    startActivity(intent); // chạy lại activity với dữ liệu mới
                                } else {
                                    Toast.makeText(CustomerProfile.this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Không thay đổi dữ liệu
                                Toast.makeText(CustomerProfile.this, "Không thay đổi thông tin.", Toast.LENGTH_SHORT).show();

                                // Nhưng vẫn reload lại activity
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });



    }
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1);
    }
}
