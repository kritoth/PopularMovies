package com.tiansirk.popularmovies.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ReviewDAO {

    //Create
    @Insert
    long insertReview(Review review);

    //Read
    @Query("SELECT * FROM reviews_table WHERE movieId= :movieId")
    List<Review> loadReviewsByMovie(int movieId);

    @Query("SELECT * FROM reviews_table")
    List<Review> loadAllReviews();

    @Query("SELECT COUNT (id) FROM reviews_table")
    int getReviewsCount();

    //Update


    //Delete
    @Delete
    int removeReview(Review review);

    @Query("DELETE FROM reviews_table")
    void deleteAllReview();
}
