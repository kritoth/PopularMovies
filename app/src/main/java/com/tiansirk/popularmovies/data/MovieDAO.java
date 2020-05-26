package com.tiansirk.popularmovies.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM fav_movies_table ORDER BY userRating")
    List<FavoriteMovie> loadAllFavMoviesByRating();

    @Query("SELECT * FROM fav_movies_table ORDER BY dateAdded")
    List<FavoriteMovie> loadAllFavMoviesByDateAdded();

    @Query("SELECT * FROM fav_movies_table WHERE onlineId= :id")
    FavoriteMovie loadFavMovie(int id);

    @Insert
    void insertFavMovie(FavoriteMovie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateFavMovie(FavoriteMovie movie);

    @Delete
    void removeFavMovie(FavoriteMovie movie);

    @Query("SELECT * FROM reviews_table")
    List<Review> loadAllReviews();

    @Insert
    void insertReview(Review review);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateReview(Review review);

    @Delete
    void deleteReview(Review review);

    @Query("SELECT * FROM videokeys_table")
    List<VideoKey> loadAllTrailers();

    @Insert
    void insertTrailer(VideoKey videoKey);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateTrailer(VideoKey videoKey);

    @Delete
    void deleteTrailer(VideoKey videoKey);
}
