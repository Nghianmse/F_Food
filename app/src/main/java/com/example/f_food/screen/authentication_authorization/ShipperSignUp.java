package com.example.f_food.screen.authentication_authorization;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.entity.Restaurant;
import com.example.f_food.entity.Shipper;
import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.repository.ShipperRepository;
import com.example.f_food.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShipperSignUp extends AppCompatActivity {
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    private ShipperRepository shipperRepository;

    private EditText etFullName, etEmail, etPhoneNumber, etAddress, etPassword, etConfirmPassword;
    private Button btnConfirm;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_sign_up);

        // Initialize repositories
        userRepository = new UserRepository(this);
        restaurantRepository = new RestaurantRepository(this);
        shipperRepository = new ShipperRepository(this);
        // Initialize UI components
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
        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String address = etAddress.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Basic validation
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(password) || !password.equals(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email or phone number already exists
        if (userRepository.getUserByEmail(email) != null) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userRepository.getUserByPhone(phoneNumber) != null) {
            Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and insert user
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phoneNumber);
        user.setPassword(password);
        user.setUserType("Shipper");
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        user.setCreatedAt(currentDateTime);
        user.setUpdatedAt(currentDateTime);

        userRepository.insert(user);

        // Get the userId of the newly inserted user (assuming last inserted)
        User insertedUser = userRepository.getAllUsers().get(userRepository.getAllUsers().size() - 1);
        int userId = insertedUser.getUserId();

        // Create and insert restaurant
        Shipper shipper = new Shipper();

        shipper.setUserId(userId);

        shipper.setStatus("Active");


        shipperRepository.insert(shipper);

        // Provide feedback to the user
        Toast.makeText(this, "Shipper registered successfully", Toast.LENGTH_SHORT).show();
        // Optionally, navigate back or to another screen
        navigateToShipperLogIn();


    }
    private void navigateToShipperLogIn() {
        Intent intent = new Intent(this, ShipperLogin.class);  // Assuming RestaurantLogInActivity is your target activity
        startActivity(intent);
    }
}