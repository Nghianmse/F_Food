package com.example.f_food.screen.authentication_authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.R;
import com.example.f_food.repository.UserRepository;
import com.example.f_food.entity.User;
import com.google.firebase.functions.FirebaseFunctions;

import javax.mail.MessagingException;

public class ForgotPassActivity extends AppCompatActivity {
    private static final long TIME_LIMIT = 2 * 60 * 1000; // 2 phút (120000 milliseconds)
    private SharedPreferences sharedPreferences;
    private EditText emailInput;
    private Button sendButton;
    private UserRepository userRepository;
    private FirebaseFunctions functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Quên mật khẩu");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        userRepository = new UserRepository(this);
        functions = FirebaseFunctions.getInstance();

        emailInput = findViewById(R.id.emailInput);
        sendButton = findViewById(R.id.sendButton);
        sharedPreferences = getSharedPreferences("ForgotPassPrefs", MODE_PRIVATE);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();

                if (email.isEmpty()) {
                    showAlertDialog("Lỗi", "Vui lòng nhập email");
                } else if (!isValidEmail(email)) {
                    showAlertDialog("Lỗi", "Vui lòng nhập email hợp lệ");
                } else {
                    long lastSentTime = sharedPreferences.getLong("lastSentTime", 0);
                    long currentTime = System.currentTimeMillis();

                    if ((currentTime - lastSentTime) < TIME_LIMIT) {
                        showAlertDialog("Lỗi", "Vui lòng chờ trước khi yêu cầu mật khẩu mới.");
                    } else {
                        User user = userRepository.getUserByEmail(email);
                        if (user != null) {
                            String newPassword = generateRandomPassword(10);
                            user.setPassword(newPassword);
                            userRepository.update(user);
                            sendMail(email, newPassword);

                            // Cập nhật thời gian gửi vào SharedPreferences
                            sharedPreferences.edit().putLong("lastSentTime", currentTime).apply();
                        } else {
                            showAlertDialog("Lỗi", "Email không tồn tại");
                        }
                    }
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim().toLowerCase()).matches();
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#&!";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    private void sendMail(String email, String newPassword) {
        new Thread(() -> {
            try {
                GMailSender sender = new GMailSender();
                String subject = "Cấp lại mật khẩu";
                String body = "Chào bạn,\n\n"
                        + "Chúng tôi đã nhận được yêu cầu cấp lại mật khẩu của bạn. Dưới đây là mật khẩu mới:\n\n"
                        + "**" + newPassword + "**\n\n"
                        + "Vì lý do bảo mật, vui lòng đăng nhập và đổi mật khẩu ngay lập tức.\n\n"
                        + "Nếu bạn không yêu cầu thay đổi này, vui lòng liên hệ với bộ phận hỗ trợ của chúng tôi.\n\n"
                        + "Trân trọng,\n"
                        + "Đội ngũ hỗ trợ F-Food";

                sender.sendEmail(email, subject, body);

                runOnUiThread(() -> showAlertDialog("Thành công", "Mật khẩu mới đã được gửi đến " + email));
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> showAlertDialog("Lỗi", "Gửi email thất bại: " + e.getMessage()));
            }
        }).start();
    }

    private void showAlertDialog(String title, String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
