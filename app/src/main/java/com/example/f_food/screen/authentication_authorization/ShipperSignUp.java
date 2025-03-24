package com.example.f_food.screen.authentication_authorization;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.entity.Shipper;
import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.ShipperRepository;
import com.example.f_food.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShipperSignUp extends AppCompatActivity {
    private UserRepository userRepository;
    private ShipperRepository shipperRepository;

    private EditText etFullName, etEmail, etPhoneNumber, etAddress, etPassword, etConfirmPassword;
    private Button btnConfirm;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_sign_up);

        userRepository = new UserRepository(this);
        shipperRepository = new ShipperRepository(this);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvLogin = findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(v -> navigateToShipperLogIn());
        btnConfirm.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() ||
                address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email không hợp lệ.");
            return;
        }

        if (!isValidVietnamesePhoneNumber(phoneNumber)) {
            showToast("Số điện thoại không hợp lệ.");
            return;
        }

        if (!isValidPassword(password)) {
            showToast("Mật khẩu phải có ít nhất 8 ký tự và bắt đầu bằng chữ cái viết hoa.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Mật khẩu xác nhận không khớp.");
            return;
        }

        if (userRepository.getUserByEmail(email) != null) {
            showToast("Email đã tồn tại.");
            return;
        }

        if (userRepository.getUserByPhone(phoneNumber) != null) {
            showToast("Số điện thoại đã tồn tại.");
            return;
        }

        // Tạo tài khoản shipper
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phoneNumber);
        user.setPassword(password);
        user.setUserType("Shipper");
        user.setCreatedAt(currentDateTime);
        user.setUpdatedAt(currentDateTime);

        userRepository.insert(user);

        // Lấy ID của user vừa tạo
        User insertedUser = userRepository.getAllUsers().get(userRepository.getAllUsers().size() - 1);
        int userId = insertedUser.getUserId();

        Shipper shipper = new Shipper();
        shipper.setUserId(userId);
        shipper.setStatus("Active");
        shipperRepository.insert(shipper);
        //abc
        showSuccessDialog();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Đăng ký thành công!")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    navigateToShipperLogIn();
                    finish();
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && Character.isUpperCase(password.charAt(0));
    }

    private boolean isValidVietnamesePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^0[0-9]{9,10}$");
    }

    private void navigateToShipperLogIn() {
        Intent intent = new Intent(this, ShipperLogin.class);
        startActivity(intent);
    }
}
