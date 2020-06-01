package com.tiansirk.popularmovies.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviews_table", foreignKeys = @ForeignKey(
        entity = Movie.class,
        parentColumns = "onlineId",
        childColumns = "movieId",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class Review {
    @ColumnInfo(index = true)
    private int movieId; //This is the Foreign Key
    private String review;
    @PrimaryKey(autoGenerate = true)
    @NonNull
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nMovie ID: " + getMovieId()
                + "\nReview: " + getReview().substring(0, 9) + " ..."
                + "\nDbase Id: " + getId();
    }
}
