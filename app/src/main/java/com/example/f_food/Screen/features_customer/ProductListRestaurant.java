package com.example.f_food.Screen.features_customer;

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

import com.example.f_food.Adapter.FoodListAdapter;
import com.example.f_food.Adapter.RestaurantListAdapter;
import com.example.f_food.Entity.Food;
import com.example.f_food.Entity.Restaurant;
import com.example.f_food.R;
import com.example.f_food.Repository.FoodRepository;
import com.example.f_food.Repository.RestaurantRepository;

import java.util.List;

public class ProductListRestaurant extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodListAdapter adapter;
    private List<Food> foodList;

    private FoodRepository foodRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list_restaurant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.foodListRestaurant), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ẩn thanh điều hướng (Navigation Bar)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        recyclerView = findViewById(R.id.recyclerViewListProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        init();
        adapter = new FoodListAdapter(foodList, new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Food food) {
                // Xử lý sự kiện click: Chuyển sang Activity mới
                Intent intent = new Intent(ProductListRestaurant.this, FoodDetailActivity.class);
                //intent.putExtra("restaurant", restaurant); // Truyền dữ liệu qua Intent
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        foodRepository = new FoodRepository(this);
        foodList = foodRepository.getFoodsByRestaurantId(1);
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