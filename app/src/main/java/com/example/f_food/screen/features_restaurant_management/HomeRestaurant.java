package com.example.f_food.screen.features_restaurant_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.R;
import com.example.f_food.screen.authentication_authorization.LoginActivity;
import com.example.f_food.screen.features_customer.CustomerProfile;
import com.example.f_food.screen.features_customer.HomeStart;

public class HomeRestaurant extends AppCompatActivity {

    LinearLayout btnManageFood, btnRevenue, btnManageOrders, btnRestaurantInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_restaurant);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView menuIcon = findViewById(R.id.menuIcon);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        // Ánh xạ view
        btnManageFood = findViewById(R.id.btnManageFood);
        btnRevenue = findViewById(R.id.btnRevenue);
        btnManageOrders = findViewById(R.id.btnManageOrders);
        btnRestaurantInfo = findViewById(R.id.btnRestaurantInfo);

        // Xử lý sự kiện click
        btnManageFood.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeRestaurant.this, MenuManagement.class);
            Log.d("start food", "onCreate: ");
                    startActivity(intent);
                });

        btnRevenue.setOnClickListener(v ->
                startActivity(new Intent(HomeRestaurant.this, RestaurantSalesReport.class)));

        btnManageOrders.setOnClickListener(v ->
                startActivity(new Intent(HomeRestaurant.this, RestaurantOrders.class)));

        btnRestaurantInfo.setOnClickListener(v ->
                startActivity(new Intent(HomeRestaurant.this, CustomerProfile.class)));
    }
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.right_nav_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_profile) {
                Intent intent = new Intent(HomeRestaurant.this, CustomerProfile.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.action_logout) {
                Toast.makeText(HomeRestaurant.this, "Logging out...", Toast.LENGTH_SHORT).show();
                performLogout(); // Gọi hàm đăng xuất (nếu có)
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void performLogout() {

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Xóa dữ liệu đăng nhập
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
