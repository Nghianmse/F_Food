package com.example.f_food.screen.features_restaurant_management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.f_food.entity.Food;
import com.example.f_food.R;
import com.example.f_food.repository.FoodRepository;
import java.text.NumberFormat;
import java.util.Locale;

public class ManageFoodDetail extends AppCompatActivity {

    private TextView tvFoodTitle, tvFoodPrice, tvFoodCategory, tvFoodDescription;
    private ImageView ivFoodImage;
    private Button btnUpdateFood;
    private FoodRepository foodRepository;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_food_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ManageFoodDetail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần UI
        tvFoodTitle = findViewById(R.id.tvFoodTitle);
        ivFoodImage = findViewById(R.id.ivFoodImage);
        tvFoodPrice = findViewById(R.id.tvFoodPrice);
        tvFoodCategory = findViewById(R.id.tvFoodCategory);
        tvFoodDescription = findViewById(R.id.tvFoodDescription);
        btnUpdateFood = findViewById(R.id.btnUpdateFood);

        // Lấy foodId từ Intent
        foodId = getIntent().getIntExtra("food_id", -1);
        foodRepository = new FoodRepository(getApplicationContext());

        if (foodId != -1) {
            loadFoodDetails(foodId);
        }

        // Xử lý sự kiện khi nhấn nút Update
        btnUpdateFood.setOnClickListener(v -> {
            Intent intent = new Intent(ManageFoodDetail.this, UpdateFood.class);
            intent.putExtra("food_id", foodId);
            startActivity(intent);
        });
    }

    private void loadFoodDetails(int foodId) {
        new Thread(() -> {
            Food food = foodRepository.getFoodById(foodId);
            if (food != null) {
                runOnUiThread(() -> {
                    tvFoodTitle.setText(food.getName());
                    tvFoodPrice.setText("Giá: " + NumberFormat.getInstance(new Locale("vi", "VN")).format(food.getPrice()) + " VNĐ");
                    tvFoodCategory.setText("Danh mục: " + food.getCategoryId());
                    tvFoodDescription.setText("Mô tả về món ăn:\n" + food.getDescription());

                    if (food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
                        com.squareup.picasso.Picasso.get().load(food.getImageUrl()).resize(500, 500).centerCrop().into(ivFoodImage);
                    }
                });
            }
        }).start();
    }
}
