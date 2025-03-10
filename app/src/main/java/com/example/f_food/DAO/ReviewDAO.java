/*
package com.example.f_food.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.f_food.Entity.Review;

import java.util.List;

@Dao
public interface ReviewDAO {

    @Query("SELECT * FROM Reviews")
    List<Review> getAllReviews();

    @Query("SELECT * FROM Reviews WHERE review_id = :id")
    Review getReviewById(int id);

    @Query("SELECT * FROM Reviews WHERE restaurant_id = :restaurantId")
    List<Review> getReviewsByRestaurantId(int restaurantId);

    @Query("SELECT * FROM Reviews WHERE user_id = :userId")
    List<Review> getReviewsByUserId(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Review review);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Review> reviews);

    @Update
    void update(Review review);

    @Delete
    void delete(Review review);

    @Query("DELETE FROM Reviews WHERE review_id = :id")
    void deleteById(int id);

    @Query("DELETE FROM Reviews")
    void deleteAll();
}
*/
