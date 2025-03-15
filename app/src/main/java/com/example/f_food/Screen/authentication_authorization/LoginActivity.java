package com.example.f_food.Screen.authentication_authorization;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.Entity.OrderDetail;
import com.example.f_food.Entity.User;
import com.example.f_food.MainActivity;
import com.example.f_food.R;
import com.example.f_food.Repository.OrderDetailRepository;
import com.example.f_food.Repository.UserRepository;
import com.example.f_food.Screen.features_customer.ManageAddress;
import com.example.f_food.Screen.features_customer.OrderHistory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnLoginForPartner;
    private TextView tvForgotPassword;
    private UserRepository userRepository;
    ImageView imgLogoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginForPartner = findViewById(R.id.btnLoginForPartner);  // Initialize the Login for Partner button
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        imgLogoLogin = findViewById(R.id.imgLogoLogin);

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
        btnLoginForPartner.setOnClickListener(v -> navigateToRestaurantLogIn());
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlertDialog("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Lấy danh sách user từ database
        List<User> users = userRepository.getAllUsers();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                // Kiểm tra UserType là "Customer"
                if ("Customer".equals(user.getUserType())) {
                    // Lưu userId vào SharedPreferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("userId", user.getUserId()); // Giả sử `getId()` trả về userId
                    editor.apply();

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình OrderHistory
                    Intent intent = new Intent(this, ManageAddress.class);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    showAlertDialog("Email hoặc mật khẩu không đúng!");
                    return;
                }
            }
        }

        // Nếu không tìm thấy user phù hợp
        showAlertDialog("Email hoặc mật khẩu không đúng!");
    }
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Method to navigate to restaurant login screen
    private void navigateToRestaurantLogIn() {
        Intent intent = new Intent(this, RestaurantLogIn.class);  // Assuming RestaurantLogInActivity is your target activity
        startActivity(intent);
    }
}
