package com.tiansirk.popularmovies.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "videokeys_table", foreignKeys = @ForeignKey(
                entity = FavoriteMovie.class,
                parentColumns = "onlineId",
                childColumns = "movieId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class VideoKey {
@PrimaryKey(autoGenerate = true)
    private int id;
    private int movieId;
    private String videoKey;

    @Ignore
    public VideoKey(String videoKey, int movieId) {
        this.videoKey = videoKey;
        this.movieId = movieId;
    }

    public VideoKey(int id, String videoKey, int movieId) {
        this.id = id;
        this.videoKey = videoKey;
        this.movieId = movieId;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }
}
