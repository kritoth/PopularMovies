package com.tiansirk.popularmovies.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TrailerDAO {

    //Create
    @Insert
    long insertTrailer(VideoKey videoKey);

    //Read
    @Query("SELECT * FROM videokeys_table WHERE movieId= :movieId")
    List<VideoKey> loadTrailersByMovie(int movieId);

    @Query("SELECT * FROM videokeys_table")
    List<VideoKey> loadAllTrailers();

    @Query("SELECT COUNT (id) FROM videokeys_table")
    int getTrailersCount();

    //Update


    //Delete
    @Delete
    int removeTrailer(VideoKey videoKey);

    @Query("DELETE FROM videokeys_table")
    void deleteAllTrailer();
}
