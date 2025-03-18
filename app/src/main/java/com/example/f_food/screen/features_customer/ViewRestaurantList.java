package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.RestaurantListAdapter;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.R;
import com.example.f_food.repository.RestaurantRepository;

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
        // Ẩn thanh điều hướng (Navigation Bar)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        recyclerView = findViewById(R.id.recyclerViewListRestaurant);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        init();
        adapter = new RestaurantListAdapter(restaurantList, new RestaurantListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                // Xử lý sự kiện click: Chuyển sang Activity mới
                Intent intent = new Intent(ViewRestaurantList.this, ProductListRestaurant.class);
                intent.putExtra("restaurantId", id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        restaurantRepository = new RestaurantRepository(this);
        restaurantList = restaurantRepository.getAllRestaurants();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

}