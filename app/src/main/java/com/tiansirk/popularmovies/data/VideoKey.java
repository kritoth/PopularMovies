package com.tiansirk.popularmovies.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "videokeys_table", foreignKeys = @ForeignKey(
                entity = Movie.class,
                parentColumns = "onlineId",
                childColumns = "movieId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class VideoKey {
    @ColumnInfo(index = true)
    private int movieId; //This is the Foreign Key
    private String videoKey;
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @Ignore
    public VideoKey(int movieId, String videoKey) {
        this.movieId = movieId;
        this.videoKey = videoKey;
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

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nMovie ID: " + getMovieId()
                + "\nReview: " + getVideoKey().substring(0, 3) + " ..."
                + "\nDbase Id: " + getId();
    }
}
