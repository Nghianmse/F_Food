package com.example.f_food.Screen.features_customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Adapter.RestaurantListAdapter;
import com.example.f_food.Entity.Restaurant;
import com.example.f_food.R;
import com.example.f_food.Repository.RestaurantRepository;

import java.util.List;

public class ViewRestaurantList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private List<Restaurant> restaurantList;

    private RestaurantRepository restaurantRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_restaurant_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewRestaurantList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        init();
        adapter = new RestaurantListAdapter(restaurantList, new RestaurantListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Restaurant restaurant) {
                // Xử lý sự kiện click: Chuyển sang Activity mới
                Intent intent = new Intent(ViewRestaurantList.this, FoodDetailActivity.class);
                //intent.putExtra("restaurant", restaurant); // Truyền dữ liệu qua Intent
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        restaurantRepository = new RestaurantRepository(this);
        restaurantList = restaurantRepository.getAllRestaurants();
    }
}