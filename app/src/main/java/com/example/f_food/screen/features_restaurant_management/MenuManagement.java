package com.example.f_food.screen.features_restaurant_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.adapter.FoodListAdapter;
import com.example.f_food.entity.Food;
import com.example.f_food.R;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.screen.authentication_authorization.LoginActivity;
import com.example.f_food.screen.features_customer.CustomerProfile;
import com.example.f_food.screen.features_customer.HomeStart;

import java.util.List;

public class MenuManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodListAdapter adapter;
    private FoodRepository repository;

    Button btnAddFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_management);
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        int uid = getLoggedInUserId();
        int rid = restaurantRepository.getRestaurantByUserId(uid).getRestaurantId();
        recyclerView = findViewById(R.id.recyclerViewFoods);

        btnAddFood = findViewById(R.id.btnAddFood);
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

        btnAddFood.setOnClickListener(v->{
            Intent intent = new Intent(MenuManagement.this, AddFoodActivity.class);
            startActivity(intent);
        });
    }
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1); // Trả về -1 nếu không tìm thấy userId
    }
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.right_nav_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_profile) {
                Intent intent = new Intent(MenuManagement.this, CustomerProfile.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.action_logout) {
                Toast.makeText(MenuManagement.this, "Logging out...", Toast.LENGTH_SHORT).show();
                performLogout(); // Gọi hàm đăng xuất (nếu có)
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void performLogout() {

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Xóa dữ liệu đăng nhập
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}