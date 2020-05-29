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
    @Query("SELECT * FROM videokeys_table")
    List<VideoKey> loadAllTrailers();

    //Update


    //Delete
    @Delete
    int removeTrailer(VideoKey videoKey);
}
