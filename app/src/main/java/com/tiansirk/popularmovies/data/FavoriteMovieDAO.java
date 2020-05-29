package com.tiansirk.popularmovies.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface FavoriteMovieDAO {

    //Create
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Transaction
    long insertFavMovie(FavoriteMovie favoriteMovie);

    //Read
    @Query("SELECT * FROM fav_movies_table ORDER BY userRating")
    List<FavoriteMovie> loadAllFavMoviesByRating();

    @Query("SELECT * FROM fav_movies_table ORDER BY dateAdded")
    List<FavoriteMovie> loadAllFavMoviesByDateAdded();

    @Query("SELECT * FROM fav_movies_table WHERE onlineId= :id")
    FavoriteMovie loadFavMovie(int id);

    //Update


    //Delete
    @Delete
    int removeFavMovie(FavoriteMovie movie);

/*
    public void insertFavMovieWithReviewAndTrailer(FavoriteMovie movie, List<Review> reviews, List<VideoKey> videoKeys){
        insertFavMovie(movie);
        for(Review review : reviews){
            review.setMovieId(movie.getOnlineId());
            insertReview(review);
        }
        for(VideoKey videoKey : videoKeys){
            videoKey.setMovieId(movie.getOnlineId());
            insertTrailer(videoKey);
        }
    }
    @Delete()
    @Transaction
    public void removeFavMovieWithReviewAndTrailer(FavoriteMovie movie, List<Review> reviews, List<VideoKey> videoKeys){
        removeFavMovie(movie);
        for(Review review : reviews){
            review.setMovieId(movie.getOnlineId());
            removeReview(review);
        }
        for(VideoKey videoKey : videoKeys){
            videoKey.setMovieId(movie.getOnlineId());
            removeTrailer(videoKey);
        }
    }

 */
}
