package com.example.f_food.screen.features_restaurant_management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.R;
import com.example.f_food.screen.features_customer.CustomerProfile;

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
}
