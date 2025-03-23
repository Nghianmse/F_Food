package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.adapter.FoodListAdapter;
import com.example.f_food.adapter.HomeCustomerAdapter;
import com.example.f_food.entity.Category;
import com.example.f_food.entity.Food;
import com.example.f_food.repository.CategoryRepository;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeStart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeCustomerAdapter adapter;
    private List<Food> foodList;

    TextView fullname;

    private FoodRepository foodRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.HomeStart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hideSystemUI(); // Ẩn ngay khi khởi chạy

        // Đảm bảo hệ thống vẫn ẩn khi người dùng chạm vào màn hình
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                visibility -> hideSystemUI()
        );

        fullname = findViewById(R.id.AccountLogin);
        Intent intent = getIntent();
        String fullname1 = intent.getStringExtra("fullName");
        fullname.setText(fullname1);

        EditText search_bar = findViewById(R.id.search_bar); // Ánh xạ search_bar từ layout

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    searchFoodByName(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        init();
        adapter = new HomeCustomerAdapter(this, foodList, new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int foodId) {
                Intent intent = new Intent(HomeStart.this, FoodDetailActivity.class);
                intent.putExtra("foodId", foodId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        CategoryRepository categoryRepository = new CategoryRepository(this);
        LinearLayout categoryContainer = findViewById(R.id.categoryContainer);
        List<Category> categoryList = categoryRepository.getAllCategories();
        List<String> categories = new ArrayList<>();
        categories.add("All");
        for (Category category : categoryList) {
            categories.add(category.getName());
        }

// Kiểm tra nếu danh sách rỗng
        if (categories == null || categories.isEmpty()) {
            categories = Arrays.asList("All Coffee", "Macchiato", "Latte", "Espresso", "Cappuccino"); // Dữ liệu mặc định
        }

        for (String category : categories) {
            Button button = new Button(this);
            button.setText(category);
            button.setPadding(20, 10, 20, 10);
            button.setTextColor(ContextCompat.getColor(this, R.color.brown));

            // Đặt màu nền cho nút "All Coffee" khác biệt
            if (category.equals("All Coffee")) {
                button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.brown)));
                button.setTextColor(Color.WHITE);
            } else {
                button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));
            }

            // Xử lý sự kiện click
            button.setOnClickListener(v -> {
                Toast.makeText(this, "Chọn: " + category, Toast.LENGTH_SHORT).show();
            });

            // Tạo LayoutParams để có khoảng cách giữa các Button
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 0, 0);  // Khoảng cách giữa các Button
            button.setLayoutParams(params);

            categoryContainer.addView(button);  // Thêm Button vào LinearLayout
            button.setOnClickListener(v -> {
                // Lấy danh sách thực phẩm theo danh mục được chọn
                if (category.equals("All")) {
                    foodList = foodRepository.getAllFoods();
                } else {
                    int id = categoryRepository.getCategoryByName(category).getCategoryId();
                    foodList = foodRepository.getFoodsByCategoryId(id);
                }

                // Cập nhật dữ liệu cho adapter
                adapter.updateData(foodList);
            });

        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent1 = null;

            if (itemId == R.id.nav_home) {
                intent1 = new Intent(this, HomeStart.class); // Màn hình Trang chủ
            } else if (itemId == R.id.nav_bag) {
                intent1 = new Intent(this, OrderHistory.class); // Màn hình Đơn hàng
            } else if (itemId == R.id.nav_orders) {
                intent1 = new Intent(this, ViewRestaurantList.class); // Màn hình Nhà hàng
            } else if (itemId == R.id.nav_profile) {
                intent1 = new Intent(this, activity_cart.class); // Màn hình Thông báo
            }
            if (intent1 != null) {
                startActivity(intent1);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Hiệu ứng chuyển màn
            }
            return true;
        });

    }
    private void init() {
        Intent intent = getIntent();
        foodRepository = new FoodRepository(this);
        foodList = foodRepository.getAllFoods();
    }

    private void searchFoodByName(String query) {
        List<Food> filteredList;

        if (query.isEmpty()) {
            filteredList = foodRepository.getAllFoods(); // Nếu trống thì lấy toàn bộ
        } else {
            filteredList = foodRepository.getFoodsByName(query);
        }

        adapter.updateData(filteredList);
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }


}