package com.example.f_food.screen.features_customer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.dao.ReviewDAO;
import com.example.f_food.entity.Review;
import com.example.f_food.R;
import com.example.f_food.repository.ReviewRepository;
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
}
