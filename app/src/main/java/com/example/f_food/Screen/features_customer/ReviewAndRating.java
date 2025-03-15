package com.example.f_food.Screen.features_customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.DAO.ReviewDAO;
import com.example.f_food.Entity.Review;
import com.example.f_food.R;
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
            foodId = getIntent().getIntExtra("food_id", -1); // Lấy food_id từ Intent, -1 là giá trị mặc định nếu không có
            userId = getIntent().getIntExtra("user_id", -1);

            // Set data to UI
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
            // Lấy thời gian hiện tại
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
}
