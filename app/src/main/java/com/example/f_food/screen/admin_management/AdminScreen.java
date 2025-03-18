package com.example.f_food.screen.admin_management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.R;
import com.squareup.picasso.Picasso;

public class AdminScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_screen);

        // Apply window insets to avoid overlapping UI elements with system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminscreen_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize LinearLayouts
        LinearLayout layoutShipperManagement = findViewById(R.id.layout_ShipperManagement);
        LinearLayout layoutCustomerManagement = findViewById(R.id.layout_CustomerManagement);
        LinearLayout layoutPolicyManagement = findViewById(R.id.layout_PolicyManagement);
        LinearLayout layoutRestaurantManagement = findViewById(R.id.layout_RestaurantManagement);
        LinearLayout layoutPaymentManagement = findViewById(R.id.layout_PaymentManagement);
        //  LinearLayout layoutOrderManagement = findViewById(R.id.layout_PaymentManagement);


        // Load images using Picasso for dynamic image loading
        ImageView logoImageView = findViewById(R.id.logo);
        Picasso.get().load(R.drawable.login).resize(500, 500)
                .centerCrop().into(logoImageView); // Replace with your actual image URL

        // Set click listeners for each LinearLayout
        layoutShipperManagement.setOnClickListener(v -> {
            Log.d("AdminScreen", "Shipper Management clicked");
            Intent intent = new Intent(AdminScreen.this, Shipper_Management.class);
            startActivity(intent);
        });

        layoutCustomerManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminScreen.this, Customer_Management.class);
            startActivity(intent);
        });

        layoutPolicyManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminScreen.this, Policy_Management.class);
            startActivity(intent);
        });

        layoutRestaurantManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminScreen.this, Restaurant_Management.class);
            startActivity(intent);
        });
        layoutPaymentManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminScreen.this, Payment_Transactions_Management.class);
            startActivity(intent);
        });
        layoutPaymentManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminScreen.this, Payment_Transactions_Management.class);
            startActivity(intent);
        });
        //   layoutOrderManagement.setOnClickListener(v -> {
        //         Intent intent = new Intent(AdminScreen.this, Payment_Transactions_Management.class);
        //         startActivity(intent);
        //      });

    }
}
