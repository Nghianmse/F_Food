package com.example.f_food.screen.features_restaurant_management;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.f_food.entity.Category;
import com.example.f_food.entity.Food;
import com.example.f_food.R;
import com.example.f_food.repository.CategoryRepository;
import com.example.f_food.repository.FoodRepository;
import com.example.f_food.repository.RestaurantRepository;

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
            if (foodName.isEmpty() || foodPriceStr.isEmpty() || foodDescription.isEmpty() || imageUri == null) {
                showAlert("Lưu ý", "Vui lòng điền đầy đủ thông tin và chọn ảnh!");
                return;
            }

            double foodPrice;
            try {
                foodPrice = Double.parseDouble(foodPriceStr);
                if (foodPrice <= 0) {
                    showAlert("Lưu ý", "Giá sản phẩm phải lớn hơn 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lưu ý", "Giá sản phẩm không hợp lệ!");
                return;
            }

            if (selectedCategoryId == -1) {
                showAlert("Lưu ý", "Vui lòng chọn danh mục!");
                return;
            }

            if (!spinnerStockStatuslist.contains(StockStatus)) {
                showAlert("Lưu ý", "Trạng thái sản phẩm không hợp lệ!");
                return;
            }

            String imageUriString = imageUri.toString();

            RestaurantRepository restaurantRepository = new RestaurantRepository(this);

            int rid = restaurantRepository.getRestaurantByUserId(getLoggedInUserId()).getRestaurantId();

            // Tạo đối tượng Food
            Food newFood = new Food();
            newFood.setRestaurantId(rid);
            newFood.setName(foodName);
            newFood.setPrice(foodPrice);
            newFood.setDescription(foodDescription);
            newFood.setCategoryId(selectedCategoryId);
            newFood.setStockStatus(StockStatus);
            newFood.setImageUrl(imageUriString);

            foodRepository = new FoodRepository(this);

            // Lưu vào Room Database trong luồng phụ
            new Thread(() -> {
                try {
                    foodRepository.insert(newFood);
                    runOnUiThread(() -> {
                        showAlert("Thành công", "Thêm sản phẩm thành công!", true);
                        startActivity(new Intent(AddFoodActivity.this, MenuManagement.class));
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> showAlert("Lỗi", "Không thể thêm sản phẩm, vui lòng thử lại sau!"));
                }
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
                        selectedCategoryId = categoryList.get(position).getCategoryId();
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                });
            });
        }).start();
    }

    private void loadStockStatus() {
        spinnerStockStatuslist = new ArrayList<>();
        spinnerStockStatuslist.add("Available");
        spinnerStockStatuslist.add("Out of Stock");

        // Tạo Adapter và gán vào Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerStockStatuslist);
        spinnerStockStatus.setAdapter(adapter);

        spinnerStockStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                StockStatus = spinnerStockStatuslist.get(position);
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

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showAlert(String title, String message, boolean closeActivity) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    if (closeActivity) finish();
                })
                .show();
    }

    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1); // Trả về -1 nếu không tìm thấy userId
    }
}