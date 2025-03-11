package com.example.f_food.Repository;

import android.content.Context;
import android.os.AsyncTask;
import com.example.f_food.DAO.ReviewDAO;
import com.example.f_food.DAO.RestaurantRoomDatabase;
import com.example.f_food.Entity.Review;
import java.util.List;

public class ReviewRepository {
    private ReviewDAO reviewDAO;

    public ReviewRepository(Context context) {
        RestaurantRoomDatabase database = RestaurantRoomDatabase.getInstance(context);
        reviewDAO = database.reviewDAO();
    }

    public void insertReview(Review review) {
        new InsertReviewAsyncTask(reviewDAO).execute(review);
    }

    public List<Review> getAllReviews() {
        return reviewDAO.getAllReviews();
    }

    public List<Review> getReviewsByRestaurantId(int restaurantId) {
        return reviewDAO.getReviewsByRestaurantId(restaurantId);
    }

    public List<Review> getReviewsByUserId(int userId) {
        return reviewDAO.getReviewsByUserId(userId);
    }

    public void updateReview(Review review) {
        new UpdateReviewAsyncTask(reviewDAO).execute(review);
    }

    public void deleteReview(Review review) {
        new DeleteReviewAsyncTask(reviewDAO).execute(review);
    }

    private static class InsertReviewAsyncTask extends AsyncTask<Review, Void, Void> {
        private ReviewDAO reviewDAO;

        private InsertReviewAsyncTask(ReviewDAO reviewDAO) {
            this.reviewDAO = reviewDAO;
        }

        @Override
        protected Void doInBackground(Review... reviews) {
            reviewDAO.insert(reviews[0]);
            return null;
        }
    }

    private static class UpdateReviewAsyncTask extends AsyncTask<Review, Void, Void> {
        private ReviewDAO reviewDAO;

        private UpdateReviewAsyncTask(ReviewDAO reviewDAO) {
            this.reviewDAO = reviewDAO;
        }

        @Override
        protected Void doInBackground(Review... reviews) {
            reviewDAO.update(reviews[0]);
            return null;
        }
    }

    private static class DeleteReviewAsyncTask extends AsyncTask<Review, Void, Void> {
        private ReviewDAO reviewDAO;

        private DeleteReviewAsyncTask(ReviewDAO reviewDAO) {
            this.reviewDAO = reviewDAO;
        }

        @Override
        protected Void doInBackground(Review... reviews) {
            reviewDAO.delete(reviews[0]);
            return null;
        }
    }
}
