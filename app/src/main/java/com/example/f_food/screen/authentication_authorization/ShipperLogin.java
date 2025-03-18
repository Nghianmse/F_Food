package com.example.f_food.screen.authentication_authorization;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.entity.User;
import com.example.f_food.MainActivity;
import com.example.f_food.R;
import com.example.f_food.repository.UserRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShipperLogin extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnLoginForCustomer, btnRegister;
    private TextView tvForgotPassword;
    private UserRepository userRepository;
    ImageView imgLogoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_login);

        // Ánh xạ UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginForCustomer = findViewById(R.id.btnLoginForCustomer);  // Initialize the Login for Partner button
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        imgLogoLogin = findViewById(R.id.imgLogoLogin);
        btnRegister = findViewById(R.id.btnRegister);

        Picasso.get()
                .load(R.drawable.login)
                .resize(500, 500)
                .centerCrop()
                .into(imgLogoLogin);

        // Khởi tạo repository
        userRepository = new UserRepository(this);

        // Xử lý sự kiện khi nhấn nút đăng nhập
        btnLogin.setOnClickListener(v -> handleLogin());
        // Xử lý sự kiện khi nhấn nút đăng nhập cho partner
        btnLoginForCustomer.setOnClickListener(v -> navigateToShipperLogIn());
        btnRegister.setOnClickListener(view -> navigateToShipperRegister());
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy danh sách user từ database
        List<User> users = userRepository.getAllUsers();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                // Kiểm tra UserType là "Customer"
                if ("Shipper".equals(user.getUserType())) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình khác sau khi đăng nhập
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        // Nếu không tìm thấy user phù hợp
        Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
    }
    private void navigateToShipperLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);  // Assuming RestaurantLogInActivity is your target activity
        startActivity(intent);
    }
    private void navigateToShipperRegister() {
        Intent intent = new Intent(this, ShipperSignUp.class);  // Assuming RestaurantLogInActivity is your target activity
        startActivity(intent);
    }
}
