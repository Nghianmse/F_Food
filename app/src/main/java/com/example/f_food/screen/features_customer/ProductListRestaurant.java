package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.FoodListAdapter;
import com.example.f_food.entity.Food;
import com.example.f_food.R;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListRestaurant extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantRepository restaurantRepository;
    private FoodListAdapter adapter;
    private List<Food> foodList;

    private FoodRepository foodRepository;

    private ImageView headerImage;

    private TextView tvRestaurantName;


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
        adapter = new FoodListAdapter(this, foodList, new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int foodId) {
                Intent intent = new Intent(ProductListRestaurant.this, FoodDetailActivity.class);
                intent.putExtra("foodId", foodId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        Intent intent = getIntent();
        foodRepository = new FoodRepository(this);
        restaurantRepository = new RestaurantRepository(this);
        int idRestaurantIntent = intent.getIntExtra("restaurantId", -1);
        var restaurant = restaurantRepository.getRestaurantById(idRestaurantIntent);
        foodList = foodRepository.getFoodsByRestaurantId(idRestaurantIntent);
        headerImage = findViewById(R.id.headerImage);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvRestaurantName.setText(restaurant.getName());
        if(restaurant.getImage() != null && !restaurant.getImage().isEmpty()) {
            Picasso.get()
                    .load(restaurant.getImage())
                    .resize(500, 500)
                    .centerCrop()
                    .into(headerImage);
        }
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