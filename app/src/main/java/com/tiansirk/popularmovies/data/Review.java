package com.tiansirk.popularmovies.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviews_table", foreignKeys = @ForeignKey(
        entity = FavoriteMovie.class,
        parentColumns = "onlineId",
        childColumns = "movieId",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class Review {
    @ColumnInfo(index = true)
    private int movieId; //This is the Foreign Key
    private String review;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Review(int id, int movieId, String review) {
        this.id = id;
        this.movieId = movieId;
        this.review = review;
    }

    @Ignore
    public Review(int movieId, String review) {
        this.movieId = movieId;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
