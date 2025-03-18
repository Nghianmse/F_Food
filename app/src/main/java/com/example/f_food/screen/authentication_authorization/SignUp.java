package com.example.f_food.screen.authentication_authorization;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.UserRepository;

public class SignUp extends AppCompatActivity {
    private EditText fullName, email, phone, password, confirmPassword;
    private Button btnSignUp;
    private UserRepository userRepository;
    private TextView txtLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
        txtLogin = findViewById(R.id.txtFromSignUptoSignin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String name = fullName.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        userRepository = new UserRepository(this);

        if (name.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem email hoặc số điện thoại đã tồn tại chưa
        if (userRepository.getUserByEmail(emailText) != null) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userRepository.getUserByPhone(phoneText) != null) {
            Toast.makeText(this, "Phone number already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng User mới và thêm vào cơ sở dữ liệu
        User newUser = new User(0, name, emailText, pass, phoneText, "Customer", "2024-03-14 12:00:00", "2024-03-14 12:00:00");
        userRepository.insert(newUser);

        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

        // Chuyển sang màn hình đăng nhập
       // startActivity(new Intent(Register.this, LoginActivity.class));
       // finish();
    }

}
