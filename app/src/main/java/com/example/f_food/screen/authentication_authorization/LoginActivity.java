package com.example.f_food.screen.authentication_authorization;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.UserRepository;
import com.example.f_food.screen.admin_management.AdminScreen;
import com.example.f_food.screen.features_customer.HomeStart;
import com.example.f_food.screen.features_customer.ManageAddress;
import com.example.f_food.screen.features_customer.ViewRestaurantList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnLoginForPartner, btnLoginForShipper;
    private TextView tvForgotPassword;
    private UserRepository userRepository;
    Button reigister;
    ImageView imgLogoLogin;
    private CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginForPartner = findViewById(R.id.btnLoginForPartner);
        btnLoginForShipper = findViewById(R.id.btnLoginForShipper);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        imgLogoLogin = findViewById(R.id.imgLogoLogin);
        reigister=findViewById(R.id.btnRegister);
        cbRememberMe = findViewById(R.id.cbRememberMe);

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
        btnLoginForShipper.setOnClickListener(v -> navigateToShipperLogIn());
        reigister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUp.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
            startActivity(intent);
        });
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
                    editor.putInt("userId", user.getUserId());

                    // Save email and password if "Remember Me" is checked
                    if (cbRememberMe.isChecked()) {
                        editor.putString("email", email);
                        editor.putString("password", password);
                    } else {
                        editor.remove("email");
                        editor.remove("password");
                    }
                    editor.apply();

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình OrderHistory
                    Intent intent = new Intent(this, HomeStart.class);
                    intent.putExtra("fullName", user.getFullName());
                    startActivity(intent);
                    finish();
                    return;
                } else if ("Admin".equals(user.getUserType())) {
                    // Lưu userId vào SharedPreferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("userId", user.getUserId());
                    editor.apply();

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình AdminScreen
                    Intent intent = new Intent(this, AdminScreen.class);
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

    private void navigateToShipperLogIn() {
        Intent intent = new Intent(this, ShipperLogin.class);  // Assuming RestaurantLogInActivity is your target activity
        startActivity(intent);
    }
}
