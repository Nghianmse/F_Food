package com.example.f_food.Screen.admin_management;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.Adapter.RestaurantManagementListAdapter;
import com.example.f_food.Entity.Restaurant;
import com.example.f_food.R;
import com.example.f_food.Repository.RestaurantRepository;
import java.util.ArrayList;
import java.util.List;

public class Restaurant_Management extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RestaurantManagementListAdapter adapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_management);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurantManagementActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewListRestaurant);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize repository and data
        init();

        // Set adapter after initializing data
        adapter = new RestaurantManagementListAdapter(restaurantList);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        restaurantList = restaurantRepository.getAllRestaurants();

        if (restaurantList == null) {
            restaurantList = new ArrayList<>(); // Prevent NullPointerException
        }


    }
}
