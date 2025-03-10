package com.example.f_food.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity
 public class Review {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "review_id")
    private int reviewId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "restaurant_id")
    private int restaurantId;

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    public Review(int userId, int restaurantId, int rating, String comment, String createdAt) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
