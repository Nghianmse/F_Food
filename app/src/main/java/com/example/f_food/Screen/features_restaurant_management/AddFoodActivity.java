package com.example.f_food.Screen.features_restaurant_management;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.Entity.Category;
import com.example.f_food.Entity.Food;
import com.example.f_food.R;
import com.example.f_food.Repository.CategoryRepository;
import com.example.f_food.Repository.FoodRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFoodActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    private EditText edtFoodName, edtFoodPrice, edtFoodDescription;
    private Uri imageUri;

    private ImageView imgFoodPreview;
    private Spinner spinnerStockStatus;
    private Button btnSelectImage;
    private Button btnAddFood;
    private List<Category> categoryList;

    private List<String> spinnerStockStatuslist;

    private CategoryRepository categoryRepository;
    private int selectedCategoryId = -1;

    private String StockStatus = "Available";

    private FoodRepository foodRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Ánh xạ UI
        spinnerCategory = findViewById(R.id.spinnerCategory);
        edtFoodName = findViewById(R.id.edtFoodName);
        edtFoodPrice = findViewById(R.id.edtFoodPrice);
        edtFoodDescription = findViewById(R.id.edtFoodDescription);
        spinnerStockStatus = findViewById(R.id.spinnerStockStatus);
        btnAddFood = findViewById(R.id.btnAddFood);
        imgFoodPreview = findViewById(R.id.imgFoodPreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        loadCategories();
        loadStockStatus();
        btnSelectImage.setOnClickListener(view -> openGallery());
        btnAddFood.setOnClickListener(v -> {
            // Lấy dữ liệu từ EditText
            String foodName = edtFoodName.getText().toString().trim();
            String foodPriceStr = edtFoodPrice.getText().toString().trim();
            String foodDescription = edtFoodDescription.getText().toString().trim();

            // Kiểm tra dữ liệu có bị trống không
            if (foodName.isEmpty() || foodPriceStr.isEmpty() || foodDescription.isEmpty() || imgFoodPreview == null) {
                Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            double foodPrice = Double.parseDouble(foodPriceStr);

            // Chuyển đổi Uri ảnh thành String
            String imageUriString = imageUri.toString();

            // Tạo đối tượng Food
            Food newFood = new Food();
            newFood.setRestaurantId(1);
            newFood.setName(foodName);
            newFood.setPrice(foodPrice);
            newFood.setDescription(foodDescription);
            newFood.setCategoryId(selectedCategoryId);
            newFood.setStockStatus(StockStatus);
            newFood.setImageUrl(imageUriString); // Lưu URI ảnh dưới dạng String
            foodRepository = new FoodRepository(this);
            // Lưu vào Room Database trong luồng phụ
            new Thread(() -> {

                foodRepository.insert(newFood);

                // Cập nhật giao diện trong luồng chính
                runOnUiThread(() -> {
                    Toast.makeText(this, "Food added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng Activity sau khi thêm xong
                });
            }).start();
        });

    }

    private void loadCategories() {
        new Thread(() -> {
            categoryRepository = new CategoryRepository(this);
            categoryList = categoryRepository.getAllCategories();

            runOnUiThread(() -> {
                String[] categoryNames = new String[categoryList.size()];
                for (int i = 0; i < categoryList.size(); i++) {
                    categoryNames[i] = categoryList.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
                spinnerCategory.setAdapter(adapter);

                spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        selectedCategoryId = categoryList.get(position).getCategoryId(); // Lưu ID của Category được chọn
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                });
            });
        }).start();
    }

    private void loadStockStatus() {
        // Khởi tạo danh sách và thêm giá trị
        spinnerStockStatuslist = new ArrayList<>();
        spinnerStockStatuslist.add("Available");
        spinnerStockStatuslist.add("Out of Stock");

        // Tạo Adapter và gán vào Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerStockStatuslist);
        spinnerStockStatus.setAdapter(adapter);

        spinnerStockStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                StockStatus = spinnerStockStatuslist.get(position); // Lưu trạng thái được chọn
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imgFoodPreview.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi khi chọn ảnh!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); // Dùng ACTION_OPEN_DOCUMENT
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE); // Đảm bảo chỉ chọn file mở được
        imagePickerLauncher.launch(intent);
    }
}