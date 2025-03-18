package com.example.f_food.screen.features_restaurant_management;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.f_food.entity.Food;
import com.example.f_food.R;
import com.example.f_food.repository.FoodRepository;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.Locale;

public class UpdateFood extends AppCompatActivity {

    private TextView tvFoodName, tvFoodPrice, tvFoodDescription, tvFoodCategory;
    private ImageView ivFoodImage;
    private RadioGroup rgStockStatus;
    private RadioButton rbInStock, rbOutOfStock;
    private FoodRepository foodRepository;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.UpdateFoodActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần UI
        tvFoodName = findViewById(R.id.tvFoodName);
        tvFoodPrice = findViewById(R.id.tvFoodPrice);
        tvFoodDescription = findViewById(R.id.tvFoodDescription);
        tvFoodCategory = findViewById(R.id.rvFoodCategory);
        ivFoodImage = findViewById(R.id.ivFoodImage);
        rgStockStatus = findViewById(R.id.rgStockStatus);
        rbInStock = findViewById(R.id.rbInStock);
        rbOutOfStock = findViewById(R.id.rbOutOfStock);

        // Nhận foodId từ Intent
        foodId = getIntent().getIntExtra("food_id", -1);
        foodRepository = new FoodRepository(getApplicationContext());

        if (foodId != -1) {
            loadFoodDetails(foodId);
        }
    }

    private void loadFoodDetails(int foodId) {
        new Thread(() -> {
            Food food = foodRepository.getFoodById(foodId);
            if (food != null) {
                runOnUiThread(() -> {
                    tvFoodName.setText(food.getName());
                    tvFoodPrice.setText("$" + NumberFormat.getInstance(new Locale("vi", "VN")).format(food.getPrice()));
                    tvFoodDescription.setText(food.getDescription());
                    tvFoodCategory.setText(food.getCategoryId());

                    if (food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
                        Picasso.get().load(food.getImageUrl()).resize(500, 500).centerCrop().into(ivFoodImage);
                    }

                    if (food.getStockStatus().equalsIgnoreCase("Available")) {
                        rbInStock.setChecked(true);
                    } else {
                        rbOutOfStock.setChecked(true);
                    }
                });
            }
        }).start();
    }
}
