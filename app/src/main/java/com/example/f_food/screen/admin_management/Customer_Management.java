package com.example.f_food.screen.admin_management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.CustomerManagementAdapter;
import com.example.f_food.entity.User;
import com.example.f_food.R;
import com.example.f_food.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class Customer_Management extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomerManagementAdapter adapter;
    private List<User> customerList;
    private UserRepository userRepository;
    private ImageView back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_management);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customer_management_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back=findViewById(R.id.btnBack_Customer_Management);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Customer_Management.this, AdminScreen.class);
            startActivity(intent);
            finish(); // Để đóng màn hiện tại nếu không cần quay lại
        });

        recyclerView = findViewById(R.id.recyclerViewCustomerManagement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize repository and data
        userRepository = new UserRepository(this);
        customerList = userRepository.getAllUsers();

        if (customerList == null) {
            customerList = new ArrayList<>(); // Prevent NullPointerException
        }

        // Set adapter with context
        adapter = new CustomerManagementAdapter(this, customerList);
        recyclerView.setAdapter(adapter);
    }
}
