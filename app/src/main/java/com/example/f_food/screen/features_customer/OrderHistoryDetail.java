package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.dao.ReviewDAO;
import com.example.f_food.entity.Review;
import com.example.f_food.R;
import com.example.f_food.repository.ReviewRepository;
import com.example.f_food.screen.authentication_authorization.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderHistoryDetail extends AppCompatActivity {
    private TextView textViewFoodName, textViewFoodDetails, textViewFeedbacks;
    private ImageView imageViewFood;
    private RatingBar ratingBar;

    private int foodId, restaurantId;
    private String foodName, foodImage;
    private ReviewDAO reviewDAO;
    private ReviewRepository reviewRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (!isUserLoggedIn()) {
            showAlertDialog("Bạn chưa đăng nhập, bạn vui lòng đăng nhập để thao tác.");
            return;
        }

        // Bind UI components
        textViewFoodName = findViewById(R.id.textViewFoodName);
        textViewFoodDetails = findViewById(R.id.textViewFoodDetails);
        textViewFeedbacks = findViewById(R.id.textViewFeedbacks);
        imageViewFood = findViewById(R.id.imageViewFood);
        ratingBar = findViewById(R.id.ratingBar);

        // Initialize Room Database
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(this);
        reviewDAO = db.reviewDAO();

        // Get data from Intent
        if (getIntent() != null) {
            foodName = getIntent().getStringExtra("food_name");
            foodImage = getIntent().getStringExtra("food_image");
            foodId = getIntent().getIntExtra("food_id", -1);
            restaurantId = getIntent().getIntExtra("restaurant_id", -1);

            // Set data to UI
            textViewFoodName.setText(foodName);
            textViewFoodDetails.setText("Details about the product..."); // Add any additional information about the product
            if (foodImage != null && !foodImage.isEmpty()) {
                Picasso.get().load(foodImage).resize(500, 500).centerCrop().into(imageViewFood);
            }

            // Load reviews and calculate average rating
            loadReviews();
        }
    }

    private void loadReviews() {
        reviewRepository = new ReviewRepository(this);
        List<Review> reviews = reviewRepository.getAllReviews();

        if (reviews.isEmpty()) {
            textViewFeedbacks.setText("No reviews yet.");
        } else {
            StringBuilder feedbackText = new StringBuilder();
            float totalRating = 0;
            for (Review review : reviews) {
                feedbackText.append("Rating: ").append(review.getRating())
                        .append("\nFeedback: ").append(review.getComment())
                        .append("\nDate: ").append(review.getCreatedAt())
                        .append("\n\n");
                totalRating += review.getRating();
            }
            textViewFeedbacks.setText(feedbackText.toString());
            // Set average rating
            float averageRating = totalRating / reviews.size();
            ratingBar.setRating(averageRating);
        }
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );


    // Kiểm tra người dùng đã đăng nhập chưa
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1); // Sử dụng PreferenceManager thay vì getSharedPreferences
        return userId != -1;
    }

    // Hiển thị hộp thoại thông báo và chuyển sang màn hình đăng nhập
    private void showAlertDialog(String message) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(OrderHistoryDetail.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create()
                .show();
    }
}
