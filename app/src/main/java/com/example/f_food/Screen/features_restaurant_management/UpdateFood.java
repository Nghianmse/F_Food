package com.example.f_food.Screen.features_restaurant_management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.f_food.Adapter.CategoryAdapter;
import com.example.f_food.Entity.Category;
import com.example.f_food.Entity.Food;
import com.example.f_food.R;
import com.example.f_food.Repository.CategoryRepository;
import com.example.f_food.Repository.FoodRepository;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import android.widget.ImageButton;

public class UpdateFood extends AppCompatActivity {

    private EditText etFoodName, etFoodPrice, etFoodDescription;
    private Spinner spFoodCategory;
    private ImageView ivFoodImage;
    private RadioGroup rgStockStatus;
    private RadioButton rbInStock, rbOutOfStock;
    private Button btnUpdate;
    private FoodRepository foodRepository;
    private CategoryRepository categoryRepository;
    private int foodId;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private Category selectedCategory;

    @SuppressLint("WrongViewCast")
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

        // Ánh xạ UI
        ImageButton btnBack = findViewById(R.id.btnBack);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodPrice = findViewById(R.id.etFoodPrice);
        etFoodDescription = findViewById(R.id.etFoodDescription);
        spFoodCategory = findViewById(R.id.spFoodCategory);
        ivFoodImage = findViewById(R.id.ivFoodImage);
        rgStockStatus = findViewById(R.id.rgStockStatus);
        rbInStock = findViewById(R.id.rbInStock);
        rbOutOfStock = findViewById(R.id.rbOutOfStock);
        btnUpdate = findViewById(R.id.btnUpdate); // Ánh xạ nút Update

        // Khởi tạo Repository
        foodRepository = new FoodRepository(getApplicationContext());
        categoryRepository = new CategoryRepository(getApplicationContext());

        // Nhận foodId từ Intent
        foodId = getIntent().getIntExtra("food_id", -1);

        // Load danh sách danh mục
        loadCategoryList();

        // Nếu có foodId, load chi tiết món ăn
        if (foodId != -1) {
            loadFoodDetails(foodId);
        }

        // Lắng nghe sự kiện chọn danh mục
        spFoodCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryList.get(position); // Lưu danh mục được chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });

        // Sự kiện khi nhấn nút "Cập nhật"
        btnUpdate.setOnClickListener(v -> saveFoodDetails());
        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isUpdated", true); // Truyền trạng thái cập nhật
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }

    private void enableEditText(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
    }

    private void loadCategoryList() {
        new Thread(() -> {
            categoryList = categoryRepository.getAllCategories();

            runOnUiThread(() -> {
                categoryAdapter = new CategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList);
                spFoodCategory.setAdapter(categoryAdapter);
            });
        }).start();
    }

    private void loadFoodDetails(int foodId) {
        new Thread(() -> {
            Food food = foodRepository.getFoodById(foodId);
            if (food != null) {
                runOnUiThread(() -> {
                    // Hiển thị thông tin món ăn
                    etFoodName.setText(food.getName());
                    etFoodPrice.setText(String.valueOf(food.getPrice())); // Hiển thị số
                    etFoodDescription.setText(food.getDescription());

                    // Đặt danh mục của món ăn vào Spinner
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i).getCategoryId() == food.getCategoryId()) {
                            spFoodCategory.setSelection(i);
                            break;
                        }
                    }

                    // Hiển thị ảnh món ăn
                    if (food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
                        Picasso.get().load(food.getImageUrl()).resize(500, 500).centerCrop().into(ivFoodImage);
                    }

                    // Trạng thái tồn kho
                    if (food.getStockStatus().equalsIgnoreCase("Available")) {
                        rbInStock.setChecked(true);
                    } else {
                        rbOutOfStock.setChecked(true);
                    }
                });
            }
        }).start();
    }

    private void saveFoodDetails() {
        String name = etFoodName.getText().toString().trim();
        String priceText = etFoodPrice.getText().toString().trim();
        String description = etFoodDescription.getText().toString().trim();

        if (name.isEmpty() || priceText.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceText);

        // Lấy danh mục đã chọn
        int selectedCategoryIndex = spFoodCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(selectedCategoryIndex).getCategoryId();

        // Lấy trạng thái món ăn
        String stockStatus = rbInStock.isChecked() ? "Available" : "Out of Stock";

        // Cập nhật Food object
        Food food = new Food(foodId, name, description, price, categoryId, "", stockStatus);

        new Thread(() -> {
            foodRepository.update(food);
            runOnUiThread(() -> Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show());
        }).start();
    }
}
