/*
package com.example.f_food.Repository;

import android.content.Context;
import android.os.AsyncTask;

//import com.example.f_food.DAO.ReviewDAO;
import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.Entity.Review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewRepository {
    private ReviewDAO reviewDAO;
    private ExecutorService executorService;

    public ReviewRepository(Context context) {
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(context);
        reviewDAO = db.reviewDAO();
        executorService = Executors.newSingleThreadExecutor();

        // Kiểm tra nếu chưa có dữ liệu, thì thêm dữ liệu mẫu
        executorService.execute(() -> {
            if (reviewDAO.getAllReviews().isEmpty()) {
                insertSampleData();
            }
        });
    }

    public void getAllReviews(Callback<List<Review>> callback) {
        executorService.execute(() -> callback.onResult(reviewDAO.getAllReviews()));
    }

    public void getReviewById(int id, Callback<Review> callback) {
        executorService.execute(() -> callback.onResult(reviewDAO.getReviewById(id)));
    }

    public void getReviewsByRestaurantId(int restaurantId, Callback<List<Review>> callback) {
        executorService.execute(() -> callback.onResult(reviewDAO.getReviewsByRestaurantId(restaurantId)));
    }

    public void getReviewsByUserId(int userId, Callback<List<Review>> callback) {
        executorService.execute(() -> callback.onResult(reviewDAO.getReviewsByUserId(userId)));
    }

    public void deleteById(int id) {
        executorService.execute(() -> reviewDAO.deleteById(id));
    }

    public void deleteAll() {
        executorService.execute(reviewDAO::deleteAll);
    }

    public void insert(Review review) {
        executorService.execute(() -> reviewDAO.insert(review));
    }

    public void insertAll(List<Review> reviews) {
        executorService.execute(() -> reviewDAO.insertAll(reviews));
    }

    public void update(Review review) {
        executorService.execute(() -> reviewDAO.update(review));
    }

    private void insertSampleData() {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        List<Review> sampleReviews = List.of(
                new Review(1, 1, 5, "Great food and service!", currentTime),
                new Review(2, 1, 4, "Nice ambiance, but a bit pricey.", currentTime),
                new Review(3, 2, 3, "Average taste, nothing special.", currentTime),
                new Review(1, 3, 5, "Loved it! Will come back again.", currentTime)
        );

        reviewDAO.insertAll(sampleReviews);
    }

    public interface Callback<T> {
        void onResult(T result);
    }
}
*/
