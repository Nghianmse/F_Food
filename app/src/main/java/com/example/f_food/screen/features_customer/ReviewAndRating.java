package com.example.f_food.screen.features_customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.dao.ReviewDAO;
import com.example.f_food.entity.Review;
import com.example.f_food.R;
import com.example.f_food.screen.authentication_authorization.LoginActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewAndRating extends AppCompatActivity {

    private TextView textViewFoodName;
    private ImageView imageViewFood;
    private RatingBar ratingBar;
    private EditText editTextFeedback;
    private Button buttonSend;

    private int orderId, restaurantId, foodId, userId;
    private String foodName, foodImage;
    private ReviewDAO reviewDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_rating);

        // Check if the user is logged in
        if (!isUserLoggedIn()) {
            showAlertDialog("Bạn chưa đăng nhập, bạn vui lòng đăng nhập để tiếp tục.");
            return;  // Exit the activity if not logged in
        }

        // Bind UI components
        textViewFoodName = findViewById(R.id.textViewFoodName);
        imageViewFood = findViewById(R.id.imageViewFood);
        ratingBar = findViewById(R.id.ratingBar);
        editTextFeedback = findViewById(R.id.editTextFeedback);
        buttonSend = findViewById(R.id.buttonSend);

        // Initialize Room Database
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(this);
        reviewDAO = db.reviewDAO();

        // Get data from Intent
        if (getIntent() != null) {
            orderId = getIntent().getIntExtra("order_id", -1);
            foodName = getIntent().getStringExtra("food_name");
            foodImage = getIntent().getStringExtra("food_image");
            restaurantId = getIntent().getIntExtra("restaurant_id", -1);
            foodId = getIntent().getIntExtra("food_id", -1); // Get food_id from Intent, -1 is default if not found
            userId = getLoggedInUserId();  // Get logged-in user ID
            textViewFoodName.setText(foodName);
            if (foodImage != null && !foodImage.isEmpty()) {
                Picasso.get().load(foodImage).resize(500, 500).centerCrop().into(imageViewFood);
            }
        }

        // Handle when the Send button is clicked
        buttonSend.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String feedback = editTextFeedback.getText().toString();

            if (rating == 0 || feedback.isEmpty()) {
                Toast.makeText(this, "Please enter a rating and feedback!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get current time
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            // Save the review into Room Database
            Review review = new Review(userId, restaurantId, (int) rating, feedback, currentTime, foodName, foodImage); // Pass the foodName and foodImage
            reviewDAO.insert(review);

            new AlertDialog.Builder(ReviewAndRating.this)
                    .setTitle("Đánh giá thành công")
                    .setMessage("Cảm ơn bạn đã đánh giá sản phẩm này!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        finish(); // Go back to the previous screen after clicking OK
                    })
                    .show();
        });
    }

    // Check if user is logged in
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        return userId != -1;
    }

    // Get logged-in userId
    private int getLoggedInUserId() {
        SharedPreferences preferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        return preferences.getInt("userId", -1); // Return -1 if userId not found
    }

    // Show alert dialog and redirect to Login screen if not logged in
    private void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Navigate to Login screen
                    Intent intent = new Intent(ReviewAndRating.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create()
                .show();
    }
}
