package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.AddressAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.dao.AddressWithUser;
import com.example.f_food.screen.authentication_authorization.LoginActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Address extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private List<AddressWithUser> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        recyclerView = findViewById(R.id.address);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve userId from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1);  // Default value is -1 if userId is not found

        if (userId == -1) {
            showAlertDialog("Bạn chưa đăng nhập, vui lòng đăng nhập để thao tác.");
            return;  // Exit if no userId is found
        }

        // Use Executor to run database operations on a background thread
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Fetch the addresses in the background thread
            addressList = RestaurantRoomDatabase.getInstance(this).addressDAO().getAddressesByUserId(userId);

            // Run UI updates on the main thread after fetching data
            runOnUiThread(() -> {
                // Check if any addresses are found
                if (addressList != null && !addressList.isEmpty()) {
                    addressAdapter = new AddressAdapter(this, addressList);
                    recyclerView.setAdapter(addressAdapter);
                } else {
                    Toast.makeText(Address.this, "No addresses found", Toast.LENGTH_SHORT).show();
                }
            });
        });
        Button btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(Address.this, ManageAddress.class);
            startActivity(intent);
        });
        // Action when the Home icon is clicked
        ImageButton homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Address.this, HomeStart.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clear the back stack
            startActivity(intent);
            finish();
        });

    }
    private void showAlertDialog(String message) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(Address.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc Activity hiện tại
                })
                .create()
                .show();
    }
}
