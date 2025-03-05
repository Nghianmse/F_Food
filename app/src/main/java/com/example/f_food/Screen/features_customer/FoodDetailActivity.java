package com.example.f_food.Screen.features_customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.Entity.Food;
import com.example.f_food.R;
import com.example.f_food.Repository.FoodRepository;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.Locale;
public class FoodDetailActivity extends AppCompatActivity {

    private FoodRepository foodRepository;

    TextView dishName;
    TextView price;

    TextView description;

    ImageView dishImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.foodDetailActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        init();
    }

    private void init() {
        dishName = findViewById(R.id.dishName);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        dishImage = findViewById(R.id.dishImage);
        Intent intent = getIntent();
        foodRepository = new FoodRepository(this);
        Food food = foodRepository.getFoodById(intent.getIntExtra("foodId", -1));
        dishName.setText(food.getName());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        price.setText("Giá tiền: " + formatter.format(food.getPrice()) + " VNĐ");
        description.setText(food.getDescription());
        if(food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(food.getImageUrl())
                    .resize(500, 500)
                    .centerCrop()
                    .into(dishImage);
        }
    }
}