package com.example.f_food.screen.features_customer;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.R;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.User;

public class ChangePassword extends AppCompatActivity {

    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnConfirmChange;
    private TextView tvEmail;
    private RestaurantRoomDatabase db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Ánh xạ view
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnConfirmChange = findViewById(R.id.btnConfirmChange);
        tvEmail = findViewById(R.id.tvEmail);

        // DB & User
        db = RestaurantRoomDatabase.getInstance(this);
        int userId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("USER_ID", -1);
        user = db.userDAO().getUserById(1);

        if (user != null) {
            tvEmail.setText(user.getEmail());
        }

        // Xử lý đổi mật khẩu
        btnConfirmChange.setOnClickListener(v -> {
            String oldPass = edtOldPassword.getText().toString().trim();
            String newPass = edtNewPassword.getText().toString().trim();
            String confirmPass = edtConfirmPassword.getText().toString().trim();

            // Kiểm tra bỏ trống
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu cũ đúng không
            if (!oldPass.equals(user.getPassword())) {
                Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra khớp mật khẩu mới
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật mật khẩu
            user.setPassword(newPass);
            db.userDAO().update(user);

            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

            // Kết thúc hoặc quay lại màn hình trước
            finish();
        });
        setupPasswordToggle(findViewById(R.id.edtOldPassword), findViewById(R.id.imgToggleOldPassword));
        setupPasswordToggle(findViewById(R.id.edtNewPassword), findViewById(R.id.imgToggleNewPassword));
        setupPasswordToggle(findViewById(R.id.edtConfirmPassword), findViewById(R.id.imgToggleConfirmPassword));

        // Nút back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
    private void setupPasswordToggle(EditText editText, ImageView toggleIcon) {
        final boolean[] isVisible = {false};
        toggleIcon.setOnClickListener(v -> {
            if (isVisible[0]) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleIcon.setImageResource(R.drawable.icon_eye_close);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleIcon.setImageResource(R.drawable.icon_eye_close);
            }
            editText.setSelection(editText.getText().length());
            isVisible[0] = !isVisible[0];
        });
    }

}
