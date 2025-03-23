package com.example.f_food.screen.features_restaurant_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.adapter.FoodListAdapter;
import com.example.f_food.entity.Food;
import com.example.f_food.R;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.RestaurantRepository;

import java.util.List;

public class MenuManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodListAdapter adapter;
    private FoodRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_management);
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        int uid = getLoggedInUserId();
        int rid = restaurantRepository.getRestaurantByUserId(uid).getRestaurantId();
        recyclerView = findViewById(R.id.recyclerViewFoods);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = new FoodRepository(this);
        // Lấy dữ liệu trực tiếp từ repository
        List<Food> foods = repository.getFoodsByRestaurantId(rid);

        adapter = new FoodListAdapter(this, foods, foodId -> {
            Intent intent = new Intent(MenuManagement.this, ManageFoodDetail.class);
            intent.putExtra("food_id", foodId);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1); // Trả về -1 nếu không tìm thấy userId
    }
}