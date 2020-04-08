package com.tiansirk.popularmovies;

import androidx.annotation.NonNull;

/**
 * Class for Movie objects to be presented to the user.
 */
public class Movie {
    private String posterImgUrl;
    private String plotSynopsis;
    private String releaseDate;
    private String title;
    private double userRating;


    public Movie(String posterPath, String overview, String releaseDate, String originalTitle, double voteAverage){
        this.posterImgUrl = posterPath;
        this.plotSynopsis = overview;
        this.releaseDate = releaseDate;
        this.title = originalTitle;
        this.userRating = voteAverage;
    }

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

    private boolean hasImage(){
        if(posterImgUrl.isEmpty()) return false;
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n Title: " + getTitle() +
                "\n Has a poster: " + hasImage() +
                "\n Plot synopsis: " + getPlotSynopsis().substring(0,100) + "..." +
                "\n Release date: " + getReleaseDate() +
                "\n User rating: " + getUserRating();
    }
}
