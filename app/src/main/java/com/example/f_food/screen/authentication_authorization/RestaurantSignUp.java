package com.example.f_food.screen.authentication_authorization;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.entity.Restaurant;
import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.repository.UserRepository;

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
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Kiểm tra các trường không được bỏ trống
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            showAlertDialog("Please fill in all fields.");
            return;
        }

        // Kiểm tra định dạng email hợp lệ
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showAlertDialog("Invalid email format.");
            return;
        }

        // Kiểm tra số điện thoại có đúng định dạng Việt Nam không
        if (!isValidVietnamesePhoneNumber(phoneNumber)) {
            showAlertDialog("Invalid Vietnamese phone number.");
            return;
        }

        // Kiểm tra điều kiện mật khẩu
        if (!isValidPassword(password)) {
            showAlertDialog("Password must be at least 8 characters long and start with an uppercase letter.");
            return;
        }

        // Kiểm tra mật khẩu nhập lại có khớp không
        if (!password.equals(confirmPassword)) {
            showAlertDialog("Passwords do not match.");
            return;
        }

        // Kiểm tra xem email hoặc số điện thoại đã tồn tại chưa
        if (userRepository.getUserByEmail(email) != null) {
            showAlertDialog("Email is already registered.");
            return;
        }

        if (userRepository.getUserByPhone(phoneNumber) != null) {
            showAlertDialog("Phone number is already registered.");
            return;
        }

        // Tạo và lưu user
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phoneNumber);
        user.setPassword(password);
        user.setUserType("Restaurant");
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        user.setCreatedAt(currentDateTime);
        user.setUpdatedAt(currentDateTime);
        user.setIsDelete(false);

        userRepository.insert(user);

        // Lấy userId của user vừa thêm vào
        User insertedUser = userRepository.getAllUsers().get(userRepository.getAllUsers().size() - 1);
        int userId = insertedUser.getUserId();

        // Tạo và lưu nhà hàng
        Restaurant restaurant = new Restaurant();
        restaurant.setUserId(userId);
        restaurant.setName(fullName + " Restaurant");
        restaurant.setAddress(address);
        restaurant.setPhone(phoneNumber);
        restaurant.setStatus("Open");
        restaurant.setCreatedAt(currentDateTime);

        restaurantRepository.insert(restaurant);

        // Hiển thị thông báo thành công
        showSuccessDialog();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && Character.isUpperCase(password.charAt(0));
    }
    private boolean isValidVietnamesePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^0[0-9]{9,10}$");
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
