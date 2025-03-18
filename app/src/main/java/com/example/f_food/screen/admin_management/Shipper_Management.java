package com.example.f_food.screen.admin_management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.ShipperManagementAdapter;
import com.example.f_food.entity.Shipper;
import com.example.f_food.R;
import com.example.f_food.repository.ShipperRepository;

import java.util.List;

public class Shipper_Management extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShipperManagementAdapter shipperManagementAdapter;
    private ShipperRepository shipperRepository;
    private List<Shipper> shipperList;
    private ImageView back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shipper_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_ShipperManagement), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khởi tạo repository
        shipperRepository = new ShipperRepository(this);

        // Lấy danh sách shipper từ SQLite
        shipperList = shipperRepository.getAllShippers();
        back=findViewById(R.id.btnBack_Shipper_Management);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Shipper_Management.this, AdminScreen.class);
            startActivity(intent);
            finish(); // Để đóng màn hiện tại nếu không cần quay lại
        });

        // Ánh xạ RecyclerView
        recyclerView = findViewById(R.id.recyclerViewShipperManagement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Kiểm tra danh sách shipper có rỗng không
        if (shipperList.isEmpty()) {
            Toast.makeText(this, "No shippers available", Toast.LENGTH_SHORT).show();
        } else {
            // Khởi tạo Adapter và gán vào RecyclerView
            shipperManagementAdapter = new ShipperManagementAdapter(this, shipperList);
            recyclerView.setAdapter(shipperManagementAdapter);
        }

    }
}