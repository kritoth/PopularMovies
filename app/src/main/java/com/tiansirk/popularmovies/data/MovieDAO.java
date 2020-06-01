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
public interface MovieDAO {

    //Create
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertFavMovie(Movie favoriteMovie);

    //Read
    @Query("SELECT EXISTS(SELECT 1 FROM movies_table WHERE onlineId= :id)")
    int searchMovie(int id);

    @Query("SELECT * FROM movies_table ORDER BY onlineId")
    List<Movie> loadAllFavMoviesById();

    @Query("SELECT * FROM movies_table ORDER BY userRating")
    List<Movie> loadAllFavMoviesByRating();

    @Query("SELECT * FROM movies_table ORDER BY dateAddedToFav")
    List<Movie> loadAllFavMoviesByDateAdded();

    @Query("SELECT * FROM movies_table WHERE onlineId= :id")
    Movie loadFavMovie(int id);

    //Update


    //Delete
    @Delete
    int removeFavMovie(Movie movie);

    @Query("DELETE FROM movies_table")
    void deleteAllFavMovie();

}
