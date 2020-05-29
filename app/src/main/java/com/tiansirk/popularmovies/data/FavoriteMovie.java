package com.tiansirk.popularmovies.data;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_movies_table")
public class FavoriteMovie extends Movie{

    private String posterImgUrl;
    private String plotSynopsis;
    private String releaseDate;
    private String title;
    private double userRating;
    private Date dateAdded;
    @PrimaryKey
    @NonNull
    private int onlineId; // Equals to TMDB id, should never be null //This is the key used in {@class Review} and {@class Trailer} as FOREIGN KEY

    public FavoriteMovie() {
    }

    /**
     * Constructor for Movie item. If any of the required params are not existing then an empty object of that type needed to be inserted
     * @param posterPath Complete URL path for an image resource as String
     * @param overview String with any length
     * @param releaseDate String with any length
     * @param originalTitle String with any length
     * @param voteAverage a double
     * @param dateAdded the date when the instance created
     * @param onlineId The id of the Movie in TMDB
     */
    @Ignore
    public FavoriteMovie(String posterPath, String overview, String releaseDate, String originalTitle,
                         double voteAverage, Date dateAdded, int onlineId){
        this.posterImgUrl = posterPath;
        this.plotSynopsis = overview;
        this.releaseDate = releaseDate;
        this.title = originalTitle;
        this.userRating = voteAverage;
        this.dateAdded = dateAdded;
        this.onlineId = onlineId;
    }

    /**
     * @return a String representing a poster path URL from where the poster image can be fetched
     * the complete path consists of the basic path: "http://image.tmdb.org/t/p/w185/" and then the "relativePath"
     */
    public String getPosterImgUrl() {
        return posterImgUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public double getUserRating() {
        return userRating;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public int getOnlineId() {
        return onlineId;
    }

    public void setPosterImgUrl(String posterImgUrl) {
        this.posterImgUrl = posterImgUrl;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setOnlineId(int onlineId) {
        this.onlineId = onlineId;
    }
}
