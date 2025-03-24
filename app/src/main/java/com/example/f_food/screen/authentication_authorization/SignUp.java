package com.example.f_food.screen.authentication_authorization;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.UserRepository;
import com.squareup.picasso.Picasso;

public class SignUp extends AppCompatActivity {
    private EditText fullName, email, phone, password, confirmPassword;
    private Button btnSignUp, signIn;
    private UserRepository userRepository;
    private ImageView imgLogoSignup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fullName = findViewById(R.id.txtSignup_fullname);
        email = findViewById(R.id.txtSignup_Email);
        phone = findViewById(R.id.txtSignup_Phone);
        password = findViewById(R.id.txtSignup_Password);
        confirmPassword = findViewById(R.id.txtSignup_ConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.btn_signup_login);
        imgLogoSignup = findViewById(R.id.imgLogoSignup);

        Picasso.get()
                .load(R.drawable.login)
                .resize(500, 500)
                .centerCrop()
                .into(imgLogoSignup);

        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        String name = fullName.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        userRepository = new UserRepository(this);

        if (name.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            showToast("Please fill all fields.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            showToast("Invalid email format.");
            return;
        }

        if (!isValidVietnamesePhoneNumber(phoneText)) {
            showToast("Invalid Vietnamese phone number.");
            return;
        }

        if (!isValidPassword(pass)) {
            showToast("Password must be at least 8 characters long and start with an uppercase letter.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showToast("Passwords do not match.");
            return;
        }

        if (userRepository.getUserByEmail(emailText) != null) {
            showToast("Email already exists.");
            return;
        }

        if (userRepository.getUserByPhone(phoneText) != null) {
            showToast("Phone number already exists.");
            return;
        }

        User newUser = new User(0, name, emailText, pass, phoneText, "Customer", "2024-03-14 12:00:00", "2024-03-14 12:00:00", false);
        userRepository.insert(newUser);

        showSuccessDialog();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setMessage("Registration successful")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    startActivity(new Intent(SignUp.this, LoginActivity.class));
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
}
