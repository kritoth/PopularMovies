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
    @Query("SELECT * FROM reviews_table")
    List<Review> loadAllReviews();

    //Update


    //Delete
    @Delete
    int removeReview(Review review);

}
