package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.R;

public class CustomerProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_profile);

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customerProfile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle Manage Address button
        Button btnManageAddress = findViewById(R.id.btnManageAddress);
        btnManageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerProfile.this, ManageAddress.class);
                startActivity(intent);
            }
        });

        // Handle icon events
        TextView trackingIcon = findViewById(R.id.trackingIcon);
        trackingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle tracking icon click
                Intent trackingIntent = new Intent(CustomerProfile.this, OrderTracking.class);
                startActivity(trackingIntent);
            }
        });

        TextView reviewIcon = findViewById(R.id.reviewIcon);
        reviewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle review icon click
                Intent reviewIntent = new Intent(CustomerProfile.this, ReviewAndRating.class);
                startActivity(reviewIntent);
            }
        });

        TextView historyIcon = findViewById(R.id.historyIcon);
        historyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle history icon click
                Intent historyIntent = new Intent(CustomerProfile.this, OrderHistory.class);
                startActivity(historyIntent);
            }
        });
    }
}
