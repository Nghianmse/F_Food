package com.example.f_food.screen.features_restaurant_management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.f_food.R;
import com.example.f_food.adapter.CategoryAdapter;
import com.example.f_food.entity.Category;
import com.example.f_food.entity.Food;
import com.example.f_food.repository.CategoryRepository;
import com.example.f_food.repository.FoodRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UpdateFood extends AppCompatActivity {

    private EditText etFoodName, etFoodPrice, etFoodDescription;
    private Spinner spFoodCategory;
    private ImageView ivFoodImage;
    private RadioButton rbInStock, rbOutOfStock;
    private Button btnUpdate;
    private ImageButton btnBack;

    private FoodRepository foodRepository;
    private CategoryRepository categoryRepository;

    private int foodId;
    private String imageUrl = "";
    private List<Category> categoryList;
    private Category selectedCategory;

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

        // Ánh xạ
        etFoodName = findViewById(R.id.etFoodName);
        etFoodPrice = findViewById(R.id.etFoodPrice);
        etFoodDescription = findViewById(R.id.etFoodDescription);
        spFoodCategory = findViewById(R.id.spFoodCategory);
        ivFoodImage = findViewById(R.id.ivFoodImage);
        rbInStock = findViewById(R.id.rbInStock);
        rbOutOfStock = findViewById(R.id.rbOutOfStock);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        foodId = getIntent().getIntExtra("food_id", -1);
        // Repository
        foodRepository = new FoodRepository(getApplicationContext());
        categoryRepository = new CategoryRepository(getApplicationContext());


        // Load danh mục
        loadCategoryList();

        // Back
        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isUpdated", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Cập nhật món ăn
        btnUpdate.setOnClickListener(v -> saveFoodDetails());

        // Spinner danh mục
        spFoodCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryList.get(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });
    }

    private void loadCategoryList() {
            categoryList = categoryRepository.getAllCategories();
                CategoryAdapter adapter = new CategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList);
                spFoodCategory.setAdapter(adapter);

                // Sau khi có category, load món ăn
                if (foodId != -1) loadFoodDetails(foodId);
                if (foodId != -1) loadFoodDetails(foodId);
    }

    private void loadFoodDetails(int id) {
        new Thread(() -> {
            Food food = foodRepository.getFoodById(id);
            if (food != null) {
                imageUrl = food.getImageUrl(); // lưu lại URL cũ

                runOnUiThread(() -> {
                    etFoodName.setText(food.getName());
                    etFoodPrice.setText(String.valueOf(food.getPrice()));
                    etFoodDescription.setText(food.getDescription());

                    // chọn danh mục
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i).getCategoryId() == food.getCategoryId()) {
                            spFoodCategory.setSelection(i);
                            break;
                        }
                    }

                    // load ảnh
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get().load(imageUrl).resize(500, 500).centerCrop().into(ivFoodImage);
                    }

                    // trạng thái
                    if ("Available".equalsIgnoreCase(food.getStockStatus())) {
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

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = selectedCategory != null ? selectedCategory.getCategoryId() : -1;
        if (categoryId == -1) {
            Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        String stockStatus = rbInStock.isChecked() ? "Available" : "Out of Stock";

        // Cập nhật món ăn

        foodRepository.updateFoodInfo(foodId,name,price,description,categoryId,stockStatus);
            runOnUiThread(() -> {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateFood.this, ManageFoodDetail.class);
                intent.putExtra("food_id", foodId);
                startActivity(intent);
            });
        };
    }
