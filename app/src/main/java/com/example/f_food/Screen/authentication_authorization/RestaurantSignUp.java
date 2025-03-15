package com.example.f_food.Screen.authentication_authorization;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.Entity.Restaurant;
import com.example.f_food.Entity.User;
import com.example.f_food.R;
import com.example.f_food.Repository.RestaurantRepository;
import com.example.f_food.Repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RestaurantSignUp extends AppCompatActivity {
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    private EditText etFullName, etEmail, etPhoneNumber, etAddress, etPassword, etConfirmPassword;
    private Button btnConfirm;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_sign_up);

        // Initialize repositories
        userRepository = new UserRepository(this);
        restaurantRepository = new RestaurantRepository(this);

        // Initialize UI components
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(v -> navigateToRestaurantLogIn());
        btnConfirm.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String address = etAddress.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Basic validation
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(password) || !password.equals(confirmPassword)) {
            showAlertDialog("Please fill in all fields correctly");
            return;
        }

// Check if email or phone number already exists
        if (userRepository.getUserByEmail(email) != null) {
            showAlertDialog("Email already registered");
            return;
        }

        if (userRepository.getUserByPhone(phoneNumber) != null) {
            showAlertDialog("Phone number already registered");
            return;
        }


        // Create and insert user
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phoneNumber);
        user.setPassword(password);
        user.setUserType("Restaurant");
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        user.setCreatedAt(currentDateTime);
        user.setUpdatedAt(currentDateTime);

        userRepository.insert(user);

        // Get the userId of the newly inserted user (assuming last inserted)
        User insertedUser = userRepository.getAllUsers().get(userRepository.getAllUsers().size() - 1);
        int userId = insertedUser.getUserId();

        // Create and insert restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setUserId(userId);
        restaurant.setName(fullName + " Restaurant");
        restaurant.setAddress(address);
        restaurant.setPhone(phoneNumber);
        restaurant.setStatus("Open");
        restaurant.setCreatedAt(currentDateTime);

        restaurantRepository.insert(restaurant);

        // Hiển thị pop-up thay vì Toast
        showSuccessDialog();
    }

    // Phương thức hiển thị popup
    private void showSuccessDialog() {
        // Tạo AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");

        // Sử dụng Layout tùy chỉnh với icon tích V màu xanh
        builder.setMessage("Restaurant registered successfully")
                .setIcon(R.drawable.ic_check_green)  // Thêm icon tick màu xanh lá
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển hướng về trang đăng nhập
                    navigateToRestaurantLogIn();
                });

        // Hiển thị Dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void navigateToRestaurantLogIn() {
        Intent intent = new Intent(this, RestaurantLogIn.class);  // Assuming RestaurantLogInActivity is your target activity
        startActivity(intent);
    }
}